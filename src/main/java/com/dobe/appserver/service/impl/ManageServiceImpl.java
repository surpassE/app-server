package com.dobe.appserver.service.impl;

import com.dobe.appserver.constants.Constants;
import com.dobe.appserver.model.AppInfo;
import com.dobe.appserver.service.ManageService;
import com.dobe.appserver.utils.SpecialCodeGenerateUtils;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Appinfo;
import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.ApkMeta;
import net.dongliu.apk.parser.bean.IconFace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.util.Comparator;
import java.util.Optional;

/**
 * TODO
 *
 * @author zc.ding
 * @create 2018/10/11
 */
@Service
public class ManageServiceImpl implements ManageService {
    
    private static final Logger logger = LoggerFactory.getLogger(ManageServiceImpl.class);
    
    @Override
    public String upload(MultipartFile file) {
        String code = SpecialCodeGenerateUtils.getSpecialNumCode();
        AppInfo appInfo = new AppInfo();
        String fileName = Optional.ofNullable(file.getOriginalFilename()).orElse("").toLowerCase();
        appInfo.setCode(code);
        appInfo.setFileName(fileName);
        appInfo.setFileSize(file.getSize());
        appInfo.setAppType(fileName.endsWith(Constants.APP_SUFFIX_ANDROID) ? Constants.APP_TYPE_ANDROID : Constants.APP_TYPE_IOS);
        appInfo.setSuffix(fileName.endsWith(Constants.APP_SUFFIX_ANDROID) ? Constants.APP_SUFFIX_ANDROID : Constants.APP_SUFFIX_IOS);
        //本地存储路径
        String filePath = fileName.endsWith(Constants.APP_SUFFIX_ANDROID) ? Constants.APP_PATH_ANDROID : Constants.APP_PATH_IOS + code + appInfo.getSuffix();
        logger.info("save file path: {}", filePath);
        //存储文件
        try(InputStream is = file.getInputStream();
            OutputStream os = new FileOutputStream(filePath)){
            byte[] buf = new byte[2048];
            int length;
            while((length = is.read()) > 0){
                os.write(buf, 0, length);
            }
        }catch (Exception e){
            logger.error("save upload file error. fileName: {}", appInfo.getFileName(), e);
            return Constants.ERROR;
        }
        this.parseApp(appInfo, filePath);
        return Constants.SUCCESS;
    }
    
    /**
    *  解析app
    *  @param appInfo   app包信息
    *  @param filePath  存储本地的文件绝对路径
    *  @return com.dobe.appserver.model.AppInfo
    *  @date                    ：2018/10/11
    *  @author                  ：zc.ding@foxmail.com
    */
    private AppInfo parseApp(AppInfo appInfo, String filePath){
        try{
            if(appInfo.getSuffix().equals(Constants.APP_SUFFIX_ANDROID)){
                return parseApk(appInfo, filePath);
            }else{
                return parseIpa(appInfo, filePath);
            }
        }catch (Exception e){
            logger.error("parse app fail.", e);
        }
        return appInfo;
    }
    
    /**
    *  解析android安装包
    *  @param appInfo
    *  @param filePath
    *  @return com.dobe.appserver.model.AppInfo
    *  @date                    ：2018/10/11
    *  @author                  ：zc.ding@foxmail.com
    */
    private AppInfo parseApk(AppInfo appInfo, String filePath) throws Exception{
        ApkFile apkFile = new ApkFile(new File(filePath));
        ApkMeta apkMeta = apkFile.getApkMeta();
        appInfo.setLabel(apkMeta.getLabel());
        appInfo.setVersionCode(apkMeta.getVersionCode());
        appInfo.setVersionName(apkMeta.getVersionName()); 
        appInfo.setPackageName(apkMeta.getPackageName());
        apkFile.getAllIcons().parallelStream().min(Comparator.comparingInt(o -> o.getData().length)).ifPresent(iconFace -> {
            appInfo.setIcon(Paths.get(filePath).getParent().toString() + File.separator + appInfo.getCode() + "_" + Paths.get(iconFace.getPath()).getFileName().toString());
            this.saveIcon(Paths.get(appInfo.getIcon()), iconFace.getData());
        });
        return appInfo;
    }
    
    private AppInfo parseIpa(AppInfo appInfo, String filePath){
        
        return appInfo;
    }

    private String saveIcon(Path path, byte[] buf){
        try {
            Files.createDirectories(path.getParent());
            File file = path.toFile();
            if(file.exists()){
                logger.info("delete exist file path: {}, state: {}", file.getAbsoluteFile(), file.delete());
            }
            Files.createFile(path);
            Files.write(path, buf);
        }catch (Exception e){
            logger.error("save app icon error.", e);
            return Constants.ERROR;
        }
        return path.toFile().getAbsolutePath();
    }

    public static void main(String[] args) throws Exception{
        String filePath = "D:\\test\\app\\android\\CXJ_ANDROID_1.0.0.apk";
        ApkFile apkFile = new ApkFile(new File(filePath));
        ApkMeta apkMeta = apkFile.getApkMeta();
//        System.out.println(apkMeta.getLabel());
//        System.out.println(apkMeta.getPackageName());
//        System.out.println(apkMeta.getVersionCode());
//        System.out.println(apkMeta.getVersionName());
//       
//        logger.info(apkMeta.getPlatformBuildVersionCode());
//        
//        logger.info(apkMeta.getPlatformBuildVersionName());
        logger.info(apkMeta.toString());
        logger.info("{}", apkMeta.getName());
        logger.info("{}", apkMeta.getPlatformBuildVersionCode());
        
       
   
    }
    
    
}
