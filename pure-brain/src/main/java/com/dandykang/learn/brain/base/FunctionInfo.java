package com.dandykang.learn.brain.base;

import lombok.Data;
import org.omg.PortableInterceptor.ObjectReferenceFactory;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

/**
 * Administrator create on 2017/12/16
 **/
@Data
public class FunctionInfo {
    /*函数对应的的class的bean实例*/
    private Object classObject;
    /*函数返回值类型*/
    private String retType;
    /*函数参数可选列表，是两层List嵌套，内层每个list是一组参数列表，支持多组参数，顺序匹配*/
    private List<List<ParameterInfo>> paramsInfo;
    /*函数支持的from列表*/
    private List<String> funcClassify;
    /*函数结果是否仅与事件的EventBean有关（不同时间使用相同的单个EventBean调用该函数，结果是否一致），
    如果true，则当前函数作为含有主维度函数的参数是，会缓存该结果到EventBean中*/
    private Boolean cacheInFilter = Boolean.FALSE;

    /*当前函数默认依赖字段*/
    private Set<String> defaultStoreFields;

    /*如果不配置则默认是用 beanid*/
    private String funcName;
    /*hson串中函数名*/
    private String userFuncName;

    /*下面的属性是启动时加载/计算出来的*/
    private int argsLen;
    private Method method;
    private List<Class> paramType;
    private Boolean containsDim;
    private List<List<ParameterInfo>> paramsInfoList;
    /*paramType中第一个参数类型是否为EventBean，paramsInfo中不需要指定*/
    private Boolean paramWithBean;
}
