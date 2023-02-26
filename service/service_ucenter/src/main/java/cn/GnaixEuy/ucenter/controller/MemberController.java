package cn.GnaixEuy.ucenter.controller;

import cn.GnaixEuy.commonutils.entity.MemberOrderVo;
import cn.GnaixEuy.commonutils.utils.JwtUtils;
import cn.GnaixEuy.commonutils.vo.ResultVo;
import cn.GnaixEuy.ucenter.entity.Member;
import cn.GnaixEuy.ucenter.model.form.MemberForm;
import cn.GnaixEuy.ucenter.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
@Api(tags = "用户管理 ")
@RestController
@RequestMapping("/ucenter/member")
@CrossOrigin
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }


    /**
     * 用户登录
     */

    @PostMapping("/login")
    @ApiOperation("会员登录")
    public ResultVo login(@RequestBody MemberForm memberForm) {
        //memberForm 对象封装手机号和密码
        //返回token 值,使用jwt生成
        String token = memberService.login(memberForm);

        return ResultVo.ok().data("token", token);
    }

    /**
     * 用户注册
     */

    @PostMapping("/register")
    @ApiOperation("会员注册")
    public ResultVo register(@RequestBody MemberForm memberForm) {
        memberService.register(memberForm);
        return ResultVo.ok();
    }

    /**
     * 获取会员信息
     */
    @ApiOperation("获取会员信息")
    @GetMapping("/getMemberInfo")
    public ResultVo getMemberInfo(HttpServletRequest request) {
        String token = request.getHeader("token");
        //调用jwt工具类的方法，根据Request对象获取头信息，返回用户id
        String memberId = JwtUtils.getMemberIdByJwtToken(token);
        //根据数据库根据用户id获取用户信息
        Member member = memberService.getById(memberId);
        return ResultVo.ok().data("member", member);
    }

    @ApiOperation("根据会员id获取用户信息")
    @GetMapping("/getMemberInfoOrder/{id}")
    public MemberOrderVo getMemberInfoOrderById(@PathVariable String id) {
        Member member = memberService.getById(id);
        MemberOrderVo memberOrderVo = new MemberOrderVo();
        BeanUtils.copyProperties(member, memberOrderVo);
        return memberOrderVo;
    }

    @GetMapping("/countRegister/{day}")
    public Integer countRegister(@PathVariable String day) {
        return memberService.countRegisterDay(day);
    }
}