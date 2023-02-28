package cn.GnaixEuy.passport.intercepter;

import cn.GnaixEuy.common.enmus.ResponseStatusEnum;
import cn.GnaixEuy.common.exceptions.GraceException;
import cn.GnaixEuy.common.utils.IPUtil;
import cn.GnaixEuy.properties.BaseInfoProperties;
import cn.GnaixEuy.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
public class PassportInterceptor extends BaseInfoProperties implements HandlerInterceptor {
    @Autowired
    public RedisUtils redis;

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request,
                             @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        // 获得用户的ip
        String userIp = IPUtil.getRequestIp(request);
        System.out.println(request);
        // 得到是否存在的判断
        boolean keyIsExist = redis.keyIsExist(MOBILE_SMS_CODE + ":" + userIp);
        if (keyIsExist) {
            GraceException.display(ResponseStatusEnum.SMS_NEED_WAIT_ERROR);
            log.info("短信发送频率太大！");
            return false;
        }
        /*
          true: 请求放行
          false: 请求拦截
         */
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    }
}

