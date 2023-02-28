package cn.GnaixEuy.fans.service;

import cn.GnaixEuy.common.utils.PagedGridResult;
import cn.GnaixEuy.model.pojo.Fans;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <img src="http://blog.gnaixeuy.cn/wp-content/uploads/2022/09/倒闭.png"/>
 *
 * <p>项目： media-v2 </p>
 * 创建日期： 2023/2/28
 *
 * @author GnaixEuy
 * @version 1.0.0
 * @see <a href="https://github.com/GnaixEuy"> GnaixEuy的GitHub </a>
 */

public interface FansService extends IService<Fans> {

    /**
     * 关注
     */
    void doFollow(String myId, String vlogerId);

    /**
     * 取关
     */
    void doCancel(String myId, String vlogerId);

    /**
     * 查询用户是否关注博主
     */
    boolean queryDoIFollowVloger(String myId, String vlogerId);

    /**
     * 查询我关注的博主列表
     */
    PagedGridResult queryMyFollows(String myId,
                                   Integer page,
                                   Integer pageSize);

    /**
     * 查询我的粉丝列表
     */
    PagedGridResult queryMyFans(String myId,
                                Integer page,
                                Integer pageSize);
}
