package com.dobe.appserver.model;

import java.io.Serializable;

/**
 * app 信息
 *
 * @author zc.ding
 * @create 2018/10/11
 */
public class AppInfo implements Serializable {

    /**
    * 唯一标识
    */
    private String code;
    /**
    * app名称
    */
    private String label;
    /**
    * 版本号
    */
    private String versionName;
    /**
    * 构建版本号
    */
    private Long versionCode;
    /**
    * 图标文件
    */
    private String icon;
    /**
     * 1-ios 2-android
     */
    private Integer appType;
    /**
     * 1-qkd 2-hkjf 3-cxj 4-qsh
     */
    private Integer sysType;
    /**
     * 1-dev(snapshoot) 2-beta 3-stable(released)
     */
    private Integer envType;
    
    private String fileName;
    private Long fileSize;
    private String suffix;
    private String packageName;
    

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Long getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Long versionCode) {
        this.versionCode = versionCode;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getAppType() {
        return appType;
    }

    public void setAppType(Integer appType) {
        this.appType = appType;
    }

    public Integer getSysType() {
        return sysType;
    }

    public void setSysType(Integer sysType) {
        this.sysType = sysType;
    }

    public Integer getEnvType() {
        return envType;
    }

    public void setEnvType(Integer envType) {
        this.envType = envType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
