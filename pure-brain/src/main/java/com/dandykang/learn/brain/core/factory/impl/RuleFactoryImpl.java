package com.dandykang.learn.brain.core.factory.impl;

import com.dandykang.learn.brain.base.ParameterInfo;
import com.dandykang.learn.brain.base.Result;
import com.dandykang.learn.brain.base.Rule;
import com.dandykang.learn.brain.base.RuleOper;
import com.dandykang.learn.brain.core.element.Element;
import com.dandykang.learn.brain.core.factory.RuleFactory;
import com.dandykang.learn.brain.core.rule.ElementTool;
import com.dandykang.learn.brain.core.rule.RuleCheck;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.dandykang.learn.brain.core.rule.RuleJsonInfo.*;

/**
 * Administrator create on 2017/12/18
 **/
public class RuleFactoryImpl implements RuleFactory {
    public static final Logger logger = LoggerFactory.getLogger(RuleFactoryImpl.class);

    @Setter
    private String ruleRetType = "bool";
    @Setter
    private List<String> ruleFroms;
    @Setter
    private RuleCheck ruleCheck;
    @Setter
    private ElementTool elementTool;

    private ParameterInfo ruleParamInfo;

    @Override
    public Result ruleIsLegal(String ruleJson) {
        if(ruleJson == null){
            return Result.builder().success(false).reason("参数不能为null").build();
        }
        JSONObject ruleJsonObj;
        try {
            ruleJsonObj = new JSONObject(ruleJson);
        } catch (Exception e){
            return Result.builder().success(false).reason("规则json串异常").build();
        }
        return ruleCheck.elementIsLegal(ruleJsonObj, ruleRetType, ruleFroms);
    }

    @Override
    public Result indicantIsLegal(String funcName, String argument, String from, String type) {
        if(funcName == null || argument == null || from == null || type == null){
            return Result.builder().success(false).reason("所有参数不能为null").build();
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(KEY_FROM, from);
        jsonObject.put(KEY_TYPE, type);

        JSONObject value =new JSONObject();
        value.put(KEY_FUNCNAME, funcName);
        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray = new JSONArray(argument);
        } catch (Exception e){
            return Result.builder().success(false).reason("argument参数json格式异常").build();
        }
        value.put(KEY_ARGUMENT, jsonArray);
        jsonObject.put(KEY_VALUE, value);
        List<String> froms = new ArrayList<>();
        froms.add(from);
        return ruleCheck.elementIsLegal(jsonObject, type, froms);
    }

    @Override
    public Result getRuleOper(Rule rule, String businessUuid, boolean separate) {
        Result result = ruleIsLegal(rule.getContent());
        if(!result.isSuccess()){
            return result;
        }
        if (ruleParamInfo == null){
            ruleParamInfo = new ParameterInfo(ruleRetType, ruleFroms, Boolean.FALSE, Boolean.TRUE);
            ruleParamInfo.setAsElement(Boolean.FALSE);
            ruleParamInfo.setIsCacheInFilterField(Boolean.FALSE);
        }
        JSONObject ruleJsonObj = new JSONObject(rule.getContent());
        Element element = elementTool.getElement(businessUuid, ruleJsonObj, ruleParamInfo, separate);
        RuleOper ruleOper = new RuleOper(rule, element);
        return Result.builder().success(true).exData(ruleOper).build();
    }
}
