package com.dobe.appserver.dao;

import com.dobe.appserver.model.AppInfo;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * appInfo信息持久层
 *
 * @author zc.ding
 * @create 2018/10/12
 */
@Repository("inIRepositoryServiceImpl")
public class InIRepositoryServiceImpl implements RepositoryService{
    @Override
    public Integer saveAppInfo(AppInfo appInfo) {
        return null;
    }

    @Override
    public Integer delAppInfo(String code) {
        return null;
    }

    @Override
    public List<AppInfo> findAppInfoList() {
        return null;
    }
}
