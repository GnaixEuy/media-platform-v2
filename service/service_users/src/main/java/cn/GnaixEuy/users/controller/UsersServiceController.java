package cn.GnaixEuy.users.controller;

import cn.GnaixEuy.common.utils.JSONResult;
import cn.GnaixEuy.users.service.UserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@Slf4j
@Api(tags = "UsersServiceController 用户信息业务接口模块")
@RequestMapping(value = {"userInfo/feign"})
@RestController
public class UsersServiceController {

    @Autowired
    private UserService userService;

    @ResponseBody
    @GetMapping(value = {"getUserBase/{userId}"})
    public JSONResult getUserBaseInfoById(@PathVariable String userId) {
        return JSONResult.ok(this.userService.getUser(userId));
    }

}
