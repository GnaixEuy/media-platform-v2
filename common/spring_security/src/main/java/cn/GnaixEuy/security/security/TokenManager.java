package cn.GnaixEuy.security.security;

import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

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
public class TokenManager {

    private final long tokenExpiration = 24 * 60 * 60 * 1000;
    private final String tokenSignKey = "GnaixEuy";

    public String createToken(String username) {
        return Jwts.builder().setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(SignatureAlgorithm.HS512, tokenSignKey).compressWith(CompressionCodecs.GZIP).compact();
    }

    public String getUserFromToken(String token) {
        return Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token).getBody().getSubject();
    }

    public void removeToken(String token) {
        //jwttoken无需删除，客户端扔掉即可。
    }

}

