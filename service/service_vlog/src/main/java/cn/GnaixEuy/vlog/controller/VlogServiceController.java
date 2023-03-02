package cn.GnaixEuy.vlog.controller;

import cn.GnaixEuy.common.utils.JSONResult;
import cn.GnaixEuy.model.pojo.Vlog;
import cn.GnaixEuy.vlog.service.VlogService;
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
@Api(tags = "VlogController 短视频相关业务功能的接口")
@RestController
@RequestMapping(value = {"vlog/feign"})
public class VlogServiceController {

    @Autowired
    private VlogService vlogService;

    @GetMapping(value = {"/getVlog/{id}"})
    public JSONResult getVlog(@PathVariable String id) {
        Vlog vlog = this.vlogService.getVlog(id);
        return JSONResult.ok(vlog);
    }

}
