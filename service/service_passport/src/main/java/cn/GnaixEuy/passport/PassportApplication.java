package cn.GnaixEuy.passport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

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
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"cn.GnaixEuy"})
public class PassportApplication {
    public static void main(String[] args) {
        SpringApplication.run(PassportApplication.class, args);
    }

}
