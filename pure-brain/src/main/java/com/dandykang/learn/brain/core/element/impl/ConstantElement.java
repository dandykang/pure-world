package com.dandykang.learn.brain.core.element.impl;

import com.dandykang.learn.brain.base.EventBean;
import com.dandykang.learn.brain.base.MainDim;
import com.dandykang.learn.brain.core.element.Element;
import org.json.JSONObject;

import java.util.Map;

import static com.dandykang.learn.brain.core.rule.RuleJsonInfo.KEY_FROM;
import static com.dandykang.learn.brain.core.rule.RuleJsonInfo.KEY_TYPE;
import static com.dandykang.learn.brain.core.rule.RuleJsonInfo.KEY_VALUE;

/**
 * Administrator create on 2017/12/16
 **/
public class ConstantElement implements Element {
    private Object constantValue;
    private String constantType;
    private String jValue;
    private String jFrom;
    private boolean asElement;

    public ConstantElement(Object constantValue, String constantType, String jValue, String jFrom, boolean asElement) {
        this.constantValue = constantValue;
        this.constantType = constantType;
        this.jValue = jValue;
        this.jFrom = jFrom;
        this.asElement = asElement;
    }


    @Override
    public Object getValue(EventBean eventBean) throws Exception {
        return this.constantValue;
    }

    @Override
    public void init() throws Exception {

    }

    @Override
    public String getType() {
        return this.constantType;
    }

    @Override
    public MainDim getMainDim() {
        /*常量无MainDim*/
        return null;
    }

    @Override
    public Map<String, Boolean> getFields(Map<MainDim, Map<String, Boolean>> storeFields) throws Exception {
        /*常量无field*/
        return null;
    }

    @Override
    public Boolean asElement() {
        return this.asElement;
    }

    @Override
    public JSONObject getJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(KEY_FROM, jFrom);
        jsonObject.put(KEY_TYPE, constantType);
        jsonObject.put(KEY_VALUE, jValue);
        return jsonObject;
    }

    @Override
    public void getIndicant(Map<MainDim, Map<String, FunctionElement>> indicantMap) {
        /*常量不涉及指标*/
    }
}
