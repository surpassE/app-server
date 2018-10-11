package com.dobe.appserver.service.impl;

import com.dd.plist.NSDictionary;
import com.dd.plist.NSString;
import com.dd.plist.PropertyListParser;
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
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

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

    /**
     *  读取配置文件中的key dict string
     *  @param nsDictionary  key或是dict节点
     *  @param list  key的集合 Arrays.asList("com.apple.iTunesStore.downloadInfo#", "accountInfo#", "AppleID") 
     *               表示读取com.apple.iTunesStore.downloadInfo下accountInfo下AppleID对应的String值
     *  @return java.lang.String
     *  @date                    ：2018/10/12
     *  @author                  ：zc.ding@foxmail.com
     */
    private static String getInfo(NSDictionary nsDictionary, List<String> list){
        List<String> tmp = new ArrayList<>(list);
        if(tmp.size() > 0){
            String key = tmp.get(0);
            if(key.contains("#")){
                tmp.remove(0);
                return getInfo((NSDictionary)nsDictionary.get(key.replaceAll("#", "")), tmp);
            }else{
                return nsDictionary.get(key).toString();
            }
        }
        return null;
    }

    public static void main(String[] args) throws Exception{
//        String filePath = "D:\\soft\\test\\app\\android\\CXJ_ANDROID_1.0.0.apk";
        String filePath = "C:\\soft\\test\\gudiacidian.ipa";
        String destFilePath = "C:\\soft\\test\\info.plist";
//        ApkFile apkFile = new ApkFile(new File(filePath));
//        ApkMeta apkMeta = apkFile.getApkMeta();
//        logger.info(apkMeta.toString());
//        logger.info("{}", apkMeta.getName());
//        logger.info("{}", apkMeta.getPlatformBuildVersionCode());

        File file = new File(filePath);

//        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(filePath));
//        ZipEntry zipEntry;
//        while((zipEntry = zipInputStream.getNextEntry()) != null){
//            if(!zipEntry.isDirectory() && zipEntry.getName().endsWith(".plist")){
////                zipEntry.
//            }
//        }

        ZipFile zipFile = new ZipFile(filePath);
        Enumeration<?> entryEnumeration = zipFile.entries();
        while(entryEnumeration.hasMoreElements()){
            ZipEntry zipEntry = (ZipEntry)entryEnumeration.nextElement();
            if(!zipEntry.isDirectory() && zipEntry.getName().endsWith(".plist") && !zipEntry.getName().contains("/")){
                try(InputStream inputStream = zipFile.getInputStream(zipEntry);
                    OutputStream os = new FileOutputStream(destFilePath)){
                    byte[] buf = new byte[2048];
                    int length;
                    while((length = inputStream.read(buf)) > 0){
                        os.write(buf, 0, length);
                    }
                }
            }
        }
        
        NSDictionary rootDict = (NSDictionary)PropertyListParser.parse(destFilePath);
//        NSString parameters = (NSString) rootDict.objectForKey("CFBundleIdentifier");
        NSString parameters = (NSString) rootDict.get("bundleVersion");
        NSDictionary nsDictionary = (NSDictionary)rootDict.get("com.apple.iTunesStore.downloadInfo");
        logger.info("{}", getInfo(rootDict, Arrays.asList("com.apple.iTunesStore.downloadInfo#", "accountInfo#", "AppleID")));
        
    }
    
    
    
    
}
