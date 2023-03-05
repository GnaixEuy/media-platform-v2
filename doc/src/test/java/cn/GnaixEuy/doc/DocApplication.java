package cn.GnaixEuy.doc;

import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * <img src="http://blog.gnaixeuy.cn/wp-content/uploads/2022/09/倒闭.png"/>
 *
 * <p>项目： media-v2 </p>
 * 创建日期： 2023/3/5
 *
 * @author GnaixEuy
 * @version 1.0.0
 * @see <a href="https://github.com/GnaixEuy"> GnaixEuy的GitHub </a>
 */
@SpringBootTest
public class DocApplication {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 配置想要生成的表+ 配置想要忽略的表
     */
    public static ProcessConfig getProcessConfig() {
        // 指定生成逻辑、当存在指定表、指定表前缀、指定表后缀时，将生成指定表，其余表不生成、并跳过忽略表配置
        return ProcessConfig.builder().build();
    }

    /**
     * 数据库文档生成
     */
    @Test
    public void documentGeneration() throws SQLException {
//        DataSource dataSource = applicationContext.getBean(DruidDataSource.class);
        //数据源
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.setJdbcUrl("jdbc:mysql://localhost:3306/media-v2");
        hikariConfig.setUsername("root");
        hikariConfig.setPassword("123456");
        //设置可以获取tables remarks信息
        hikariConfig.addDataSourceProperty("useInformationSchema", "true");
        hikariConfig.setMinimumIdle(2);
        hikariConfig.setMaximumPoolSize(5);
        DataSource dataSource = new HikariDataSource(hikariConfig);
        String outPath = System.getProperty("user.dir").replaceAll("\\\\", "/");
        outPath += "/doc";
        // 生成文件配置 创建screw的引擎配置
        EngineConfig engineConfig = EngineConfig.builder()
                // 生成文件路径
                .fileOutputDir(outPath)
                // 打开目录
                .openOutputDir(true)
                // 文件类型 HTML->HTML文件  WORD->WORD文件  MD->Markdown文件
                .fileType(EngineFileType.WORD)
                // 生成模板实现
                .produceType(EngineTemplateType.freemarker)
                // 自定义文件名称，即生成的数据库文档名称
                .fileName("媒体平台系统数据库文档").build();
        // 生成文档配置（包含以下自定义版本号、描述等配置连接）
        Configuration config = Configuration.builder()
                // 版本
                .version("1.0.0")
                // 描述
                .description("媒体平台系统数据库设计")
                // 数据源
                .dataSource(dataSource)
                // 生成配置
                .engineConfig(engineConfig)
                // 生成配置
                .produceConfig(getProcessConfig())
                .build();
        // 执行screw，生成数据库文档
        new DocumentationExecute(config).execute();
    }

}

