package jjk.cspubliccomponent.service.mqConsuer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static jjk.cspubliccomponent.config.MyRabbitMqConfig.QUEUE1;
import static jjk.cspubliccomponent.config.MyRabbitMqConfig.QUEUE2;

/**
 * @author jjk
 * @create 2020-09-02 16:26
 * @Describtion 消费者
 **/
@Component
@Slf4j
public class MqConsumer {

    @RabbitHandler
    @RabbitListener(queues = {QUEUE1, QUEUE2}, containerFactory = "rabbitListenerContainerFactory")
    public void consumerMsg(Channel channel, String msg, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        try {
            channel.basicAck(tag, false);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
