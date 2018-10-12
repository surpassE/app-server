package com.dobe.appserver.service.impl;

import com.dd.plist.*;
import com.dobe.appserver.constants.Constants;
import com.dobe.appserver.dao.RepositoryService;
import com.dobe.appserver.model.AppInfo;
import com.dobe.appserver.service.ManageService;
import com.dobe.appserver.utils.SpecialCodeGenerateUtils;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Appinfo;
import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.ApkMeta;
import net.dongliu.apk.parser.bean.IconFace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.transform.Source;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * 分析app package
 *
 * @author zc.ding
 * @create 2018/10/11
 */
@Service
public class ManageServiceImpl implements ManageService {
    
    private static final Logger logger = LoggerFactory.getLogger(ManageServiceImpl.class);
    
    @Autowired
    @Qualifier("inIRepositoryServiceImpl")
    private RepositoryService repositoryService;
    
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
        //解析app安装包
        this.parseApp(appInfo, filePath);
        //持久化安装包
        if(this.repositoryService.saveAppInfo(appInfo) > 0){
            return Constants.SUCCESS;
        }
        return Constants.ERROR;
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
    *  @param appInfo   应用信息
    *  @param filePath  文件包路径
    *  @return com.dobe.appserver.model.AppInfo
    *  @date                    ：2018/10/11
    *  @author                  ：zc.ding@foxmail.com
    */
    private AppInfo parseApk(AppInfo appInfo, String filePath) throws Exception{
        ApkFile apkFile = new ApkFile(new File(filePath));
        ApkMeta apkMeta = apkFile.getApkMeta();
        appInfo.setLabel(apkMeta.getLabel());
        appInfo.setVersionCode(apkMeta.getVersionCode().toString());
        appInfo.setVersionName(apkMeta.getVersionName()); 
        appInfo.setPackageName(apkMeta.getPackageName());
        apkFile.getAllIcons().parallelStream().min(Comparator.comparingInt(o -> o.getData().length)).ifPresent(iconFace -> {
            appInfo.setIcon(Paths.get(filePath).getParent().toString() + File.separator + appInfo.getCode() + "_" + Paths.get(iconFace.getPath()).getFileName().toString());
            this.saveIcon(Paths.get(appInfo.getIcon()), iconFace.getData());
        });
        logger.info("{}", appInfo);
        return appInfo;
    }
    
    /**
    *  解析IOS安装包
    *  @param appInfo   应用信息
    *  @param filePath  文件包路径
    *  @return com.dobe.appserver.model.AppInfo
    *  @date                    ：2018/10/12
    *  @author                  ：zc.ding@foxmail.com
    */
    private AppInfo parseIpa(AppInfo appInfo, String filePath) throws Exception{
        List<Object> list = getIpaInputStream(filePath, ".app/Info.plist");
        if(list != null){
            NSDictionary rootDict = (NSDictionary) PropertyListParser.parse((InputStream)(list.get(0)));
            appInfo.setLabel(getInfo(rootDict, Collections.singletonList("CFBundleDisplayName")).toString());
            appInfo.setVersionCode(getInfo(rootDict, Collections.singletonList("CFBundleVersion")).toString());
            appInfo.setVersionName(getInfo(rootDict, Collections.singletonList("CFBundleShortVersionString")).toString());
            appInfo.setPackageName(getInfo(rootDict, Collections.singletonList("CFBundleIdentifier")).toString());
            Object obj = getInfo(rootDict, Arrays.asList("CFBundleIcons#", "CFBundlePrimaryIcon#", "CFBundleIconFiles"));
            String iconNameReg = null;
            if(obj instanceof NSArray){
                NSArray nsArray = (NSArray)obj;
                if(nsArray.count() > 0){
                    iconNameReg = nsArray.objectAtIndex(0).toJavaObject().toString();
                    if(!iconNameReg.endsWith(".png")){
                        iconNameReg = ".+" + iconNameReg + "@\\dx\\.png";
                    }
                }
            }
            Optional.ofNullable(iconNameReg).ifPresent(o -> {
                List<Object> iconList = getIpaInputStream(filePath, o);
                if(iconList != null){
                    appInfo.setIcon(Paths.get(filePath).getParent().toString() + File.separator + appInfo.getCode() + "_" + iconList.get(1));
                    this.saveIcon(Paths.get(appInfo.getIcon()), (InputStream)(iconList.get(0)));
                }
            });
        }
        logger.info("{}", appInfo);
        return appInfo;
    }
    
    /**
    *  获得压缩文件中指定文件的文件流
    *  @param filePath  压缩文件路径
    *  @param fileName  文件名称
    *  @return java.io.InputStream
    *  @date                    ：2018/10/12
    *  @author                  ：zc.ding@foxmail.com
    */
    private List<Object> getIpaInputStream(String filePath, String fileName){
        try (ZipInputStream zipIs = new ZipInputStream(new FileInputStream(filePath))){
            ZipEntry ze;
            while ((ze = zipIs.getNextEntry()) != null) {
                if (!ze.isDirectory()) {
                    String name = ze.getName();
                    // 读取 info.plist 文件,包里可能会有多个 info.plist 文件！！！
                    if (name.contains(fileName) || name.matches(fileName)) {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        int chunk;
                        byte[] data = new byte[2048];
                        while(-1 != (chunk = zipIs.read(data))) {
                            byteArrayOutputStream.write(data, 0, chunk);
                        }
                        return Arrays.asList(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), Paths.get(name).getFileName().toString());
                    }
                }
            }
        }catch (Exception e){
            logger.error("parse ipa fail.", e);
        }
        return null;
    }

    /**
    *  保存icon图片
    *  @param path 文件地址
    *  @param data   icon流或字节数组
    *  @date                    ：2018/10/12
    *  @author                  ：zc.ding@foxmail.com
    */
    private void saveIcon(Path path, Object data){
        try {
            byte[] buf;
            if(data instanceof InputStream){
                InputStream inputStream = (InputStream)data;
                buf = new byte[inputStream.available()];
                inputStream.read(buf);
            }else{
                buf = (byte[])data;
            }
            Files.createDirectories(path.getParent());
            File file = path.toFile();
            if(file.exists()){
                logger.info("delete exist file path: {}, state: {}", file.getAbsoluteFile(), file.delete());
            }
            Files.createFile(path);
            Files.write(path, buf);
        }catch (Exception e){
            logger.error("save app icon error.", e);
        }
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
    private static Object getInfo(NSDictionary nsDictionary, List<String> list){
        List<String> tmp = new ArrayList<>(list);
        if(tmp.size() > 0){
            String key = tmp.get(0);
            if(key.contains("#")){
                tmp.remove(0);
                return getInfo((NSDictionary)nsDictionary.get(key.replaceAll("#", "")), tmp);
            }else{
                return nsDictionary.get(key);
            }
        }
        return "";
    }

    public static void main(String[] args) throws Exception{
        ManageServiceImpl m = new ManageServiceImpl();
//        String filePath = "C:\\soft\\test\\gudiacidian.ipa";
        String filePath = "D:\\test\\app\\android\\CXJ_ANDROID_1.0.0.apk";
        m.parseApk(new AppInfo(), filePath);
//        String filePath = "C:\\soft\\test\\gudiacidian.ipa";
//        String filePath = "D:\\test\\app\\ios\\qiankundai.ipa";
//        m.parseIpa(new AppInfo(), filePath);
    }
    
    
    
    
}
