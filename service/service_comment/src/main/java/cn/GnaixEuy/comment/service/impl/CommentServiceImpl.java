package cn.GnaixEuy.comment.service.impl;

import cn.GnaixEuy.comment.client.MessageFeignClient;
import cn.GnaixEuy.comment.client.VlogFeignClient;
import cn.GnaixEuy.comment.dao.CommentMapper;
import cn.GnaixEuy.comment.service.CommentService;
import cn.GnaixEuy.common.enmus.MessageEnum;
import cn.GnaixEuy.common.enmus.YesOrNo;
import cn.GnaixEuy.common.utils.PageResultUtil;
import cn.GnaixEuy.common.utils.PagedGridResult;
import cn.GnaixEuy.model.bo.CommentBO;
import cn.GnaixEuy.model.bo.feign.CreateMsgBo;
import cn.GnaixEuy.model.pojo.Comment;
import cn.GnaixEuy.model.pojo.Vlog;
import cn.GnaixEuy.model.vo.CommentVO;
import cn.GnaixEuy.utils.RedisUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private RedisUtils redis;
    @Autowired
    private VlogFeignClient vlogFeignClient;
    @Autowired
    private MessageFeignClient messageFeignClient;

    @Override
    public CommentVO createComment(CommentBO commentBO) {
        Comment comment = new Comment();
        comment.setVlogId(commentBO.getVlogId());
        comment.setVlogerId(commentBO.getVlogerId());
        comment.setCommentUserId(commentBO.getCommentUserId());
        comment.setFatherCommentId(commentBO.getFatherCommentId());
        comment.setContent(commentBO.getContent());
        comment.setLikeCounts(0);
        comment.setCreateTime(new Date());
        this.baseMapper.insert(comment);
        // redis操作放在service中，评论总数的累加
        this.redis.increment(REDIS_VLOG_COMMENT_COUNTS + ":" + commentBO.getVlogId(), 1);
        // 留言后的最新评论需要返回给前端进行展示
        CommentVO commentVO = new CommentVO();
        BeanUtils.copyProperties(comment, commentVO);
        // 系统消息：评论/回复
        Vlog vlog = JSON.parseObject(JSON.toJSONString(
                        this.vlogFeignClient.getVlog(commentBO.getVlogId()).getData()),
                new TypeReference<Vlog>() {
                });
        Map msgContent = new HashMap();
        msgContent.put("vlogId", vlog.getId());
        msgContent.put("vlogCover", vlog.getCover());
        msgContent.put("commentId", comment.getId());
        msgContent.put("commentContent", commentBO.getContent());
        Integer type = MessageEnum.COMMENT_VLOG.type;
        if (StringUtils.isNotBlank(commentBO.getFatherCommentId()) &&
                !commentBO.getFatherCommentId().equalsIgnoreCase("0")) {
            type = MessageEnum.REPLY_YOU.type;
        }
        this.messageFeignClient.createMsg(
                new CreateMsgBo(
                        commentBO.getCommentUserId(),
                        commentBO.getCommentUserId(),
                        type,
                        msgContent));
        return commentVO;
    }

    @Override
    public PagedGridResult queryVlogComments(String vlogId,
                                             String userId,
                                             Integer page,
                                             Integer pageSize) {
        Page<CommentVO> commentVOPage = this.baseMapper.getCommentList(
                new Page(page, pageSize),
                new HashMap() {{
                    this.put("vlogId", vlogId);
                }}
        );
        for (CommentVO cv : commentVOPage.getRecords()) {
            String commentId = cv.getCommentId();
            // 当前短视频的某个评论的点赞总数
            String countsStr = redis.getHashValue(REDIS_VLOG_COMMENT_LIKED_COUNTS, commentId);
            Integer counts = 0;
            if (StringUtils.isNotBlank(countsStr)) {
                counts = Integer.valueOf(countsStr);
            }
            cv.setLikeCounts(counts);
            // 判断当前用户是否点赞过该评论
            String doILike = (String) redis.hget(REDIS_USER_LIKE_COMMENT, userId + ":" + commentId);
            if (StringUtils.isNotBlank(doILike) && doILike.equalsIgnoreCase("1")) {
                cv.setIsLike(YesOrNo.YES.type);
            }
        }
        return PageResultUtil.setterPagedGrid(commentVOPage);
    }

    @Override
    public void deleteComment(String commentUserId,
                              String commentId,
                              String vlogId) {
        this.baseMapper.delete(Wrappers
                .<Comment>lambdaQuery()
                .eq(Comment::getId, commentId)
                .eq(Comment::getCommentUserId, commentUserId)
        );
        // 评论总数的累减
        redis.decrement(REDIS_VLOG_COMMENT_COUNTS + ":" + vlogId, 1);
    }

    @Override
    public Comment getComment(String id) {
        return this.baseMapper.selectById(id);
    }
}
