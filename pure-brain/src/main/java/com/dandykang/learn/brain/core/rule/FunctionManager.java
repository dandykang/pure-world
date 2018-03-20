package com.dandykang.learn.brain.core.rule;

import com.dandykang.learn.brain.base.FunctionInfo;
import com.dandykang.learn.brain.base.SimpleParameter;
import com.dandykang.learn.common.spring.SpringContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Administrator create on 2017/12/18
 **/
public class FunctionManager {
    public static final Logger logger = LoggerFactory.getLogger(FunctionManager.class);

    private Map<String, FunctionInfo> functionInfoMap = new HashMap<>();

    private Boolean parameterCheck;

    private String unNamedBeanId = FunctionInfo.class.getName() + "#";

    public FunctionInfo get(String funcName) { return  functionInfoMap.get(funcName);}

    public void init(){
        if (parameterCheck != null && parameterCheck){
            checkSimpleParameter();
        }
        Map<String, FunctionInfo> map = SpringContext.getBeansOfType(FunctionInfo.class);
        logger.info("开始初始化函数，共：{} 个", map.size());

        for (String beanId : map.keySet()){
            FunctionInfo functionInfo = map.get(beanId);
            initFunctionInfoByXmlConf(functionInfo, beanId);
            logger.info("class:[{}]中的函数:[{}] 作为json中的[{}] 函数,json中返回结果类型为[{}]，函数的from可以为{}，json中的argument参数个数:{}个 ",
                    functionInfo.getClassObject().getClass().getSimpleName(),functionInfo.getFuncName(),functionInfo.getUserFuncName(), functionInfo.getRetType(),
                    functionInfo.getFuncClassify().toString(),functionInfo.getParamWithBean()?functionInfo.getArgsLen()-1:functionInfo.getArgsLen());
            functionInfoMap.put(functionInfo.getUserFuncName(), functionInfo);
        }
        logger.info("初始化完成，共：{} 个", functionInfoMap.size());
    }

    private void initFunctionInfoByXmlConf(FunctionInfo functionInfo, String beanId){

    }

    private void checkSimpleParameter(){

    }

    private void checkProperty(){

    }

    private String getParameterId(SimpleParameter simpleParameter){
        String tf = "t:" +simpleParameter.getType() + "f:" +String.join(".",simpleParameter.getFroms());
        if (simpleParameter.getIsDim()){
            if (simpleParameter.getIsCurrBid()){
                return tf + ",mainDim";
            } else {
                return tf + ",mainDim.across";
            }
        } else {
            return tf;
        }
    }
}
