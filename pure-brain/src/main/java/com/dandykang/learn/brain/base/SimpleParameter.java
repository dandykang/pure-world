package com.dandykang.learn.brain.base;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Administrator create on 2017/12/28
 **/
@Data
public class SimpleParameter {

    private String type; /*当前参数要求的类型*/
    private List<String> froms; /*当前参数支持的from列表*/
    /*当前参数是否为主维度，正常来说只有from中全部为field或者fieldName是为true*/
    private Boolean isDim = Boolean.FALSE;
    /*是否是当前业务的主维度，若为false则需要同级别的parameterInfo中有from、input、type、bid*/
    private Boolean isCurrBid = Boolean.TRUE;

    public ParameterInfo getParameterInfo(){
        return new ParameterInfo(type,new ArrayList<>(),isDim, isCurrBid);
    }
}
