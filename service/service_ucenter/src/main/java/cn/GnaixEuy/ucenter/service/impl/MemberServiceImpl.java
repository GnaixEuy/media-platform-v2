package cn.GnaixEuy.ucenter.service.impl;

import cn.GnaixEuy.commonutils.utils.JwtUtils;
import cn.GnaixEuy.servicebase.exception.BizException;
import cn.GnaixEuy.servicebase.exception.ExceptionType;
import cn.GnaixEuy.ucenter.dao.MemberMapper;
import cn.GnaixEuy.ucenter.entity.Member;
import cn.GnaixEuy.ucenter.model.form.MemberForm;
import cn.GnaixEuy.ucenter.service.MemberService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

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
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

    private final StringRedisTemplate redisTemplate;

    public MemberServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 会员登录
     *
     * @param memberForm 会员表单
     * @return token
     */
    @Override
    public String login(MemberForm memberForm) {
        //获取手机和密码
        String mobile = memberForm.getMobile();
        String password = memberForm.getPassword();

        if (StringUtils.isBlank(mobile) || StringUtils.isBlank(password)) {
            throw new BizException(ExceptionType.LOGIN_EXCEPTION);
        }


        //判断手机号是否正确
        LambdaQueryWrapper<Member> memberQuery = new LambdaQueryWrapper<>();
        memberQuery.eq(Member::getMobile, mobile);

        Member member = this.getOne(memberQuery);

        //判断对象是否为空
        member = Optional.ofNullable(member).orElseThrow(() -> new BizException(ExceptionType.LOGIN_EXCEPTION));


        //判断密码
        //因为存储到数据库密码肯定加密的
        //把输入的密码进行加密，再和数据密码进行比较
        //加密方式 MD5
        if (password.equalsIgnoreCase(DigestUtils.md5DigestAsHex(member.getPassword().getBytes(StandardCharsets.UTF_8)))) {
            throw new BizException(ExceptionType.LOGIN_EXCEPTION);
        }

        //判断用户是否禁用
        if (member.getIsDisabled()) {
            throw new BizException(ExceptionType.LOGIN_EXCEPTION);
        }

        //登录成功
        //生成token字符串，使用jwt字符串
        return JwtUtils.getJwtToken(member.getId(), member.getNickname());
    }

    /**
     * 会员注册
     *
     * @param memberForm 会员表单
     */
    @Override
    public void register(MemberForm memberForm) {
        //获取注册的数据
        String mobile = memberForm.getMobile();
        String password = memberForm.getPassword();
        String code = memberForm.getCode();
        String nickname = memberForm.getNickname();
        //非空判断
        if (StringUtils.isBlank(mobile)
                || StringUtils.isBlank(password)
                || StringUtils.isBlank(code)
                || StringUtils.isBlank(nickname)
        ) {
            throw new BizException(ExceptionType.REGISTER_EXCEPTION);
        }
        //判断验证码
        //获取redis验证码
//        String redisCode = redisTemplate.opsForValue().get(mobile);
//        if (!code.equalsIgnoreCase(redisCode)) {
//            throw new BizException(ExceptionType.REGISTER_EXCEPTION);
//        }

        //判断手机号是否重复，表里面是否存在相同手机号不进行添加
        LambdaQueryWrapper<Member> memberQuery = new LambdaQueryWrapper<>();
        memberQuery.eq(Member::getMobile, mobile);
        long count = this.count(memberQuery);
        if (count > 0) {
            throw new BizException(ExceptionType.REGISTER_EXCEPTION);
        }
        //数据添加到数据库
        Member member = new Member();
        BeanUtils.copyProperties(memberForm, member);
        //密码需要做加密处理
        member.setPassword(DigestUtils.md5DigestAsHex(member.getPassword().getBytes(StandardCharsets.UTF_8)));
        this.save(member);
    }

    /**
     * 根据openid 获取用户信息
     *
     * @param openid
     * @return 会员
     */
    @Override
    public Member getMemberByOpenid(String openid) {
//        LambdaQueryWrapper<Member> memberQuery = new LambdaQueryWrapper<>();
//        memberQuery.eq(Member::getOpenid, openid);
//        return this.getOne(memberQuery);
        return null;
    }

    /**
     * 查询指定day的注册人数
     *
     * @param day
     * @return 注册人数
     */
    @Override
    public Integer countRegisterDay(String day) {
        return this.baseMapper.countRegisterDay(day);
    }
}

