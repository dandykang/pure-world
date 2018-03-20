package com.dandykang.learn.brain.base;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Accessors(chain = true)
public class BrainContext {
    private static final ThreadLocal<BrainContext> LOCAL = ThreadLocal.withInitial(BrainContext::new);

    @Setter
    private Map<MainDim, List<EventBean>> historyEvents;

    @Setter
    private Map<String, Object> cache;
    @Getter
    @Setter
    private EventBean eventBean;
    /**
     * 指标初始化context
     * @param eventBean
     * @param historyEvents
     * @return
     */
    public static BrainContext init4Indicant(EventBean eventBean, Map<MainDim, List<EventBean>> historyEvents){
        if(null == historyEvents){
            historyEvents = new HashMap<>();
        }
        return LOCAL.get().setEventBean(eventBean)
                .setCache(null)
                .setHistoryEvents(historyEvents);
    }

    /**
     * 分离情况下规则初始化
     * @param eventBean
     * @param cache
     * @return
     */
    public static  BrainContext init4Policy(EventBean eventBean,  Map<String, Object> cache){
        if(null == cache){
            cache = new HashMap<>();
        }
        return LOCAL.get().setEventBean(eventBean)
                .setCache(cache)
                .setHistoryEvents(null);
    }

    /**
     * 不分离情况下初始化BrainContext
     * @param eventBean
     * @param historyEvents
     * @return
     */
    public static BrainContext init(EventBean eventBean, Map<MainDim, List<EventBean>> historyEvents){
        if(null == historyEvents){
            historyEvents = new HashMap<>();
        }
        return LOCAL.get().setEventBean(eventBean)
                .setCache(new HashMap<>())
                .setHistoryEvents(historyEvents);
    }

    public static BrainContext get() { return LOCAL.get();}

    public Object getCache(String key) { return cache.get(key); }

    public BrainContext setCache(String key, Object value){
        cache.put(key, value);
        return this;
    }

    public List<EventBean> getHistoryBeans(MainDim mainDim){
        return historyEvents.get(mainDim);
    }
}
