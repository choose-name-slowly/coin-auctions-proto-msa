package com.nameslowly.coinauctions.coinpay.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoinPayApplicationQueueConfig {
    @Value("${message.exchange}")
    private String exchange;
    @Value("${message.queue.bid-cancel}")
    private String queueBidCancel;
    @Value("app.coinpay.history")
    private String queueCoinHistory;
    @Bean
    public TopicExchange exchange() { return new TopicExchange(exchange); }
    @Bean public Queue queueBidCancel() { return new Queue(queueBidCancel); }
    @Bean public Queue queueCoinHistory() { return new Queue(queueCoinHistory); }
    @Bean public Binding bindingBidCancel() { return BindingBuilder.bind(queueBidCancel()).to(exchange()).with(queueBidCancel); }
    @Bean public Binding bindingCoinHistory() {return BindingBuilder.bind(queueCoinHistory()).to(exchange()).with(queueCoinHistory); }
    @Bean
    public RabbitTemplate rabbitTemplate(org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        // JSON 형식의 메시지를 직렬화하고 역직렬할 수 있도록 설정
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    /**
     * Jackson 라이브러리를 사용하여 메시지를 JSON 형식으로 변환하는 MessageConverter 빈을 생성
     *
     * @return MessageConverter 객체
     */
    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
