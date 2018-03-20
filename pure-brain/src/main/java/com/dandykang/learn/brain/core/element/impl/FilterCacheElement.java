package com.dandykang.learn.brain.core.element.impl;

import com.dandykang.learn.brain.base.EventBean;
import com.dandykang.learn.brain.base.MainDim;
import com.dandykang.learn.brain.cache.BoolCacheManager;
import com.dandykang.learn.brain.core.element.Element;

import lombok.Setter;
import org.json.JSONObject;

import java.util.Map;

/**
 * Administrator create on 2017/12/16
 **/
public class FilterCacheElement implements Element {

    @Setter
    private FunctionElement element;
    @Setter
    private BoolCacheManager boolCacheManager;

    public String getResultKey(){
        return element.getResultKey();
    }
    @Override
    public Object getValue(EventBean eventBean) throws Exception {
        return null;
    }

    @Override
    public void init() throws Exception {

    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public MainDim getMainDim() {
        return null;
    }

    @Override
    public Map<String, Boolean> getFields(Map<MainDim, Map<String, Boolean>> storeFields) throws Exception {
        return null;
    }

    @Override
    public Boolean asElement() {
        return null;
    }

    @Override
    public JSONObject getJSON() {
        return null;
    }

    @Override
    public void getIndicant(Map<MainDim, Map<String, FunctionElement>> indicantMap) {

    }
}
