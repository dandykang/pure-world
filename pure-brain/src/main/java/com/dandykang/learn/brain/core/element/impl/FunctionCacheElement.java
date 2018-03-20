package com.dandykang.learn.brain.core.element.impl;

import com.dandykang.learn.brain.base.BrainContext;
import com.dandykang.learn.brain.base.EventBean;
import com.dandykang.learn.brain.base.MainDim;
import com.dandykang.learn.brain.core.element.Element;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.util.Map;

/**
 * Administrator create on 2017/12/16
 **/
public class FunctionCacheElement implements Element {
    @Setter
    @Getter
    private FunctionElement element;

    @Override
    public Object getValue(EventBean eventBean) throws Exception {
        Object cache = BrainContext.get().getCache(element.getResultKey());
        if(cache !=  null){
            return cache;
        }
        cache = element.getValue(eventBean);
        BrainContext.get().setCache(element.getResultKey(), cache);
        return cache;
    }

    @Override
    public void init() throws Exception {
        element.init();
    }

    @Override
    public String getType() {
        return element.getType();
    }

    @Override
    public MainDim getMainDim() {
        return element.getMainDim();
    }

    @Override
    public Map<String, Boolean> getFields(Map<MainDim, Map<String, Boolean>> storeFields) throws Exception {
        return element.getFields(storeFields);
    }

    @Override
    public Boolean asElement() {
        return element.asElement();
    }

    @Override
    public JSONObject getJSON() {
        return element.getJSON();
    }

    @Override
    public void getIndicant(Map<MainDim, Map<String, FunctionElement>> indicantMap) {
        element.getIndicant(indicantMap);
    }
}
