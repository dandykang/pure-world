package com.dandykang.learn.brain.cache;

import com.dandykang.learn.brain.base.MainDim;
import com.dandykang.learn.brain.core.element.impl.FunctionElement;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Administrator create on 2017/12/16
 **/
public class BoolCacheManager {

    private class CacheInfo{
        Map<String, ManagerInfo> data = new HashMap<>();
        int version = 0;
    }
    private CacheInfo cacheInfo = new CacheInfo();

    public synchronized void register(Map<String, Map<MainDim,Set<FunctionElement>>> allBusinessIndicant){
//        Map<String, Set<String>> businessKeys = getBusinessKeys(allBusinessIndicant);
//        if()
    }
}
