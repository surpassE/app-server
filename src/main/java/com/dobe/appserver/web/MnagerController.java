package com.dobe.appserver.web;

import com.dobe.appserver.model.AppInfo;
import com.dobe.appserver.service.ManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
        return manageService.findAppInfoList(appInfo);
    }
    
    @RequestMapping("upload")
    public String upload(MultipartFile file, Integer envType, Integer sysType){
        return this.manageService.upload(file, envType, sysType);
    }
    
    @RequestMapping("del")
    public String del(String code){
        return this.manageService.delAppInfo(code).toString();
    }
}
