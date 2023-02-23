package cn.GnaixEuy.commonutils.utils;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

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
@Data
public class ResultVo {
    @ApiModelProperty(value = "是否成功")
    private Boolean success;

    @ApiModelProperty(value = "返回码")
    private Integer code;

    @ApiModelProperty(value = "返回消息")
    private String message;

    @ApiModelProperty(value = "返回数据")
    private Map<String, Object> data = new HashMap<>();

    //构造方法私有
    private ResultVo() {
    }

    //成功静态方法
    public static ResultVo ok() {
        ResultVo resultVo = new ResultVo();
        resultVo.setSuccess(true);
        resultVo.setCode(ResultCode.SUCCESS);
        resultVo.setMessage("成功");
        return resultVo;
    }

    //失败静态方法
    public static ResultVo error() {
        ResultVo resultVo = new ResultVo();
        resultVo.setSuccess(false);
        resultVo.setCode(ResultCode.ERROR);
        resultVo.setMessage("失败");
        return resultVo;
    }

    public ResultVo success(Boolean success) {
        this.setSuccess(success);
        return this;
    }

    public ResultVo message(String message) {
        this.setMessage(message);
        return this;
    }

    public ResultVo code(Integer code) {
        this.setCode(code);
        return this;
    }

    public ResultVo data(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    public ResultVo data(Map<String, Object> map) {
        this.setData(map);
        return this;
    }

}

