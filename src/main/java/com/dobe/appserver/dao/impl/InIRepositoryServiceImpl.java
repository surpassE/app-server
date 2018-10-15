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
import org.springframework.util.StringUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

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

    private ReadWriteLock lock = new ReentrantReadWriteLock();
    
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
            lock.writeLock().lock();
            iniTool.saveSec(appInfo, DB_PATH);
        }catch (Exception e){
            logger.error("save appInfo config fail.", e);
            return 0;
        }finally {
            lock.writeLock().unlock();
        }
        return 1;
    }

    @Override
    public Integer delAppInfo(String code) {
        try{
            lock.writeLock().lock();
            iniTool.remoteSec(code, DB_PATH);
        }catch (Exception e){
            logger.error("del appInfo config fail.", e);
            return 0;
        }finally {
            lock.writeLock().unlock();
        }
        return 1;
    }

    @Override
    public List<AppInfo> findAppInfoList(AppInfo appInfo) {
        try {
            lock.writeLock().lock();
            List<AppInfo> list = iniTool.loadSec(AppInfo.class, DB_PATH);
//            lock.writeLock().unlock();
            if (list != null) {
                List<AppInfo> result =
                        list.stream().filter(o -> appInfo.getAppType().equals(o.getAppType()) && appInfo.getSysType().equals(o.getSysType()) && appInfo.getSysType().equals(o.getSysType())).filter(a -> {
                            if (!StringUtils.isEmpty(appInfo.getSearch())) {
                                return a.getLabel().toUpperCase().contains(appInfo.getLabel().toUpperCase().trim());
                            }
                            return true;
                        }).sorted(Comparator.comparing(AppInfo::getTime)).collect(Collectors.toList());
                Integer total = result.size();
                if (total < appInfo.getStart()) {
                    return Collections.emptyList();
                }
                Integer maxRows = total - appInfo.getStart();
                return result.subList(appInfo.getStart(), maxRows > appInfo.getRows() ? appInfo.getRows() : maxRows);
            }
            return Collections.emptyList();
        }catch (Exception e){
            return Collections.emptyList();
        }finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean existMd5(String md5) {
        try {
            lock.readLock().lock();
            List<AppInfo> list = iniTool.loadSec(AppInfo.class, DB_PATH);
            return list.stream().anyMatch(o -> o.getMd5().equals(md5));
        }catch (Exception e){
            logger.error("find exist md5 fail.", e);
        }finally {
            lock.readLock().unlock();
        }
        return false;
    }

    @Override
    public AppInfo findAppInfoByCode(String code) {
        try {
            lock.readLock().lock();
            List<AppInfo> list = iniTool.loadSec(AppInfo.class, DB_PATH);
            return list.stream().filter(o -> o.getCode().equals(code)).findFirst().orElse(null);
        }catch (Exception e){
            logger.error("find exist md5 fail.", e);
        }finally {
            lock.readLock().unlock();
        }
        return null;
    }
}
