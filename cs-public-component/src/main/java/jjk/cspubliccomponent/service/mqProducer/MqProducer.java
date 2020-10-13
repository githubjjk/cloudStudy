package jjk.cspubliccomponent.service.mqProducer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static jjk.cspubliccomponent.config.MyRabbitMqConfig.EXCHANGE;
import static jjk.cspubliccomponent.config.MyRabbitMqConfig.ROUTINGKEY1;

/**
 * @author jjk
 * @create 2020-09-02 12:15
 * @Describtion 消息生产者
 **/
@Component
@Slf4j
public class MqProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 生产消息
     *
     * @param msg
     */
    public void sendMqMsg(Object msg) {
        UUID uuid = UUID.randomUUID();
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(String.valueOf(uuid));
        rabbitTemplate.convertAndSend(EXCHANGE, ROUTINGKEY1, msg, correlationData);
    }
}
