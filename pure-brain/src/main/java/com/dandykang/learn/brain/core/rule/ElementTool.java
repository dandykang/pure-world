package com.dandykang.learn.brain.core.rule;

import com.dandykang.learn.brain.base.*;
import com.dandykang.learn.brain.cache.BoolCacheManager;
import com.dandykang.learn.brain.core.element.Element;
import com.dandykang.learn.brain.core.element.impl.*;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.dandykang.learn.brain.core.rule.RuleJsonInfo.*;

/**
 * Administrator create on 2017/12/18
 **/
public class ElementTool {

    public static final Logger logger = LoggerFactory.getLogger(ElementTool.class);

    private String userFilterCache_Key = "ruleEngine.conf.userFilterCache";
    @Setter
    private Map<String, From> fromConversion;
    @Setter
    private InputTool inputTool;
    @Setter
    private FunctionManager functionManager;
    @Setter
    private RuleCheck ruleCheck;
    @Setter
    private BoolCacheManager boolCacheManager;

    private ThreadLocal<String>  businessUuidTL = new ThreadLocal<>();
    private ThreadLocal<Boolean>  separateTL = new ThreadLocal<>();
    private ThreadLocal<Boolean>  isInDim = new ThreadLocal<>();

    public Element getElement(String businessUuid, JSONObject rule, ParameterInfo parameterInfo, boolean separeta){
        businessUuidTL.set(businessUuid);
        separateTL.set(separeta);
        isInDim.set(Boolean.FALSE);
        return getElement(rule, parameterInfo, null);
    }

    /**
      * @param rule 规则json串
     * @param parameterInfo 如果当前element是外层函数的参数，则需要传入外层函数对该参数的要求
     * @param argumentLists 如果为函数每个参数，则需要把参数列表全部传入（因为又夸业务的指标需要获取到其他参数中的业务UUID）
     * @return 可执行的element
     */
    public Element getElement(JSONObject rule, ParameterInfo parameterInfo, JSONArray argumentLists){
        String from = rule.getString(KEY_FROM);
        String type = rule.getString(KEY_TYPE);
        switch (fromConversion.get(from)){
            case INPUT:
                Object constantValue = inputTool.str2Obj(from, type, rule.getString(KEY_VALUE)).getExData();
                return new ConstantElement(constantValue, type, rule.getString(KEY_VALUE), from, parameterInfo==null?false:parameterInfo.getAsElement());
            case FIELD:
                if(parameterInfo == null || !parameterInfo.getIsDim()){
                    return new ContextFieldElement(type, rule.getString(KEY_VALUE), from,
                            parameterInfo==null?false:parameterInfo.getAsElement(),parameterInfo==null?false:parameterInfo.getIsCacheInFilterField());
                }else {
                    String buuid = parameterInfo.getIsCurrBid()? businessUuidTL.get():argumentLists.getJSONObject(parameterInfo.getBidIdx()).getString(KEY_VALUE);
                    return new ContextFieldElement(type, rule.getString(KEY_VALUE), from, buuid, parameterInfo.getAsElement(), parameterInfo.getIsCacheInFilterField());
                }
            case FIELDNAME:
                if(parameterInfo == null || !parameterInfo.getIsDim()){
                    return new ConstantFieldElement(type, rule.getString(KEY_VALUE), from,
                            parameterInfo==null?false:parameterInfo.getAsElement(),parameterInfo==null?false:parameterInfo.getIsCacheInFilterField());
                }else {
                    String buuid = parameterInfo.getIsCurrBid()? businessUuidTL.get():argumentLists.getJSONObject(parameterInfo.getBidIdx()).getString(KEY_VALUE);
                    return new ConstantFieldElement(type, rule.getString(KEY_VALUE), from, buuid, parameterInfo.getAsElement(), parameterInfo.getIsCacheInFilterField());
                }
            case FUNCTION:
                return initFunctionElement(rule, parameterInfo, type, from);
            case FUNCLIST:
                return initFilterElement(rule, parameterInfo, true, from);
            case FUNCORLIST:
                return initFilterElement(rule, parameterInfo, false, from);
            default:
                throw new RuntimeException("为支持的from，请见车RuleCheck.elementIsLegal方法与ElementTool.getElement是否一致");
        }
    }

    private Element initFunctionElement(JSONObject rule, ParameterInfo parameterInfo, String type, String from){

        //函数未处理filter缓存
        String funcname = rule.getJSONObject(KEY_VALUE).getString(KEY_FUNCNAME);
        JSONArray argument = rule.getJSONObject(KEY_VALUE).getJSONArray(KEY_ARGUMENT);
        FunctionInfo functionInfo = functionManager.get(funcname);

        FunctionElement functionElement = new FunctionElement();
        functionElement.setFuncName(functionInfo.getFuncName());
        functionElement.setFuncObject(functionInfo.getClassObject());
        functionElement.setMethod(functionInfo.getMethod());
        functionElement.setRetType(functionInfo.getRetType());
        functionElement.setEl(parameterInfo == null ? false: parameterInfo.getAsElement() );
        functionElement.setDefaultStoreField(functionInfo.getDefaultStoreFields());

        functionElement.setJType(type);
        functionElement.setJFrom(from);
        functionElement.setJFuncname(functionInfo.getFuncName());
        functionElement.setParamWithBean(functionInfo.getParamWithBean());

        Boolean parIsInDim = isInDim.get();
        isInDim.set(functionInfo.getContainsDim());

        List<Element> params = new ArrayList<>();
        int paramLen = functionInfo.getArgsLen();
        if(functionInfo.getParamWithBean()){
            paramLen--;
        }
        /*因为函数paramsInfo中有多个list，此处只能和RuleCheck走相同的逻辑，由ruleCheck来决定使用那个list*/
        for (List<ParameterInfo> parameterInfos : functionInfo.getParamsInfo()){
            params = new ArrayList<>(parameterInfos.size());
            do {
                ParameterInfo pInfo = parameterInfos.get(params.size());
                JSONObject argumentI = argument.getJSONObject(params.size());
                Result result = ruleCheck.elementIsLegal(argumentI, pInfo.getType(), pInfo.getFromList());
                if(!result.isSuccess()){
                    break;
                }
                params.add(getElement(argumentI, pInfo, argument));
            } while (params.size() < paramLen);
            if(params.size() == paramLen){
                break;
            }
        }
        if(params.size() != paramLen){
            throw new RuntimeException("参数列表和配置单 不一致");
        }
        for (Element e : params){
            if(e.getMainDim() != null){
                functionElement.setMainDim(e.getMainDim());
                break;
            }
        }
        isInDim.set(parIsInDim);

        /*额外校验 初始化是从xml中获取到的containsDim结果是否和element中的一致*/
        if(functionInfo.getContainsDim() && functionElement.getMainDim() == null){
            logger.warn("从指标函数：{}的xml初始化配置中得到的该函数是包含主维度，但是json串中element中却没有主维度");
        }
        if(!functionInfo.getContainsDim() && functionElement.getMainDim() != null){
            logger.warn("从指标函数：{}的xml初始化配置中得到的该函数是 不包含主维度，但是json串中element中却有主维度");
        }

        functionElement.setParams(params);
        /*TODO md5*/
        functionElement.setResultKey("");
        if(functionElement.getMainDim()!=null){
            if (separateTL.get()){
                FunctionIndicantElement indicantElement = new FunctionIndicantElement();
                indicantElement.setElement(functionElement);
                return indicantElement;
            } else {
                FunctionCacheElement functionCacheElement = new FunctionCacheElement();
                functionCacheElement.setElement(functionElement);
                return functionCacheElement;
            }
        } else {
            if (parIsInDim && functionInfo.getCacheInFilter() && separateTL.get()){
                /*在规则指标分离的情况下，默认使用filterCache
                * 如果想要禁用filterCache需要使用System.setProperty来设置*/
                boolean useCache = true;
                String userFilterCache = System.getProperty(userFilterCache_Key);
                if(userFilterCache != null && !userFilterCache.equals("0")&& !userFilterCache.equals("1")){
                    logger.warn("userFilterCache_Key 值配置错误！");
                } else {
                    if (userFilterCache != null){
                        if(userFilterCache.equals("1")){
                            useCache = false;
                        }
                    }
                }
                if (useCache){
                    FilterCacheElement filterCacheElement = new FilterCacheElement();
                    filterCacheElement.setBoolCacheManager(boolCacheManager);
                    filterCacheElement.setElement(functionElement);
                    return filterCacheElement;
                }
            }
        }
        return functionElement;
    }

    private Element initFilterElement(JSONObject rule, ParameterInfo parameterInfo, boolean isAnd, String from){
        FunctionListElement functionListElement = new FunctionListElement();
        functionListElement.setFilterType(rule.getString(KEY_TYPE));
        functionListElement.setAnd(isAnd);
        functionListElement.setEl(parameterInfo == null ? false : parameterInfo.getAsElement());
        functionListElement.setJFrom(from);

        JSONArray filterArray = rule.getJSONArray(KEY_VALUE);
        List<Element> elementList = new ArrayList<>(filterArray.length());
        for (int i = 0; i < filterArray.length(); i++){
            JSONObject jsonObject = filterArray.getJSONObject(i);
            elementList.add(getElement(jsonObject, null, null));
        }
        functionListElement.setList(elementList);
        return functionListElement;
    }
}
