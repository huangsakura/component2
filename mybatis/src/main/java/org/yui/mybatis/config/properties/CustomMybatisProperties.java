package org.yui.mybatis.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author huangjinlong
 * @time 2019-03-05 15:50
 * @description
 */
@Validated
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "yunhua.component.ds")
public class CustomMybatisProperties {

    /**
     * 连接池初始化大小
     */
    @NotNull
    private Integer initSize = 5;

    /**
     * 最大活跃连接数
     */
    @NotNull
    private Integer maxActive = 200;

    /**
     * 最小连接池数量
     */
    @NotNull
    private Integer minIdle = 10;

    /**
     * 获取连接时最大等待时间，单位毫秒。配置了maxWait之后，缺省启用公平锁，并
     * 发效率会有所下降，如果需要可以通过配置useUnfairLock属性为true使用非公平锁。
     */
    @NotNull
    private Long maxWait = 10000L;

    /**
     * 是否开启连接泄漏检测
     *
     * 参考 https://github.com/alibaba/druid/wiki/%E8%BF%9E%E6%8E%A5%E6%B3%84%E6%BC%8F%E7%9B%91%E6%B5%8B
     */
    @NotNull
    private Boolean leakDetection = Boolean.FALSE;


    /**
     * 这里处理这么一种情况。
     *
     * 假设有一个枚举，其中 STUDENT的二进制值是 1，TEACHER的二进制是2，STAFF的二进制值是4
     * 则 如果想保存 STUDENT，则数据库的值是1；
     * 如果想保存 TEACHER，则数据库的值是2，
     * 如果想保存 STAFF，则数据库的值是4，
     * 如果想保存 STUDENT + TEACHER，则数据库的值是3，
     * 如果想保存 STUDENT + STAFF，则.....是5，
     * ....保存 TEACHER + STAFF，则....是6
     * .....保存 STUDENT + TEACHER + STAFF，则....是7。
     *
     *
     * 如果业务代码存在上面说的这种情况，其 实体类的字段的类型所在的包就应该配置在这个参数里面
     */
    private List<String> binaryClassPackages;

    /**
     * sql的执行时长大于此时间时，就是慢sql
     *
     * 单位秒
     */
    @NotNull
    private Integer slowSqlLimitSecond = 1;
}
