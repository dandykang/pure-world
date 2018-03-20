package com.dandykang.learn.brain.core.element.impl;

import com.dandykang.learn.brain.base.EventBean;
import com.dandykang.learn.brain.base.MainDim;
import com.dandykang.learn.brain.core.element.Element;
import org.json.JSONObject;

import java.util.Map;

/**
 * Administrator create on 2017/12/16
 **/
public class ContextFieldElement extends FieldElement {

    public ContextFieldElement(String fieldName, String fieldType, String fieldFrom, boolean el, boolean cacheInFilterField) {
        super(fieldName, fieldType, fieldFrom, el, cacheInFilterField);
    }

    public ContextFieldElement(String businessUuid, String fieldName, String fieldType, String fieldFrom, boolean el, boolean cacheInFilterField) {
        super(businessUuid, fieldName, fieldType, fieldFrom, el, cacheInFilterField);
    }

    @Override
    public Object getValue(EventBean eventBean) throws Exception {
        return eventBean.getFieldValue(this.getFieldName());
    }

    @Override
    public void init() throws Exception {

    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public Boolean asElement() {
        return this.isEl();
    }
}
