package com.dandykang.learn.brain.base;


import com.dandykang.learn.brain.core.element.Element;
import com.dandykang.learn.brain.core.element.impl.FunctionElement;
import com.dandykang.learn.brain.core.rule.RuleCheck;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Administrator create on 2017/12/16
 **/
public class RuleOper {
    public static final Logger logger = LoggerFactory.getLogger(RuleOper.class);

    private Element element;
    @Getter
    private Rule rule;
    @Getter
    private long startTimeStamp;
    @Getter
    private long endTimeStamp;

    public RuleOper(Rule rule, Element element) {
        this.rule = rule;
        this.element = element;
        if (rule.getStartTime() != null) {
            startTimeStamp = rule.getStartTime().getTime();
        } else {
            startTimeStamp = 0L;
        }
        if (rule.getEndTime() != null) {
            startTimeStamp = rule.getEndTime().getTime();
        } else {
            startTimeStamp = Long.MAX_VALUE;
        }
    }

    public Boolean judge() {
        try {
            EventBean eventBean = BrainContext.get().getEventBean();
            long curr = eventBean.getEventTimeStamp();
            if (startTimeStamp <= curr && endTimeStamp >= curr) {
                return (Boolean) element.getValue(BrainContext.get().getEventBean());
            } else {
                return Boolean.FALSE;
            }
        } catch (Exception e) {
            logger.error("规则：{}在对事件：{}匹配时有异常，异常信息：{}", rule.getPolicyUuid(), BrainContext.get().getEventBean().getSequence(), e.getMessage());
            return Boolean.FALSE;
        }
    }

    public Map<MainDim, Set<String>> getStoreFields(boolean withFilterFields){
        Map<MainDim,Map<String,Boolean>> fields = new HashMap<>();

        try {
            RuleCheck.isInDim.set(Boolean.FALSE);
            element.getFields(fields);
            Map<MainDim, Set<String>>  result = new HashMap<>();
            if(withFilterFields){
                for(MainDim mainDim : fields.keySet()){
                    result.put(mainDim, fields.get(mainDim).keySet());
                }
            } else {
                for(MainDim mainDim : fields.keySet()){
                    Set<String> set = new HashSet<>();
                    result.put(mainDim, set);
                    Map<String, Boolean> map = fields.get(mainDim);
                    for (String md : map.keySet()){
                        if(map.get(md)){
                            set.add(md);
                        }
                    }
                }
            }
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Map<MainDim, Map<String, FunctionElement>> getIndicantMap(){
        Map<MainDim, Map<String, FunctionElement>> map = new HashMap<>();
        element.getIndicant(map);
        return map;
    }
}
