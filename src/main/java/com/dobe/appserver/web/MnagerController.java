package com.dobe.appserver.web;

import com.dobe.appserver.constants.Constants;
import com.dobe.appserver.model.AppInfo;
import com.dobe.appserver.service.ManageService;
import com.dobe.appserver.utils.DateUtils;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Appinfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

/**
 * app server manaer
 *
 * @author zc.ding
 * @create 2018/10/11
 */

@RestController
@RequestMapping("/manage/")
public class MnagerController {
    
    @Autowired
    private ManageService manageService;
    
    @RequestMapping("list")
    public List<AppInfo> list(AppInfo appInfo){
        AppInfo app = new AppInfo();
        app.setCode("hello");
        app.setLabel("鸿坤金服");
        app.setIcon("default.png");
        app.setPackageName("com.yirun.qiankundai");
        app.setVersionCode("1.0.0");
        app.setVersionName("1.10");
        app.setEnvType(1);
        app.setAppType(2);
        app.setSysType(2);
        app.setTime(DateUtils.format());
//        return Collections.singletonList(app);
        return manageService.findAppInfoList(appInfo);
    }
    
    @RequestMapping("upload")
    public String upload(MultipartFile file){
        return this.manageService.upload(file);
    }
    
    @RequestMapping("del")
    public String del(String code){
        return this.manageService.delAppInfo(code).toString();
    }
}
