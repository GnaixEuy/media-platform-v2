package cn.GnaixEuy.gateway.filter;


import cn.GnaixEuy.commonutils.utils.JwtUtils;
import com.alibaba.nacos.common.packagescan.resource.AntPathMatcher;
import com.alibaba.nacos.shaded.com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

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
@Slf4j
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        if (antPathMatcher.match("/api/**/ucenter/member/login", path) || antPathMatcher.match("/api/**/ucenter/member/register", path)) {
            return chain.filter(exchange);
        }
        if (antPathMatcher.match("/api/**", path)) {
            List<String> tokenList = request.getHeaders().get("token");
            if (null == tokenList) {
                ServerHttpResponse response = exchange.getResponse();
                return out(response);
            } else {
                boolean isCheck = JwtUtils.checkToken(tokenList.get(0));
                if (!isCheck) {
                    ServerHttpResponse response = exchange.getResponse();
                    return out(response);
                }
            }
        }
        //内部服务接口，不允许外部访问
        if (antPathMatcher.match("/**/inner/**", path)) {
            ServerHttpResponse response = exchange.getResponse();
            return out(response);
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private Mono<Void> out(ServerHttpResponse response) {
        JsonObject message = new JsonObject();
        message.addProperty("success", false);
        message.addProperty("code", 28004);
        message.addProperty("data", "鉴权失败");
        byte[] bits = message.toString().getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }
}

