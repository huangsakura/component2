package com.diligroup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author huangjinlong
 * @time 2019-10-11 19:42
 * @description
 */
@SpringBootApplication(scanBasePackages = {"org.yui","com.diligroup"})
public class DiliApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiliApplication.class,args);
    }
}
