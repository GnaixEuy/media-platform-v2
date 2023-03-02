package cn.GnaixEuy.comment.controller;

import cn.GnaixEuy.comment.client.MessageFeignClient;
import cn.GnaixEuy.comment.client.VlogFeignClient;
import cn.GnaixEuy.comment.service.CommentService;
import cn.GnaixEuy.common.enmus.MessageEnum;
import cn.GnaixEuy.common.utils.JSONResult;
import cn.GnaixEuy.model.bo.CommentBO;
import cn.GnaixEuy.model.bo.feign.CreateMsgBo;
import cn.GnaixEuy.model.pojo.Comment;
import cn.GnaixEuy.model.pojo.Vlog;
import cn.GnaixEuy.model.vo.CommentVO;
import cn.GnaixEuy.utils.RedisUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static cn.GnaixEuy.properties.BaseInfoProperties.*;

/**
 * <img src="http://blog.gnaixeuy.cn/wp-content/uploads/2022/09/倒闭.png"/>
 *
 * <p>项目： media-v2 </p>
 * 创建日期： 2023/3/1
 *
 * @author GnaixEuy
 * @version 1.0.0
 * @see <a href="https://github.com/GnaixEuy"> GnaixEuy的GitHub </a>
 */
@Slf4j
@Api(tags = "CommentController 评论模块的接口")
@RequestMapping(value = {"comment"})
@RestController
public class CommentController {
    @Autowired
    private RedisUtils redis;
    @Autowired
    private CommentService commentService;
    @Autowired
    private MessageFeignClient messageFeignClient;
    @Autowired
    private VlogFeignClient vlogFeignClient;

    @PostMapping("create")
    public JSONResult create(@RequestBody @Valid CommentBO commentBO)
            throws Exception {

        CommentVO commentVO = commentService.createComment(commentBO);
        return JSONResult.ok(commentVO);
    }

    @GetMapping("counts")
    public JSONResult counts(@RequestParam String vlogId) {
        String countsStr = this.redis.get(REDIS_VLOG_COMMENT_COUNTS + ":" + vlogId);
        if (StringUtils.isBlank(countsStr)) {
            countsStr = "0";
        }

        return JSONResult.ok(Integer.valueOf(countsStr));
    }

    @GetMapping("list")
    public JSONResult list(@RequestParam String vlogId,
                           @RequestParam(defaultValue = "") String userId,
                           @RequestParam Integer page,
                           @RequestParam Integer pageSize) {

        return JSONResult.ok(
                commentService.queryVlogComments(
                        vlogId,
                        userId,
                        page,
                        pageSize));
    }

    @DeleteMapping("delete")
    public JSONResult delete(@RequestParam String commentUserId,
                             @RequestParam String commentId,
                             @RequestParam String vlogId) {
        commentService.deleteComment(commentUserId,
                commentId,
                vlogId);
        return JSONResult.ok();
    }

    @PostMapping("like")
    public JSONResult like(@RequestParam String commentId,
                           @RequestParam String userId) {

        // 故意犯错，bigkey
        redis.incrementHash(REDIS_VLOG_COMMENT_LIKED_COUNTS, commentId, 1);
        redis.setHashValue(REDIS_USER_LIKE_COMMENT, userId + ":" + commentId, "1");
//        redis.hset(REDIS_USER_LIKE_COMMENT, userId, "1");


        // 系统消息：点赞评论
        Comment comment = commentService.getComment(commentId);
        Vlog vlog = JSON.parseObject(JSON.toJSONString(
                this.vlogFeignClient.getVlog(comment.getVlogId()).getData()), new TypeReference<Vlog>() {
        });
        Map msgContent = new HashMap();
        msgContent.put("vlogId", vlog.getId());
        msgContent.put("vlogCover", vlog.getCover());
        msgContent.put("commentId", commentId);
        this.messageFeignClient.createMsg(new CreateMsgBo(
                userId,
                comment.getCommentUserId(),
                MessageEnum.LIKE_COMMENT.type,
                msgContent)
        );
        return JSONResult.ok();
    }

    @PostMapping("unlike")
    public JSONResult unlike(@RequestParam String commentId,
                             @RequestParam String userId) {

        redis.decrementHash(REDIS_VLOG_COMMENT_LIKED_COUNTS, commentId, 1);
        redis.hdel(REDIS_USER_LIKE_COMMENT, userId + ":" + commentId);

        return JSONResult.ok();
    }
}