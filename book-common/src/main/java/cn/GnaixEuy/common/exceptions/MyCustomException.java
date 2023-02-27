package cn.GnaixEuy.common.exceptions;

import cn.GnaixEuy.common.utils.result.ResponseStatusEnum;
import lombok.Data;

/**
 * <img src="http://blog.gnaixeuy.cn/wp-content/uploads/2022/09/倒闭.png"/>
 * 自定义异常
 * 目的：统一处理异常信息
 * 便于解耦，拦截器、service与controller 异常错误的解耦，
 * 不会被service返回的类型而限制
 * <p>项目： media-v2-new </p>
 * 创建日期： 2023/2/27
 *
 * @author GnaixEuy
 * @version 1.0.0
 * @see <a href="https://github.com/GnaixEuy"> GnaixEuy的GitHub </a>
 */
@Data
public class MyCustomException extends RuntimeException {

    private ResponseStatusEnum responseStatusEnum;

    public MyCustomException(ResponseStatusEnum responseStatusEnum) {
        super("异常状态码为：" + responseStatusEnum.status()
                + "；具体异常信息为：" + responseStatusEnum.msg());
        this.responseStatusEnum = responseStatusEnum;
    }

}
