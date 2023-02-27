package cn.GnaixEuy.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * <img src="http://blog.gnaixeuy.cn/wp-content/uploads/2022/09/倒闭.png"/>
 *
 * <p>项目： media-v2 </p>
 * 创建日期： 2023/2/27
 *
 * @author GnaixEuy
 * @version 1.0.0
 * @see <a href="https://github.com/GnaixEuy"> GnaixEuy的GitHub </a>
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RegisterLoginBO {

    @NotBlank(message = "手机号不能为空")
    @Length(min = 11, max = 11, message = "手机长度不正确")
    private String mobile;
    @NotBlank(message = "验证码不能为空")
    private String smsCode;

}

