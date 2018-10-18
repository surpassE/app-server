package com.dobe.appserver.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public interface UrlUtils {
    
    static String getIpPort(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getScheme()+ "://" + request.getServerName() + ":"+request.getServerPort() + request.getContextPath()+"/";
    }
}
