package com.dobe.appserver.dao;

import com.dobe.appserver.model.AppInfo;

import java.util.List;

/**
*  appInfo持久层，如需扩展实现接口
*  @date                    ：2018/8/10
*  @author                  ：zc.ding@foxmail.com
*/
public interface RepositoryService {
    
    /**
    *  添加AppInfo配置
    *  @param appInfo
    *  @return java.lang.Integer
    *  @date                    ：2018/10/12
    *  @author                  ：zc.ding@foxmail.com
    */
    Integer saveAppInfo(AppInfo appInfo);
    
    /**
    *  删除AppInfo配置
    *  @param code
    *  @return java.lang.Integer
    *  @date                    ：2018/10/12
    *  @author                  ：zc.ding@foxmail.com
    */
    Integer delAppInfo(String code);
    
    /**
    *  查询所有AppInfo配置
    *  @return java.util.List<com.dobe.appserver.model.AppInfo>
    *  @date                    ：2018/10/12
    *  @author                  ：zc.ding@foxmail.com
    */
    List<AppInfo> findAppInfoList();
}
