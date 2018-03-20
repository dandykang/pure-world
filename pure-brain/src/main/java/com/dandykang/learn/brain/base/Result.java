package com.dandykang.learn.brain.base;

import lombok.Builder;
import lombok.Data;
import org.omg.PortableInterceptor.ObjectReferenceFactory;

/**
 * Administrator create on 2017/12/16
 **/
@Data
@Builder
public class Result {
    private String reason;
    private boolean success = true;
    private Object exData;
}
