package ru.sauvest.social.kafka.config;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.CommonLoggingErrorHandler;
import ru.sauvest.social.kafka.config.properties.KafkaAuthProperties;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(value = KafkaAuthProperties.class)
public class KafkaConfig {

    @Autowired
    private KafkaAuthProperties kafkaAuthProperties;

    @Bean
    public ConsumerFactory<String, ru.sauvest.kafka.avro.model.UserRegisterEvent.UserRegisterEvent> userRegisterEventConsumerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> config = new HashMap<>(kafkaProperties.buildConsumerProperties());
        config.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaAuthProperties.getGroupId());
        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ru.sauvest.kafka.avro.model.UserRegisterEvent.UserRegisterEvent> userRegisterEventConcurrentKafkaListenerContainerFactory(
            ConsumerFactory<String, ru.sauvest.kafka.avro.model.UserRegisterEvent.UserRegisterEvent> avroConsumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, ru.sauvest.kafka.avro.model.UserRegisterEvent.UserRegisterEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(avroConsumerFactory);
        factory.setConcurrency(1);
        factory.setCommonErrorHandler(new CommonLoggingErrorHandler());
        return factory;
    }

}
