package com.dobe.appserver.web;

import com.dobe.appserver.model.AppInfo;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Appinfo;
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
    
    @RequestMapping("list")
    public List<Appinfo> list(AppInfo appInfo){
        
        return null;
    }
    
    @RequestMapping("upload")
    public String upload(MultipartFile file){
        

        return "success";
    }
}
