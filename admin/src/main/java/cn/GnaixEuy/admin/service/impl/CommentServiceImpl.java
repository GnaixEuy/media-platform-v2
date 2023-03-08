package cn.GnaixEuy.admin.service.impl;

import cn.GnaixEuy.admin.dao.CommentDao;
import cn.GnaixEuy.admin.dao.UsersDao;
import cn.GnaixEuy.admin.service.CommentService;
import cn.GnaixEuy.admin.vo.AdminCommentInfoVo;
import cn.GnaixEuy.model.pojo.Comment;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
@Service
public class CommentServiceImpl extends ServiceImpl<CommentDao, Comment> implements CommentService {

    @Autowired
    private UsersDao usersDao;

    /**
     * @param vlogId
     * @return
     */
    @Override
    public List<AdminCommentInfoVo> getCommentListByVlogId(String vlogId) {

        List<Comment> list = this.list(Wrappers
                .<Comment>lambdaQuery()
                .eq(Comment::getVlogId, vlogId));
        List<AdminCommentInfoVo> adminCommentInfoVoList = new ArrayList<>();
        list.forEach(item -> {
            AdminCommentInfoVo adminCommentInfoVo = new AdminCommentInfoVo();
            adminCommentInfoVo.setId(item.getId());
            adminCommentInfoVo.setContent(item.getContent());
            adminCommentInfoVo.setCreateTime(item.getCreateTime());
            adminCommentInfoVo.setCommentUser(this.usersDao.getByIdIgnoreIsDeleted(item.getCommentUserId()));
            adminCommentInfoVoList.add(adminCommentInfoVo);
        });
        return adminCommentInfoVoList;
    }
}
