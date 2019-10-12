package org.yui.tomcat.config;

import org.yui.base.bean.constant.StringConstant;
import org.yui.base.util.FileUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.valves.AccessLogValve;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
@Configuration
public class TomcatConfig {

    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer() {
        return (webServerFactory) -> {
            if (webServerFactory instanceof TomcatServletWebServerFactory) {
                TomcatServletWebServerFactory tomcatServletWebServerFactory =
                        (TomcatServletWebServerFactory)webServerFactory;
                tomcatServletWebServerFactory.setUriEncoding(StandardCharsets.UTF_8);

                Path path = Paths.get("access_log");
                if (FileUtil.createDirectoryQuietly(path)) {
                    tomcatServletWebServerFactory.setBaseDirectory(path.toFile());

                    AccessLogValve valve2 = new AccessLogValve();
                    valve2.setPattern("%h %l [%{yyyy-MM-dd HH:mm:ss.SSS}t] \"%r\" %s %b %D");
                    valve2.setSuffix(".log");
                    valve2.setRequestAttributesEnabled(true);
                    valve2.setDirectory(StringConstant.BLANK);
                    valve2.setBuffered(true);
                    valve2.setRotatable(true);
                    valve2.setEnabled(true);
                    valve2.setRenameOnRotate(false);
                    valve2.setPrefix("access_log");
                    valve2.setFileDateFormat(".yyyy-MM-dd");
                    tomcatServletWebServerFactory.addContextValves(valve2);
                }
            }
        };
    }
}
