package jjk.cspubliccomponent.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jjk
 * @create 2020-09-01 16:26
 * @Describtion RabbitMq配置
 **/
@Configuration
public class MyRabbitMqConfig {
    //参数设定
    @Autowired
    private ConnectionFactory connectionFactory;
    //routingkey1
    public static final String ROUTINGKEY1 = "QUEUE1";
    //routingkey2
    public static final String ROUTINGKEY2 = "QUEUE2";
    //交换机名字
    public static final String EXCHANGE = "myExchange";
    //队列1名称
    public static final String QUEUE1 = "oneQueue";
    //队列2名称
    public static final String QUEUE2 = "twoQueue";

    /**
     * 获取队列1
     *
     * @return
     */
    @Bean
    public Queue getOneQueue() {
        return new Queue(QUEUE1, true, false, false);
    }

    /**
     * 获取队列2
     *
     * @return
     */
    @Bean
    public Queue getTwoQueue() {
        return new Queue(QUEUE2, true, false, false);
    }

    /**
     * 获取路由
     *
     * @return
     */
    @Bean
    public DirectExchange getExchange() {
        return new DirectExchange(EXCHANGE, true, false);
    }

    /**
     * 队列1与路由绑定
     *
     * @return
     */
    @Bean
    public Binding bindingOne() {
        return BindingBuilder.bind(getOneQueue()).to(getExchange()).with(ROUTINGKEY1);
    }

    /**
     * 队列2与路由绑定
     *
     * @return
     */
    @Bean
    public Binding bindingTwo() {
        return BindingBuilder.bind(getTwoQueue()).to(getExchange()).with(ROUTINGKEY2);
    }

    /**
     * queue listener  观察 监听模式
     * 当有消息到达时会通知监听在对应的队列上的监听对象
     *
     * @return
     */
    @Bean
    public SimpleMessageListenerContainer messageListenerContainer() {
        SimpleMessageListenerContainer smlc = new SimpleMessageListenerContainer(connectionFactory);
        smlc.addQueues(getOneQueue(), getTwoQueue());
        smlc.setExposeListenerChannel(true);
        smlc.setConcurrentConsumers(1);
        smlc.setMaxConcurrentConsumers(5);
        smlc.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return smlc;
    }

    /**
     * 消息发送接收模板
     *
     * @return
     */
    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setConfirmCallback(msgSendCallBackConfirm());
        return rabbitTemplate;
    }

    /**
     * 消息确认回执
     *
     * @return
     */
    @Bean
    public MsgSendCallBackConfirm msgSendCallBackConfirm() {
        return new MsgSendCallBackConfirm();
    }
}
