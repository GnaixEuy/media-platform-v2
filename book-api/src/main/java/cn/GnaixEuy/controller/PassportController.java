package cn.GnaixEuy.controller;

import cn.GnaixEuy.base.BaseInfoProperties;
import cn.GnaixEuy.bo.RegistLoginBO;
import cn.GnaixEuy.common.utils.IPUtil;
import cn.GnaixEuy.common.utils.SMSUtils;
import cn.GnaixEuy.common.utils.result.JSONResult;
import cn.GnaixEuy.common.utils.result.ResponseStatusEnum;
import cn.GnaixEuy.pojo.Users;
import cn.GnaixEuy.service.UserService;
import cn.GnaixEuy.vo.UsersVO;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

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
@Slf4j
@Api(tags = "PassportController 通信证接口模块")
@RequestMapping("passport")
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PassportController extends BaseInfoProperties {

    private final SMSUtils smsUtils;
    private final UserService userService;

    @PostMapping("getSMSCode")
    public JSONResult getSMSCode(@RequestParam String mobile,
                                 HttpServletRequest request) throws Exception {
        if (StringUtils.isBlank(mobile)) {
            return JSONResult.ok();
        }
        // 获得用户ip，
        String userIp = IPUtil.getRequestIp(request);
        // 根据用户ip进行限制，限制用户在60秒之内只能获得一次验证码
        redis.setnx60s(MOBILE_SMSCODE + ":" + userIp, userIp);
        String code = (int) ((Math.random() * 9 + 1) * 100000) + "";
        smsUtils.sendSMS(mobile, code);
        log.info(code);
        // 把验证码放入到redis中，用于后续的验证
        redis.set(MOBILE_SMSCODE + ":" + mobile, code, 30 * 60);
        return JSONResult.ok();
    }

    @PostMapping("login")
    public JSONResult login(@Valid @RequestBody RegistLoginBO registLoginBO,
                            HttpServletRequest request) throws Exception {
        String mobile = registLoginBO.getMobile();
        String code = registLoginBO.getSmsCode();
        // 1. 从redis中获得验证码进行校验是否匹配
        String redisCode = redis.get(MOBILE_SMSCODE + ":" + mobile);
        if (StringUtils.isBlank(redisCode) || !redisCode.equalsIgnoreCase(code)) {
            return JSONResult.errorCustom(ResponseStatusEnum.SMS_CODE_ERROR);
        }
        // 2. 查询数据库，判断用户是否存在
        Users user = userService.queryMobileIsExist(mobile);
        if (user == null) {
            // 2.1 如果用户为空，表示没有注册过，则为null，需要注册信息入库
            user = userService.createUser(mobile);
        }
        // 3. 如果不为空，可以继续下方业务，可以保存用户会话信息
        String uToken = UUID.randomUUID().toString();
        redis.set(REDIS_USER_TOKEN + ":" + user.getId(), uToken);
        // 4. 用户登录注册成功以后，删除redis中的短信验证码
        redis.del(MOBILE_SMSCODE + ":" + mobile);
        // 5. 返回用户信息，包含token令牌
        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(user, usersVO);
        usersVO.setUserToken(uToken);
        return JSONResult.ok(usersVO);
    }


    @PostMapping("logout")
    public JSONResult logout(@RequestParam String userId,
                             HttpServletRequest request) throws Exception {

        // 后端只需要清除用户的token信息即可，前端也需要清除，清除本地app中的用户信息和token会话信息
        redis.del(REDIS_USER_TOKEN + ":" + userId);

        return JSONResult.ok();
    }

}