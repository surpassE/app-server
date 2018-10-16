package com.dobe.appserver.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;

/**
 * 文件操作工具类
 *
 * @author zc.ding
 * @create 2018/10/15
 */
public interface FileUtils {

    Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
    *  获取文件的md5值
    *  @param filePath
    *  @return java.lang.String
    *  @date                    ：2018/10/15
    *  @author                  ：zc.ding@foxmail.com
    */
    static String getFileMD5(String filePath) {
        File file = new File(filePath);
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest;
        byte[] buffer = new byte[1024];
        int len;
        try (FileInputStream in = new FileInputStream(file)){
            digest = MessageDigest.getInstance("MD5");
            while ((len = in.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }
        } catch (Exception e) {
            logger.error("get file md5 fail.", e);
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }

    /**
     *  获取文件的md5值
     *  @param inputStream
     *  @return java.lang.String
     *  @date                    ：2018/10/15
     *  @author                  ：zc.ding@foxmail.com
     */
    static String getFileMD5(InputStream inputStream) {
        MessageDigest digest;
        byte[] buffer = new byte[1024];
        int len;
        try (InputStream is = inputStream){
            digest = MessageDigest.getInstance("MD5");
            while ((len = is.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }
        } catch (Exception e) {
            logger.error("get file md5 fail.", e);
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }
    
    /**
    *  存储文件
    *  @param is
    *  @param filePath
    *  @return boolean
    *  @date                    ：2018/10/15
    *  @author                  ：zc.ding@foxmail.com
    */
    static boolean saveFile(InputStream is, String filePath){
        try {
            File path = new File(filePath);
            if(!path.getParentFile().exists()){
                Files.createDirectories(Paths.get(path.getParent()));
            }
        }catch (Exception e){
            
        }
        //存储文件
        try(InputStream isTmp = is;
            OutputStream os = new FileOutputStream(filePath)){
            byte[] buf = new byte[2048];
            int length;
            while((length = isTmp.read(buf)) > 0){
                os.write(buf, 0, length);
            }
            os.flush();
        }catch (Exception e){
            logger.error("save upload file error. fileName: {}", filePath, e);
            return false;
        }
        return true;
    }

    /**
     *  保存数据到文件
     *  @param path 文件地址
     *  @param data   icon流或字节数组
     *  @date                    ：2018/10/12
     *  @author                  ：zc.ding@foxmail.com
     */
    static void saveFile(Path path, Object data){
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
}
