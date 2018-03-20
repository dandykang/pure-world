package com.dandykang.learn.brain.core.element.impl;

import com.dandykang.learn.brain.base.EventBean;
import com.dandykang.learn.brain.base.MainDim;
import com.dandykang.learn.brain.core.element.Element;
import com.dandykang.learn.brain.core.rule.RuleCheck;
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.dandykang.learn.brain.core.rule.RuleJsonInfo.*;

/**
 * Administrator create on 2017/12/16
 **/
@Data
public class FunctionElement implements Element {
    private Object funcObject;
    private String retType;
    private String funcName;
    private Method method;
    private List<Element> params;
    private Boolean paramWithBean;
    private String resultKey;
    private boolean el;
    private String jFrom;
    private String jType;
    private String jFuncname;
    private Set<String> defaultStoreField;
    private MainDim mainDim;

    @Override
    public Object getValue(EventBean eventBean) throws Exception {
        Object[] args;
        if(params.size() == 0){
            if(paramWithBean){
                args = new Object[1];
                args[0] = eventBean;
                return this.method.invoke(this.funcObject, args);
            } else {
                return this.method.invoke(this.funcObject);
            }
        }
        int i = 0;
        if(paramWithBean){
            args = new Object[this.params.size() + 1];
            args[i++] = eventBean;
        } else {
            args = new Object[this.params.size()];
        }
        for (Element element : this.params){
            if(element.asElement()){
                args[i++] = element;
            } else {
                args[i++] = element.getValue(eventBean);
            }
        }

        return this.method.invoke(this.funcObject, args);
    }

    @Override
    public void init() throws Exception {

    }

    @Override
    public String getType() {
        return retType;
    }

    @Override
    public MainDim getMainDim() {
        return null;
    }

    @Override
    public Map<String, Boolean> getFields(Map<MainDim, Map<String, Boolean>> storeFields) throws Exception {
        Map<String, Boolean> fields = new HashMap<>();
        if(defaultStoreField != null && defaultStoreField.size() > 0 ){
            for(String dim : defaultStoreField){
                fields.put(dim, Boolean.TRUE);
            }
        }
        Boolean parIsInDim = RuleCheck.isInDim.get();
        if(mainDim != null){
            RuleCheck.isInDim.set(Boolean.TRUE);
        }
        for (Element element : this.params){
            Map<String, Boolean> map = element.getFields(storeFields);
            if(map != null){
                for(String fieldName : map.keySet()){
                    if(!fields.containsKey(fieldName)){
                        fields.put(fieldName, map.get(fieldName));
                    } else if (!fields.get(fieldName) && map.get(fieldName)){
                        fields.put(fieldName, map.get(fieldName));
                    }
                }
            }
        }
        RuleCheck.isInDim.set(parIsInDim);
        if(mainDim == null){
            /*没有maindim代表该函数支队EventBean中fields中的字段进行处理，但不会涉及历史事件*/
            return fields;
        }
        if(!storeFields.containsKey(mainDim) || storeFields.get(mainDim) == null || storeFields.get(mainDim).size() <= 0){
            storeFields.put(mainDim, fields);
        } else {
            Map<String ,Boolean> storeMap = storeFields.get(mainDim);
            /*合并两个map，如果两个map中都含有maindim则优先使用为true的值*/
            for (String fieldName : fields.keySet()){
                if(!storeMap.containsKey(fieldName)){
                    storeMap.put(fieldName, fields.get(fieldName));
                } else if (!storeMap.get(fieldName) && fields.get(fieldName)){
                    storeMap.put(fieldName, fields.get(fieldName));
                }
            }
        }
        return null;
    }

    @Override
    public Boolean asElement() {
        return this.el;
    }

    @Override
    public JSONObject getJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(KEY_FROM, jFrom);
        jsonObject.put(KEY_TYPE, jType);
        JSONObject value =new JSONObject();
        jsonObject.put(KEY_VALUE, value);
        JSONArray jsonArray = new JSONArray();
        value.put(KEY_ARGUMENT, jsonArray);
        for (Element element : this.params){
            jsonArray.put(element.getJSON());
        }
        return jsonObject;
    }

    @Override
    public void getIndicant(Map<MainDim, Map<String, FunctionElement>> indicantMap) {
        if(mainDim != null){
            if(!indicantMap.containsKey(mainDim)){
                indicantMap.put(mainDim, new HashMap<>());
            }
            indicantMap.get(mainDim).put(this.getResultKey(), this);
            return;
        }
        for (Element element : this.params){
            element.getIndicant(indicantMap);
        }
    }
}
