package ru.sauvest.auth.kafka.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import ru.sauvest.auth.kafka.config.properties.KafkaSocialProperties;
import ru.sauvest.kafka.avro.model.UserRegisterEvent.UserRegisterEvent;

import java.util.concurrent.ExecutionException;

@Component
public class SauvestAuthKafkaProducer {

    @Autowired
    private KafkaSocialProperties kafkaSocialProperties;

    @Autowired
    private KafkaTemplate<String, ru.sauvest.kafka.avro.model.UserRegisterEvent.UserRegisterEvent> userRegisterEventKafkaTemplate;

    public void sendUserRegisterEventAsync(ru.sauvest.kafka.avro.model.UserRegisterEvent.UserRegisterEvent userRegisterEvent) {
        userRegisterEventKafkaTemplate.send(kafkaSocialProperties.getTopics().getUserRegister(), userRegisterEvent);
    }

    public SendResult<String, UserRegisterEvent> sendUserRegisterEventSync(ru.sauvest.kafka.avro.model.UserRegisterEvent.UserRegisterEvent userRegisterEvent) throws ExecutionException, InterruptedException {
        return userRegisterEventKafkaTemplate.send(kafkaSocialProperties.getTopics().getUserRegister(), userRegisterEvent).get();
    }

}
