package jjk.cspubliccomponent.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @author jjk
 * @create 2020-09-02 12:08
 * @Describtion 消息确认实现
 **/
@Slf4j
public class MsgSendCallBackConfirm implements RabbitTemplate.ConfirmCallback {
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String msg) {
        if (ack) {
            log.info("msg send ok");
        } else {
            log.warn("msg send error: {}", msg);
        }
    }

}
