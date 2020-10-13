package jjk.cspubliccomponent.controller;

import jjk.cspubliccomponent.service.mqProducer.MqProducer;
import jjk.csutils.pojo.ApiResult;
import jjk.csutils.pojo.SuccessResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jjk
 * @create 2020-09-02 16:40
 * @Describtion rabbitmq相关控制
 **/
@RestController
@RequestMapping("/mq")
public class MqController {
    @Autowired
    private MqProducer mqProducer;

    @GetMapping("/triggerProducer")
    public ApiResult triggerProducer() {
        String msg = "这是一条消息";
        mqProducer.sendMqMsg(msg);
        return new SuccessResult<>("请求成功");
    }
}
