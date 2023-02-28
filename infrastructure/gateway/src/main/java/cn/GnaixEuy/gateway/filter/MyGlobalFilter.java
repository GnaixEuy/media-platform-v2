package cn.GnaixEuy.gateway.filter;

import cn.GnaixEuy.common.enmus.ResponseStatusEnum;
import cn.GnaixEuy.common.exceptions.GraceException;
import cn.GnaixEuy.common.utils.JSONResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

import static cn.GnaixEuy.properties.BaseInfoProperties.REDIS_USER_TOKEN;


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
@Configuration
public class MyGlobalFilter implements GlobalFilter, Ordered {
    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        URI uri = exchange.getRequest().getURI();
        if (uri.toString().contains("/api/v2/passport")) {
            return chain.filter(exchange);
        }
        String headerUserId = exchange.getRequest().getHeaders().get("headerUserId").get(0);
        String headerUserToken = exchange.getRequest().getHeaders().get("headerUserToken").get(0);
        // 判断header中用户id和token不能为空
        if (StringUtils.isNotBlank(headerUserId) && StringUtils.isNotBlank(headerUserToken)) {
            String redisToken = this.redisTemplate.opsForValue().get(REDIS_USER_TOKEN + ":" + headerUserId);
            if (StringUtils.isBlank(redisToken)) {
                try {
                    return this.invalidTokenMono(exchange);
                } catch (JSONException e) {
                    GraceException.display(ResponseStatusEnum.UN_LOGIN);
                }
            } else {
                // 比较token是否一致，如果不一致，表示用户在别的手机端登录
                if (!redisToken.equalsIgnoreCase(headerUserToken)) {
                    try {
                        return this.invalidTokenMono(exchange);
                    } catch (JSONException e) {
                        GraceException.display(ResponseStatusEnum.TICKET_INVALID);
                    }
                }
            }
        } else {
            try {
                return this.invalidTokenMono(exchange);
            } catch (JSONException e) {
                GraceException.display(ResponseStatusEnum.UN_LOGIN);
            }
        }
        return chain.filter(exchange);
    }

    /**
     * 顺序,数值越小,优先级越高
     *
     * @return
     */
    @Override
    public int getOrder() {
        return 1;
    }

    /**
     * 设置向后传递的header
     *
     * @param chain
     * @param exchange
     * @param headerMap
     */
    private Mono<Void> chainFilterAndSetHeaders(GatewayFilterChain chain, ServerWebExchange exchange, LinkedHashMap<String, String> headerMap) {
        // 添加header
        Consumer<HttpHeaders> httpHeaders = httpHeader -> {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                // 遍历Map设置header，向后传递
                httpHeader.set(entry.getKey(), entry.getValue());
            }
        };
        ServerHttpRequest newRequest = exchange.getRequest().mutate().headers(httpHeaders).build();
        ServerWebExchange build = exchange.mutate().request(newRequest).build();
        //将现在的request 变成 exchange对象
        return chain.filter(build);
    }

    /**
     * 无效的token
     */
    private Mono<Void> invalidTokenMono(ServerWebExchange exchange) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("status", JSONResult.errorTicket().getStatus());
        json.put("msg", JSONResult.errorTicket().getMsg());
        json.put("success", JSONResult.errorTicket().getSuccess());
        return buildReturnMono(json, exchange);
    }

    private Mono<Void> buildReturnMono(JSONObject json, ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        byte[] bits = json.toString().getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "text/plain;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }
}

