package com.dandykang.learn.brain.core.element.impl;

import com.dandykang.learn.brain.base.EventBean;
import com.dandykang.learn.brain.base.MainDim;
import com.dandykang.learn.brain.core.element.Element;
import com.dandykang.learn.brain.core.rule.RuleCheck;
import lombok.Data;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.dandykang.learn.brain.core.rule.RuleJsonInfo.KEY_FROM;
import static com.dandykang.learn.brain.core.rule.RuleJsonInfo.KEY_TYPE;
import static com.dandykang.learn.brain.core.rule.RuleJsonInfo.KEY_VALUE;

/**
 * Administrator create on 2017/12/16
 **/
@Data
public abstract class FieldElement implements Element {
    private String fieldName;
    private String fieldType;
    private String fieldFrom;
    private boolean el;
    private boolean cacheInFilterField;
    /*如果当前element所在的parameter配置里isDim为true，则此值不为null*/
    private MainDim mainDim;

    public FieldElement(String fieldName, String fieldType, String fieldFrom, boolean el, boolean cacheInFilterField) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.fieldFrom = fieldFrom;
        this.el = el;
        this.cacheInFilterField = cacheInFilterField;
    }
    public FieldElement(String businessUuid, String fieldName, String fieldType, String fieldFrom, boolean el, boolean cacheInFilterField) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.fieldFrom = fieldFrom;
        this.mainDim = new MainDim(businessUuid, this.fieldName);
        this.el = el;
        this.cacheInFilterField = cacheInFilterField;
    }


    @Override
    public Map<String, Boolean> getFields(Map<MainDim, Map<String, Boolean>> storeFields) throws Exception {
        Map<String, Boolean> map = new HashMap<>();
        if(RuleCheck.isInDim.get() && this.cacheInFilterField){
            /*value为false表示 该字段的值在本地缓存（该字段所在函数是在filter里，并且该字段所在函数的结果可以被EventBean缓存）时时可以精简的*/
            map.put(this.fieldName, Boolean.FALSE);
        } else {
            map.put(this.fieldName, Boolean.TRUE);
        }
        return map;
    }

    @Override
    public JSONObject getJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(KEY_FROM, fieldFrom);
        jsonObject.put(KEY_TYPE, fieldType);
        jsonObject.put(KEY_VALUE, fieldName);
        return jsonObject;
    }

    @Override
    public void getIndicant(Map<MainDim, Map<String, FunctionElement>> indicantMap) {
        /*字段不涉及指标*/
    }
}
