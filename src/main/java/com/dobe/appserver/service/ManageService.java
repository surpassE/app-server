package com.dobe.appserver.service;


import com.dobe.appserver.model.AppInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ManageService {
    
    /**
    *  app安装包上传
    *  @param file
    *  @return java.lang.String
    *  @date                    ：2018/10/14
    *  @author                  ：zc.ding@foxmail.com
    */
    String upload(MultipartFile file);
    
    /**
    *  条件检索配置信息
    *  @param appInfo
    *  @return java.util.List<com.dobe.appserver.model.AppInfo>
    *  @date                    ：2018/10/14
    *  @author                  ：zc.ding@foxmail.com
    */
    List<AppInfo> findAppInfoList(AppInfo appInfo);
    
    /**
    *  删除AppInfo配置信息
    *  @param code
    *  @return java.util.Integer
    *  @date                    ：2018/10/14
    *  @author                  ：zc.ding@foxmail.com
    */
    Integer delAppInfo(String code);
}
