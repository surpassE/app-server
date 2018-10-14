package com.dobe.appserver.dao.impl;

import com.dobe.appserver.constants.Constants;
import com.dobe.appserver.dao.RepositoryService;
import com.dobe.appserver.model.AppInfo;
import com.sirding.singleton.IniTool;
import org.apache.tomcat.util.bcel.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

/**
 * appInfo信息持久层
 *
 * @author zc.ding
 * @create 2018/10/12
 */
@Repository("inIRepositoryServiceImpl")
public class InIRepositoryServiceImpl implements RepositoryService {
    
    private static Logger logger = LoggerFactory.getLogger(InIRepositoryServiceImpl.class);
    private static IniTool iniTool = IniTool.newInstance();
    private final static String DB_PATH = Constants.APP_PATH + File.separator + Constants.APP_DB_PATH;
    
    //创建配置保存路径
    static{
        File file = new File(DB_PATH);
        if(!file.exists()){
            try {
                Files.createDirectories(Paths.get(DB_PATH).getParent());
                logger.info("create db.ini config file. status : {}", file.createNewFile());
            }catch (Exception e){
                logger.error("create db.ini config fail.", e);
            }
        }
    }
    
    @Override
    public Integer saveAppInfo(AppInfo appInfo) {
        try {
            iniTool.saveSec(appInfo, DB_PATH);
        }catch (Exception e){
            logger.error("save appInfo config fail.", e);
            return 0;
        }
        return 1;
    }

    @Override
    public Integer delAppInfo(String code) {
        try{
            iniTool.remoteSec(code, DB_PATH);
        }catch (Exception e){
            logger.error("del appInfo config fail.", e);
            return 0;
        }
        return 1;
    }

    @Override
    public List<AppInfo> findAppInfoList() {
        try {
            return iniTool.loadSec(AppInfo.class, DB_PATH);
        }catch (Exception e){
            return Collections.emptyList();
        }
    }
}
