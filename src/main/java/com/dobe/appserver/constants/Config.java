package com.dobe.appserver.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * TODO
 *
 * @author zc.ding
 * @create 2018/10/17
 */
@Component
public class Config {
    @Value("${base.path}")
    private String basePath;

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }
}
