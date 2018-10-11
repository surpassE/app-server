package com.dobe.appserver.constants;

public enum AppType {
    IOS(1, ".ipa"),
    ANDROID(2, ".apk");

    private int value;
    private String suffix;

    private AppType(int value, String suffix){
        this.value = value;
        this.suffix = suffix;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
