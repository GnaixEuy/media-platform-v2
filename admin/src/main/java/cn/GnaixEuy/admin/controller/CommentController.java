package cn.GnaixEuy.admin.controller;

import cn.GnaixEuy.admin.service.CommentService;
import cn.GnaixEuy.admin.utils.ResponseResult;
import cn.GnaixEuy.admin.vo.AdminCommentInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <img src="http://blog.gnaixeuy.cn/wp-content/uploads/2022/09/倒闭.png"/>
 *
 * <p>项目： media-v2 </p>
 * 创建日期： 2023/3/9
 *
 * @author GnaixEuy
 * @version 1.0.0
 * @see <a href="https://github.com/GnaixEuy"> GnaixEuy的GitHub </a>
 */
@RestController
@RequestMapping(value = {"comment"})
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping(value = {"{vlogId}"})
    public ResponseResult<List<AdminCommentInfoVo>> getCommentListByVlogId(@PathVariable String vlogId) {
        return ResponseResult.success(this.commentService.getCommentListByVlogId(vlogId));
    }


    @DeleteMapping(value = {"{commentId}"})
    public ResponseResult<String> delete(@PathVariable String commentId) {
        this.commentService.removeById(commentId);
        return ResponseResult.success("移除成功");
    }

}
