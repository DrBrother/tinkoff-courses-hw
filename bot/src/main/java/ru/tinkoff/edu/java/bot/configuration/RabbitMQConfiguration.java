package ru.tinkoff.edu.java.bot.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue scrapperQueue(ApplicationProperties config) {
        return QueueBuilder.durable(config.queueName())
                .withArgument("x-dead-letter-exchange", deadLetterExchangeName(config.queueName()))
                .build();
    }

    @Bean
    public DirectExchange scrapperExchange(ApplicationProperties config) {
        return new DirectExchange(config.exchangeName());
    }

    @Bean
    public Binding bindingScrapper(Queue scrapperQueue, DirectExchange scrapperExchange) {
        return BindingBuilder.bind(scrapperQueue).to(scrapperExchange).withQueueName();
    }

    @Bean
    public Queue deadLetterQueue(ApplicationProperties config) {
        return QueueBuilder.durable(deadLetterQueueName(config.queueName())).build();
    }

    @Bean
    public FanoutExchange deadLetterExchange(ApplicationProperties config) {
        return new FanoutExchange(deadLetterExchangeName(config.queueName()));
    }

    @Bean
    public Binding deadLetterBinding(Queue deadLetterQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(deadLetterQueue).to(fanoutExchange);
    }

    private static String deadLetterQueueName(String queueName) {
        return queueName.concat(".dlq");
    }

    private static String deadLetterExchangeName(String exchangeName) {
        return exchangeName.concat(".dlx");
    }

}
