package cn.GnaixEuy.admin.service.impl;

import cn.GnaixEuy.admin.dao.MyLikedVlogDao;
import cn.GnaixEuy.admin.dao.UsersDao;
import cn.GnaixEuy.admin.service.MyLikedVlogService;
import cn.GnaixEuy.admin.vo.AdminLikeInfoVo;
import cn.GnaixEuy.model.pojo.MyLikedVlog;
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
 * 创建日期： 2023/3/8
 *
 * @author GnaixEuy
 * @version 1.0.0
 * @see <a href="https://github.com/GnaixEuy"> GnaixEuy的GitHub </a>
 */
@Service
public class MyLikedVlogServiceImpl extends ServiceImpl<MyLikedVlogDao, MyLikedVlog> implements MyLikedVlogService {

    @Autowired
    private UsersDao usersDao;


    /**
     * 通过vlogID获取点赞时间和人员名称
     *
     * @param vlogId
     * @return
     */
    @Override
    public List<AdminLikeInfoVo> getLikePeopleByVlogId(String vlogId) {
        List<MyLikedVlog> myLikedVlogs = this.baseMapper.selectList(Wrappers
                .<MyLikedVlog>lambdaQuery()
                .eq(MyLikedVlog::getVlogId, vlogId));
        List<AdminLikeInfoVo> adminLikeInfoVos = new ArrayList<>(myLikedVlogs.size());
        myLikedVlogs.forEach(item -> {
            AdminLikeInfoVo adminLikeInfoVo = new AdminLikeInfoVo();
            adminLikeInfoVo.setId(item.getId());
            String userId = item.getId();
            adminLikeInfoVo.setUserId(userId);
            adminLikeInfoVo.setUserNickname(this.usersDao.getByIdIgnoreIsDeleted(userId).getNickname());
            adminLikeInfoVo.setVlogId(item.getVlogId());
            adminLikeInfoVo.setCreatedTime(item.getCreatedTime());
            adminLikeInfoVos.add(adminLikeInfoVo);
        });
        return adminLikeInfoVos;
    }
}
