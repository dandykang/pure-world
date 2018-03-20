package com.dandykang.learn.flowmanage;

import com.dandykang.learn.common.spring.SpringContext;
import com.dandykang.learn.flowmanage.handler.IHandler;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Administrator create on 2018/3/17
 **/
@Service("flowManage")
public class FlowManage {
    public static final Logger logger = LoggerFactory.getLogger(FlowManage.class);

    private Map<String, Map<String, HandlerProperty>> handlerMap = new ConcurrentHashMap<>();


    @PostConstruct
    public void initHandler(){
        Map<String, Map<String, HandlerProperty>> handlerMapTmp = new ConcurrentHashMap<>();

        Map<String, HandlerProperty> handlerPropertyMap = new HashMap<>();
        registHandler(handlerPropertyMap, (IHandler) SpringContext.getBean("startHandler"), 1);

        LinkedHashMap sortedMap = new LinkedHashMap();
        ArrayList<Map.Entry<String, HandlerProperty>> entryList = new ArrayList(handlerPropertyMap.entrySet());
        Collections.sort(entryList, (s1, s2) ->{
            return (s1.getValue().compareTo(s2.getValue()));
        });

        StringBuilder handlerOrder = new StringBuilder();
        for (Map.Entry<String, HandlerProperty> tmpEntry : entryList) {
            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
            handlerOrder.append(tmpEntry.getKey()).append(" ");
        }
        handlerMapTmp.put("uniqueId", sortedMap);
        logger.info("hander order is : ", handlerOrder);

        this.handlerMap = handlerMapTmp;
    }

    public void execute(HandlerContext handlerContext) throws Exception{
        Iterator<HandlerProperty> iterator = this.handlerMap.get(handlerContext.getUniqueId()).values().iterator();
        while (iterator.hasNext()){
            HandlerProperty hp = iterator.next();
            boolean success = hp.getHandler().hook(handlerContext);
            if(!success){
                break;
            }
        }
    }

    public FlowManage registHandler(Map handlerMap, IHandler handler, int priority){
        HandlerProperty hp = new HandlerProperty();
        hp.setHandlerName(handler.getClass().getSimpleName());
        hp.setHandler(handler);
        hp.setPriority(priority);
        handlerMap.put(hp.getHandlerName(), hp);
        return this;
    }

    @Data
    public class HandlerProperty implements Comparable<HandlerProperty> {
        IHandler handler;
        String handlerName;
        int priority;

        @Override
        public int compareTo(HandlerProperty o) {
            return o == null ? -1 : this.priority - o.priority;
        }

        @Override
        public boolean equals(Object obj){
            if(this == obj){
                return true;
            } else if (obj == null){
                return false;
            } else if (this.getClass() != obj.getClass()){
                return false;
            } else {
                HandlerProperty other = (HandlerProperty) obj;
                if(this.handlerName == null){
                    if(other.handlerName != null){
                        return false;
                    }
                } else if (!this.handlerName.equals(other.handlerName)){
                    return false;
                }
                return true;
            }
        }
    }
}
