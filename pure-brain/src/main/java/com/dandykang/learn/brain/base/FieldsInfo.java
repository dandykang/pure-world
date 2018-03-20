package com.dandykang.learn.brain.base;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Administrator create on 2017/12/16
 **/
public class FieldsInfo {

    private Map<String, Map<String, Set<String>>> businessFields = new HashMap<>();

    public Result setBusinessFields(String businessUuid, Map<String, Set<String>> fields){
        if(null == businessUuid){
            return Result.builder().success(false).reason("businessUuid 不能为空.").build();
        }
        if(fields == null){
            return Result.builder().success(false).reason("fields 不能为空.").build();
        }
        for(Set<String> list : fields.values()){
            if(null == list){
                return Result.builder().success(false).reason("field 值不能为空.").build();
            }
        }
        if(fields.containsKey(null)){
            fields.remove(null);
        }
        businessFields.put(businessUuid, fields);
        return Result.builder().success(true).build();
    }

    public Map<String, Set<String>> getFields(String businessUuid){
        return businessFields.get(businessUuid);
    }
}
