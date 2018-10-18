package com.dobe.appserver.web;

import com.dobe.appserver.constants.Config;
import com.dobe.appserver.model.AppInfo;
import com.dobe.appserver.service.ManageService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
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
    @Autowired
    private Config config;
    
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

    
    @RequestMapping("download")
    public void download(String code, String appType, String suffix, HttpServletRequest request, HttpServletResponse response){
        File file = new File(config.getBasePath() + "/" + appType + "/" + code + suffix);
        try(FileInputStream fis = new FileInputStream(file)){
            response.setContentType("application/octet-stream");
            response.setContentLength(Long.valueOf(file.length()).intValue());
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", file.getName());
            response.setHeader(headerKey, headerValue);
            IOUtils.copy(fis, response.getOutputStream());
            response.flushBuffer();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
