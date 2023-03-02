package cn.GnaixEuy.vlog.client;

import cn.GnaixEuy.common.utils.JSONResult;
import cn.GnaixEuy.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
@FeignClient(value = "fans-server", configuration = {FeignConfig.class})
public interface FansFeignClient {

    @GetMapping(value = {"fans/feign/queryDoIFollowVloger/{userId}/{vlogerId}"})
    JSONResult queryDoIFollowVloger(@PathVariable(value = "userId") String userId,
                                    @PathVariable(value = "vlogerId") String vlogerId);

}
