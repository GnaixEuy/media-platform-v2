package cn.GnaixEuy.user.entity;

import lombok.Data;

import java.util.Date;

/**
 * <img src="http://blog.gnaixeuy.cn/wp-content/uploads/2022/09/倒闭.png"/>
 *
 * <p>项目： media-v2 </p>
 * 创建日期： 2023/2/23
 *
 * @author GnaixEuy
 * @version 1.0.0
 * @see <a href="https://github.com/GnaixEuy"> GnaixEuy的GitHub </a>
 */
@Data
public class User {

    private String id;
    private Date createdDateTime;
    private Date updatedDateTime;
    private String username;
    private String password;
    private String telephone;
    private String sex;
    private String area;
    private Date birthday;

}
