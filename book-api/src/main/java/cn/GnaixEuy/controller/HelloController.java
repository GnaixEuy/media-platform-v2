package cn.GnaixEuy.controller;

import cn.GnaixEuy.common.utils.SMSUtils;
import cn.GnaixEuy.common.utils.result.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
@Api(tags = "Hello 测试的接口")
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HelloController {
    private final SMSUtils smsUtils;

    @ApiOperation(value = "hello - 这是一个hello的测试路由")
    @GetMapping("hello")
    public Object hello() {
        return JSONResult.ok("Hello SpringBoot~");
    }


    @GetMapping("sms")
    public Object sms() throws Exception {
        String code = "123456";
        smsUtils.sendSMS("13365917711", code);
        return JSONResult.ok();
    }
}
