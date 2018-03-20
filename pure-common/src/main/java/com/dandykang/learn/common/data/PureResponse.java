package com.dandykang.learn.common.data;

import lombok.Builder;
import lombok.Data;

/**
 * Administrator create on 2018/3/20
 **/
@Data
@Builder
public class PureResponse {
    private boolean success;
    private int retCode;
    private String msg;

    public static PureResponse creatSucc(){
        return PureResponse.builder().success(true).retCode(200).build();
    }

    public static PureResponse creatFail(){
        return PureResponse.builder().success(false).msg("异常").build();
    }
}
