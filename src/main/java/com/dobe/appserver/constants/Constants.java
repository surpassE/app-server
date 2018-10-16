package com.dobe.appserver.constants;

import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * 常量
 *
 * @author zc.ding
 * @create 2018/10/11
 */
public class Constants {
    
    /**
    * 成功
    */
    public final static String SUCCESS = "SUCCESS";
    /**
    * 失败
    */
    public final static String ERROR = "ERROR";
    /**
    * 查询所有
    */
    public final static int ALL = 999;
    
    /**
     * D:/test/app  C:/soft/test/app
     */
    public static String APP_PATH;

    static{
        try {
            APP_PATH = new File(ResourceUtils.getURL("classpath:").getPath()).getAbsolutePath() + File.separator + "static/apps" + File.separator;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
    * 数据存储位置
    */
    public final static String APP_DB_PATH = "db.ini";
    
    /**
    * 图标后缀
    */
    public final static String APP_ICON_SUFFIX = ".png";

    /**
     * 二维码图片后缀
     */
    public final static String APP_QR_CODE_SUFFIX = ".jpg";

    /**
    * android后缀
    */
    public final static String APP_SUFFIX_ANDROID = ".apk";
    /**
    * ios后缀
    */
    public final static String APP_SUFFIX_IOS = ".ipa";
    /**
    * 默认app图标
    */
    public final static String APP_ICON = "default.png";
    /**
    * android app存储路径
    */
    public final static String APP_PATH_ANDROID = APP_PATH + "/android/";
    /**
    * iso存储路径
    */
    public final static String APP_PATH_IOS = APP_PATH + "/ios/";
    /**
    * app安装包类型 ios
    */
    public final static int APP_TYPE_IOS = 1;
    /**
    * app安装包类型 android
    */
    public final static int APP_TYPE_ANDROID = 2;
    /**
    * 开发环境测试版本
    */
    public final static int APP_ENV_TYPE_DEV = 1;
    /**
    * 预环境内测版本
    */
    public final static int APP_ENV_TYPE_BETA = 2;
    /**
    * 生产环境版本
    */
    public final static int APP_ENV_TYPE_RELEASED = 3;
    
    /**
    * app类型 乾坤袋(旧版鸿坤金服)
    */
    public final static int APP_SYS_TYPE_QKD = 1;
    /**
    * app类型 鸿坤金服
    */
    public final static int APP_SYS_TYPE_HKJF = 2;
    /**
    * app类型 财享+
    */
    public final static int APP_SYS_TYPE_CXJ = 3;
    /**
    * app类型 前生活
    */
    public final static int APP_SYS_TYPE_QSH = 4;
}
