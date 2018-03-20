package com.dandykang.learn.brain.base;

import com.dandykang.learn.brain.cache.EventWithCache;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Administrator create on 2017/12/16
 **/
@Data
@Accessors(chain = true)
public class EventBean implements Serializable, Cloneable{
    private static Map<String, Field> eventBeanFieldCache = new HashMap<>();

    private String busiUuid;
    private String sequence;
    private Long eventTimeStamp;
    private String userId;
    private String phone;
    private String ip;
    private String email;
    private String result;
    private transient EventWithCache ref;
    private Map<String, Object> addiMap = new HashMap<>();

    static {
        for (Class<?> clazz = EventBean.class; clazz != Object.class; clazz = clazz.getSuperclass()){
            Field[] fields = clazz.getDeclaredFields();
            for(Field field : fields){
                if(!eventBeanFieldCache.containsKey(field.getName())){
                    eventBeanFieldCache.put(field.getName(), field);
                }
            }
        }
        eventBeanFieldCache.remove("addiMap");
        eventBeanFieldCache.remove("ref");
    }

    public Object getFieldValue(String fieldName){
        Field field = eventBeanFieldCache.get(fieldName);
        if(field != null){
            try {
                return field.get(this);
            }catch (IllegalAccessException ignore){
                return null;
            }
        }else {
            return this.addiMap.get(fieldName);
        }
    }

    public EventBean setFieldValud(String fieldName, Object value){
        Field field = eventBeanFieldCache.get(fieldName);
        if(field != null){
            try {
                field.set(this, value);
            }catch (IllegalAccessException ignore){

            }
        }else {
            this.addiMap.put(fieldName, value);
        }
        return this;
    }
}
