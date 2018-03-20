package com.dandykang.learn.flowmanage.handler;

import com.dandykang.learn.flowmanage.HandlerContext;

/**
 * Administrator create on 2018/3/17
 **/
public interface IHandler {

    boolean hook(HandlerContext handlerContext) throws Exception;

}
