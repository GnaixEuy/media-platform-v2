package cn.GnaixEuy.ucenter.service;

import cn.GnaixEuy.ucenter.entity.Member;
import cn.GnaixEuy.ucenter.model.form.MemberForm;
import com.baomidou.mybatisplus.extension.service.IService;

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
public interface MemberService extends IService<Member> {

    /**
     * 会员登录
     *
     * @param memberForm 会员表单
     * @return token
     */
    String login(MemberForm memberForm);

    /**
     * 会员注册
     *
     * @param memberForm 会员表单
     */
    void register(MemberForm memberForm);

    /**
     * 根据openid 获取用户信息
     *
     * @param openid
     * @return 会员
     */
    Member getMemberByOpenid(String openid);

    /**
     * 查询指定day的注册人数
     *
     * @param day
     * @return 注册人数
     */
    Integer countRegisterDay(String day);
}
