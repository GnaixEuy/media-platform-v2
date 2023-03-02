package cn.GnaixEuy.comment.service;

import cn.GnaixEuy.common.utils.PagedGridResult;
import cn.GnaixEuy.model.bo.CommentBO;
import cn.GnaixEuy.model.pojo.Comment;
import cn.GnaixEuy.model.vo.CommentVO;
import com.baomidou.mybatisplus.extension.service.IService;

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

public interface CommentService extends IService<Comment> {

    /**
     * 发表评论
     */
    CommentVO createComment(CommentBO commentBO);

    /**
     * 查询评论的列表
     */
    PagedGridResult queryVlogComments(String vlogId,
                                      String userId,
                                      Integer page,
                                      Integer pageSize);

    /**
     * 删除评论
     */
    void deleteComment(String commentUserId,
                       String commentId,
                       String vlogId);

    /**
     * 根据主键查询comment
     */
    Comment getComment(String id);
}

