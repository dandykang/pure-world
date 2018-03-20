package com.dandykang.learn.brain.core.element;

import com.dandykang.learn.brain.base.EventBean;
import com.dandykang.learn.brain.base.MainDim;
import com.dandykang.learn.brain.core.element.impl.FunctionElement;
import org.json.JSONObject;

import java.util.Map;

/**
 * Administrator create on 2017/12/16
 **/
public interface Element {
    /**
     * 递归获取json串中规则函数计算的结果
     * @param eventBean
     * @return
     * @throws Exception
     */
    Object getValue(EventBean eventBean) throws Exception;

    /**
     * 生产element后进行初始化，暂时未用到
     * @throws Exception
     */
    void init() throws Exception;

    /**
     * @return 返回当前element在json中定义的type
     */
    String getType();

    /**
     * @return 返回当前element内的MainDim（不递归调用），可能为null
     */
    MainDim getMainDim();

    /**
     * 当前element内如果含有MainDim，则吧内部的element和当前element返回的字段写入storeFields中（Boolean为true代表字段是函数中用到的，false代表字段是filter中用到的）
     * 如果不含有MainDim则直接把当前和内部的element字段返回到外面
     * 在调用getFields钱必须先调用 RuleCheck.isInDim.set(Boolean.FALSE)先设置当前Element不在主维度内，否则结果中是否为filter中用到字段可能会有问题
     * @param storeFields
     * @return 当前element或其内部element中用到的字段（如果当前element含有MainDim则直接把数据写入storeFields
     * 如果没有MainDim 才返回出去）
     * @throws Exception
     */
    Map<String, Boolean> getFields(Map<MainDim, Map<String,Boolean>> storeFields) throws Exception;

    /**
     * @return 当前element是否在计算时直接把element作为参数
     */
    Boolean asElement();

    /**
     * 递归获取 纯净的json串
     * @return 当前element及其下的所有element的json串
     */
    JSONObject getJSON();

    /**
     * 获取当前element以及其下element内部的所有用于远端计算的指标函数
     * @param indicantMap
     */
    void getIndicant(Map<MainDim, Map<String, FunctionElement>> indicantMap);

}
