package com.dandykang.learn.brain.base;

import lombok.Data;

import java.util.List;

/**
 * Administrator create on 2017/12/16
 **/
@Data
public class ParameterInfo {
    /*当前参数类型*/
    private String type;
    /*参数支持的from列表*/
    private List<String> fromList;
    /*当前参数是否为主维度*/
    private Boolean isDim = Boolean.FALSE;
    /*是否为当前业务的主维度，若为false：则需要同级别的paramterInfo中有from input，type bid*/
    private Boolean isCurrBid = Boolean.TRUE;
    /*若isCurrBid是false，则此处需要指定bid在参数中的下标（从0开始，不包含EventBean）*/
    private Integer bidIdx;
    /*启动时加载计算*/
    private Boolean asElement;
    /*此值在初始化函数时，根据cacheInFilter来设置，只有在Element是field时有意义，
    * 表示Element所在函数如果在含有dim函数的内部，则该Element所在函数的结果支持缓存，
    * 因此该field可以精简，不进行保存*/
    private Boolean isCacheInFilterField;

    public ParameterInfo(String type, List<String> fromList, Boolean isDim, Boolean isCurrBid) {
        this.type = type;
        this.fromList = fromList;
        this.isDim = isDim;
        this.isCurrBid = isCurrBid;
    }
}
