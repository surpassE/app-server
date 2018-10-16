package com.dobe.appserver.model;

import com.sirding.annotation.Option;

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
    @Option(isSection = true)
    private String code;
    /**
    * app名称
    */
    @Option
    private String label;
    /**
    * 版本号
    */
    @Option
    private String versionName;
    /**
    * 构建版本号
    */
    @Option
    private String versionCode;
    /**
    * 图标文件
    */
    @Option
    private String icon;
    /**
     * 1-ios 2-android
     */
    @Option
    private Integer appType;
    /**
     * 1-qkd 2-hkjf 3-cxj 4-qsh
     */
    private Integer sysType;
    /**
     * 1-dev(snapshoot) 2-beta 3-stable(released)
     */
    private Integer envType;
    @Option
    private String fileName;
    @Option
    private Long fileSize;
    @Option
    private String suffix;
    @Option
    private String packageName;
    @Option
    private String time;
    
    private Integer start = 0;
    private Integer rows = 5;
    /**
    * 查询条件
    */
    private String search;
    
    /**
    * 解析过程中状态
    */
    private boolean state = true;
    @Option
    private String md5;
    

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

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getStart() {
        return start;
    }

    public AppInfo setStart(Integer start) {
        this.start = start;
        return this;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
    public boolean isState() {
        return state;
    }

    public AppInfo setState(boolean state) {
        this.state = state;
        return this;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "code='" + code + '\'' +
                ", label='" + label + '\'' +
                ", versionName='" + versionName + '\'' +
                ", versionCode='" + versionCode + '\'' +
                ", icon='" + icon + '\'' +
                ", appType=" + appType +
                ", sysType=" + sysType +
                ", envType=" + envType +
                ", fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                ", suffix='" + suffix + '\'' +
                ", packageName='" + packageName + '\'' +
                '}';
    }
}
