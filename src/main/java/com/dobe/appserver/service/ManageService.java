package com.dobe.appserver.service;


import org.springframework.web.multipart.MultipartFile;

public interface ManageService {
    
    /**
    *  app安装包上传
    *  @param file
    *  @return java.lang.String
    *  @date                    ：2018/10/11
    *  @author                  ：zc.ding@foxmail.com
    */
    String upload(MultipartFile file);
}
