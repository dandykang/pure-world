package com.dandykang.learn.brain.core.rule;

import com.dandykang.learn.brain.base.From;
import com.dandykang.learn.brain.base.InputTool;
import com.dandykang.learn.brain.base.Result;
import lombok.Setter;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Administrator create on 2017/12/18
 **/
public class RuleCheck {
    @Setter
    private FunctionManager functionManager;
    @Setter
    private Map<String, From> fromConversion;
    @Setter
    private InputTool inputTool;
    @Setter
    private Map<String, List<String>> filtersFromInfo;

    public static ThreadLocal<Boolean> isInDim = new ThreadLocal<>();

    public Result elementIsLegal(JSONObject ruleJson, String type, List<String> froms){
        isInDim.set(Boolean.FALSE);
        Map<String , List<String>> useFieldsInfo =  new HashMap<>();
        Result result = elementIsLegalInside(ruleJson, type, froms, useFieldsInfo);
        if(result.isSuccess()){
            return Result.builder().success(true).exData(useFieldsInfo).build();
        }else {
            return result;
        }
    }

    public Result elementIsLegalInside(JSONObject jsonObject, String type, List<String> froms, Map<String , List<String>> useFieldsInfo){
        return null;
    }

}
