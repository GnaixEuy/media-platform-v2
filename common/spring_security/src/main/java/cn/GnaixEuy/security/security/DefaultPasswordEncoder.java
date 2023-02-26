package cn.GnaixEuy.security.security;

import cn.GnaixEuy.commonutils.utils.MD5;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * <img src="http://blog.gnaixeuy.cn/wp-content/uploads/2022/09/倒闭.png"/>
 *
 * <p>项目： media-v2 </p>
 * 创建日期： 2023/2/26
 *
 * @author GnaixEuy
 * @version 1.0.0
 * @see <a href="https://github.com/GnaixEuy"> GnaixEuy的GitHub </a>
 */
@Component
public class DefaultPasswordEncoder implements PasswordEncoder {

    public DefaultPasswordEncoder() {
        this(-1);
    }

    /**
     * @param strength the log rounds to use, between 4 and 31
     */
    public DefaultPasswordEncoder(int strength) {

    }

    @Override
    public String encode(CharSequence rawPassword) {
        return MD5.encrypt(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.equals(MD5.encrypt(rawPassword.toString()));
    }
}
