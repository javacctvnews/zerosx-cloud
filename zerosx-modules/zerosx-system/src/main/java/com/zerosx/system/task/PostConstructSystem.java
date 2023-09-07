package com.zerosx.system.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * InitSystem
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-08-31 16:08
 **/
@Component
public class PostConstructSystem {

    @Autowired
    private SystemAsyncTask systemAsyncTask;

    @PostConstruct
    public void initSomethings() {
        systemAsyncTask.initCacheDictData();
    }


}
