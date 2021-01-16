package com.xm.cpsmall.comm.mq.config;

import com.xm.cpsmall.comm.mq.handler.MessageHandler;
import com.xm.cpsmall.comm.mq.handler.MessageManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * 配置jackson序列化
 */
@Slf4j
@Configuration
public class RabbitMqConfig {

    @Autowired
    private SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory;

    @PostConstruct
    public void initSimpleRabbitListenerContainerFactory(){
        //忽略jackson 序列化时对属性的强匹配
        simpleRabbitListenerContainerFactory.setMessageConverter(new Jackson2JsonMessageConverter("com.xm"));
    }
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter("com.xm"));
        return rabbitTemplate;
    }

    @Bean
    public MessageManager initMqHandler(WebApplicationContext webApplicationContext){
        Map<String, MessageHandler> handlerMaps = webApplicationContext.getBeansOfType(MessageHandler.class);
        MessageManager messageManager = new MessageManager();
        handlerMaps.values().stream().forEach(o -> messageManager.add(o));
        return messageManager;
    }
}
