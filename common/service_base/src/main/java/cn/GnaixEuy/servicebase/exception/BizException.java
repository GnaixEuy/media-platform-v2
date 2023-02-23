package cn.GnaixEuy.servicebase.exception;

/**
 * <img src="http://blog.gnaixeuy.cn/wp-content/uploads/2022/09/倒闭.png"/>
 *
 * <p>项目： media-v2 </p>
 * 创建日期： 2023/2/23
 *
 * @author GnaixEuy
 * @version 1.0.0
 * @see <a href="https://github.com/GnaixEuy"> GnaixEuy的GitHub </a>
 */
public class BizException extends RuntimeException {

    private final Integer code;

    public BizException(ExceptionType exceptionType) {
        super(exceptionType.getMessage());
        this.code = exceptionType.getCode();
    }

    public Integer getCode() {
        return code;
    }
}
