package cn.GnaixEuy.admin.service;

import cn.GnaixEuy.admin.vo.AdminCommentInfoVo;
import cn.GnaixEuy.model.pojo.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

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
public interface CommentService extends IService<Comment> {
    List<AdminCommentInfoVo> getCommentListByVlogId(String vlogId);
}
