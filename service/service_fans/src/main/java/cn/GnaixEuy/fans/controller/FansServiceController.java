package cn.GnaixEuy.fans.controller;

import cn.GnaixEuy.common.utils.JSONResult;
import cn.GnaixEuy.fans.service.FansService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <img src="http://blog.gnaixeuy.cn/wp-content/uploads/2022/09/倒闭.png"/>
 *
 * <p>项目： media-v2 </p>
 * 创建日期： 2023/3/1
 *
 * @author GnaixEuy
 * @version 1.0.0
 * @see <a href="https://github.com/GnaixEuy"> GnaixEuy的GitHub </a>
 */
@Slf4j
@Api(tags = "FansController 粉丝相关业务Feign功能的接口")
@RequestMapping(value = {"fans/feign"})
@RestController
public class FansServiceController {

    @Autowired
    private FansService fansService;

    @GetMapping(value = {"queryDoIFollowVloger/{userId}/{vlogerId}"})
    public JSONResult queryDoIFollowVloger(@PathVariable String userId, @PathVariable String vlogerId) {
        if (fansService.queryDoIFollowVloger(userId, vlogerId)) {
            return JSONResult.ok("true");
        } else {
            return JSONResult.ok("false");
        }
    }


}
