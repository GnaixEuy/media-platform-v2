package cn.GnaixEuy.admin.dao;

import cn.GnaixEuy.model.pojo.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * <img src="http://blog.gnaixeuy.cn/wp-content/uploads/2022/09/倒闭.png"/>
 *
 * <p>项目： media-v2 </p>
 * 创建日期： 2023/3/6
 *
 * @author GnaixEuy
 * @version 1.0.0
 * @see <a href="https://github.com/GnaixEuy"> GnaixEuy的GitHub </a>
 */
@Mapper
public interface UsersDao extends BaseMapper<Users> {

    @Select("SELECT * FROM `users` WHERE id= #{id} AND ( deleted = 0 OR deleted = 1)")
    Users getByIdIgnoreIsDeleted(String id);

}
