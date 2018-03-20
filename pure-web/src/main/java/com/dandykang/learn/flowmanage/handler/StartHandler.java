package com.dandykang.learn.flowmanage.handler;

import com.dandykang.learn.flowmanage.HandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Administrator create on 2018/3/17
 **/
@Service("startHandler")
public class StartHandler implements IHandler {
    public static final Logger logger = LoggerFactory.getLogger(StartHandler.class);

    @Override
    public boolean hook(HandlerContext handlerContext) throws Exception {

        logger.info("startHandler done !");
        return true;
    }
}
