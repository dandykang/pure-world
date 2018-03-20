package com.dandykang.learn.brain.base;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Administrator create on 2017/12/16
 **/
@Data
public class Rule implements Serializable{
    private String ruleUuid;
    private String policyUuid;
    private String ruleName;
    private String content;
    private Double weight;
    private String action;
    private Integer order;
    private Boolean isPre = Boolean.FALSE;
    private Date startTime;
    private Date endTime;
}
