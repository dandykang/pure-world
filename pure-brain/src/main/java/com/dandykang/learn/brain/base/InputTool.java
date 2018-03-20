package com.dandykang.learn.brain.base;

/**
 * Administrator create on 2017/12/16
 **/
public interface InputTool {

    /**
     * 规则、指标 Json串中from为Input是会调用该方法校验、转换
     * @param from json中from值
     * @param type json中type
     * @param value json中value
     * @return success表示是否校验通过，通过后exData中是转换后的Object
     */
    Result str2Obj(String from, String type, String value);
}
