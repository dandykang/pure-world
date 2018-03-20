package com.dandykang.learn.brain.core.factory;

import com.dandykang.learn.brain.base.Result;
import com.dandykang.learn.brain.base.Rule;

/**
 * Administrator create on 2017/12/18
 **/
public interface RuleFactory {

    /**
     * 校验规则是否正确
     * @param ruleJson 规则json
     * @return
     */
    public Result ruleIsLegal(String ruleJson);

    /**
     * 校验指标是否正确
     * @param funcName 指标函数名
     * @param argument 指标参数json
     * @param from 指标放入规则时的from值
     * @param type 指标函数返回值类型
     * @return
     */
    public Result indicantIsLegal(String funcName, String argument, String from, String type);

    /**
     * 根据规则返回可执行的对象
     * @param rule 规则内容
     * @param businessUuid 业务id
     * @param separate 是否指标分离
     * @return
     */
    public Result getRuleOper(Rule rule, String businessUuid, boolean separate);
}
