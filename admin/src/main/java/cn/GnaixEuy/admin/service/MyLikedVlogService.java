package cn.GnaixEuy.admin.service;

import cn.GnaixEuy.admin.vo.AdminLikeInfoVo;
import cn.GnaixEuy.model.pojo.MyLikedVlog;
import com.baomidou.mybatisplus.extension.service.IService;

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
public interface MyLikedVlogService extends IService<MyLikedVlog> {

    /**
     * 通过vlogID获取点赞时间和人员名称
     *
     * @param vlogId
     * @return
     */
    List<AdminLikeInfoVo> getLikePeopleByVlogId(String vlogId);

}
