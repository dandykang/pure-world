package com.dandykang.learn.controller;

import com.dandykang.learn.common.data.PureResponse;
import com.dandykang.learn.flowmanage.FlowManage;
import com.dandykang.learn.flowmanage.HandlerContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Administrator create on 2018/3/20
 **/
@Controller
@RequestMapping("/pure")
public class ReceiveController {
    public static final Logger logger = LoggerFactory.getLogger(ReceiveController.class);

    @Autowired
    FlowManage flowManage;

    @RequestMapping("ruler")
    @ResponseBody
    public PureResponse ruler(String sequenceId){
        try {
            HandlerContext handlerContext = new HandlerContext();
            if (StringUtils.isNotBlank(sequenceId)) {
                handlerContext.setUniqueId(sequenceId);
            } else {
                handlerContext.setUniqueId("uniqueId");
            }
            if(flowManage.getHandlerMap(handlerContext.getUniqueId()) == null){
                logger.error("id对应handler为空",handlerContext.getUniqueId());
                return PureResponse.creatFail();
            }
            flowManage.execute(handlerContext);
            return PureResponse.creatSucc();
        }catch (Exception e){
            logger.error("异常：{}",e);
            return PureResponse.creatFail();
        }
    }
}
