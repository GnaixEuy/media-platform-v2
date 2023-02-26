package cn.GnaixEuy.ucenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * <img src="http://blog.gnaixeuy.cn/wp-content/uploads/2022/09/倒闭.png"/>
 *
 * <p>项目： ${PROJECT_NAME} </p>
 * 创建日期： ${DATE}
 *
 * @author GnaixEuy
 * @version 1.0.0
 * @see <a href="https://github.com/GnaixEuy"> GnaixEuy的GitHub </a>
 */
@SpringBootApplication(scanBasePackages = "cn.GnaixEuy")
@EnableDiscoveryClient
public class UcenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(UcenterApplication.class, args);
    }
}