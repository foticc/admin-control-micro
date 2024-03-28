package com.foticc.apigeteway.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@EnableConfigurationProperties(NacosValBean.class)
public class Schedule {

   private final NacosValBean nacosValBean;


    public Schedule(NacosValBean nacosValBean) {
        this.nacosValBean = nacosValBean;
    }


    public NacosValBean getNacosValBean() {
        return this.nacosValBean;
    }
}
