package cn.GnaixEuy.service;

import cn.GnaixEuy.bo.UpdatedUserBO;
import cn.GnaixEuy.pojo.Users;

/**
 * <img src="http://blog.gnaixeuy.cn/wp-content/uploads/2022/09/倒闭.png"/>
 *
 * <p>项目： media-v2-new </p>
 * 创建日期： 2023/2/27
 *
 * @author GnaixEuy
 * @version 1.0.0
 * @see <a href="https://github.com/GnaixEuy"> GnaixEuy的GitHub </a>
 */

public interface UserService {

    /**
     * 判断用户是否存在，如果存在则返回用户信息
     */
    Users queryMobileIsExist(String mobile);


    /**
     * 创建用户信息，并且返回用户对象
     */
    Users createUser(String mobile);

    /**
     * 根据用户主键查询用户信息
     */
    Users getUser(String userId);

    /**
     * 用户信息修改
     */
    Users updateUserInfo(UpdatedUserBO updatedUserBO);

    /**
     * 用户信息修改
     */
    Users updateUserInfo(UpdatedUserBO updatedUserBO, Integer type);
}
