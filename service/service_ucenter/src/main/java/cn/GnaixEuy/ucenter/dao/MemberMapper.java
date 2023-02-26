package cn.GnaixEuy.ucenter.dao;

import cn.GnaixEuy.ucenter.entity.Member;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <img src="http://blog.gnaixeuy.cn/wp-content/uploads/2022/09/倒闭.png"/>
 *
 * <p>项目： media-v2 </p>
 * 创建日期： 2023/2/26
 *
 * @author GnaixEuy
 * @version 1.0.0
 * @see <a href="https://github.com/GnaixEuy"> GnaixEuy的GitHub </a>
 */
@Mapper
public interface MemberMapper extends BaseMapper<Member> {

    /**
     * 查询指定day的注册人数
     *
     * @param day 指定日期
     * @return 注册日期
     */
    Integer countRegisterDay(String day);
}
