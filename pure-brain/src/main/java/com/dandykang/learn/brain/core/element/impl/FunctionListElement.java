package com.dandykang.learn.brain.core.element.impl;

import com.dandykang.learn.brain.base.EventBean;
import com.dandykang.learn.brain.base.MainDim;
import com.dandykang.learn.brain.core.element.Element;
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;

import static com.dandykang.learn.brain.core.rule.RuleJsonInfo.*;

/**
 * Administrator create on 2017/12/16
 **/
@Data
public class FunctionListElement implements Element {
    public static final Logger logger = LoggerFactory.getLogger(FunctionListElement.class);

    private List<Element> list;
    private String filterType;
    private boolean and;
    private boolean el;
    private String jFrom;

    /*在functionElementList中一个子element执行结果为null(异常)时，都认为是false*/
    @Override
    public Object getValue(EventBean eventBean) throws Exception {
        if(list == null || list.size() == 0){
            return Boolean.TRUE;
        }
        Boolean r;
        if(and){
            for (Element element : list){
                try {
                    r = (Boolean) element.getValue(eventBean);
                    if (r == null || !r){
                        return Boolean.FALSE;
                    }
                }catch (Exception e){
                    /*filter异常也认为是false*/
                    logger.error("FunctionListElement中 and element执行异常");
                    return Boolean.FALSE;
                }
            }
            return Boolean.TRUE;
        } else {
            for (Element element : list){
                try {
                    r = (Boolean) element.getValue(eventBean);
                    if (r != null && r){
                        return Boolean.TRUE;
                    }
                }catch (Exception e){
                    logger.error("FunctionListElement中 or element执行异常");
                }
            }
            return Boolean.FALSE;
        }
    }

    @Override
    public void init() throws Exception {

    }

    @Override
    public String getType() {
        return this.filterType;
    }

    @Override
    public MainDim getMainDim() {
        return null;
    }

    @Override
    public Map<String, Boolean> getFields(Map<MainDim, Map<String, Boolean>> storeFields) throws Exception {
        if(list == null || list.size() == 0){
            return null;
        }
        Map<String, Boolean> fields = new HashMap<>();
        for (Element element : list){
            /*正常情况下functionListElement中不应该包含maindim，如果包含，可能会有问题*/
            Map<String, Boolean> mapTmp = element.getFields(storeFields);
            if(mapTmp != null && mapTmp.size() >0 ){
                for (String fieldName : mapTmp.keySet()){
                    if(!fields.containsKey(fieldName)){
                        /*functionListElement中element依赖的字段都是filter中需要的字段，所以设置为false*/
                        fields.put(fieldName, mapTmp.get(fieldName));
                    }else if (!fields.get(fieldName) && mapTmp.get(fieldName)){
                        fields.put(fieldName, mapTmp.get(fieldName));
                    }
                }
            }
        }
        return fields;
    }

    @Override
    public Boolean asElement() {
        return this.el;
    }

    @Override
    public JSONObject getJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(KEY_FROM, jFrom);
        jsonObject.put(KEY_TYPE, filterType);
        JSONArray jsonArray = new JSONArray();
        jsonObject.put(KEY_VALUE, jsonArray);
        for (Element element : list){
            jsonArray.put(element.getJSON());
        }
        return jsonObject;
    }

    @Override
    public void getIndicant(Map<MainDim, Map<String, FunctionElement>> indicantMap) {
        for (Element element : list){
            element.getIndicant(indicantMap);
        }
    }
}
