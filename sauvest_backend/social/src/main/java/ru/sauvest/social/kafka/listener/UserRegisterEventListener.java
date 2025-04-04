//package ru.sauvest.social.kafka.listener;
//
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//import ru.sauvest.social.dto.UserDto;
//import ru.sauvest.social.service.UserService;
//
//import static ru.sauvest.social.kafka.constant.SausocialTopicPropertyNameConstants.USER_REGISTER_EVENT_TOPIC;
//
//@Component
//@RequiredArgsConstructor
//public class UserRegisterEventListener {
//
//    @Autowired
//    private UserService userService;
//
//    @KafkaListener(
//            topics = {
//                    "${" + USER_REGISTER_EVENT_TOPIC + "}"
//            },
//            containerFactory = "userRegisterEventConcurrentKafkaListenerContainerFactory"
//    )
//    public void listenUserRegisterEvent(ru.sauvest.kafka.avro.model.UserRegisterEvent.UserRegisterEvent userRegisterEvent) {
//        UserDto userDto = UserDto.builder()
//                .username(userRegisterEvent.getUsername())
//                .email(userRegisterEvent.getEmail())
//                .ssoToken(userRegisterEvent.getSsoToken())
//                .build();
//        userService.save(userDto);
//    }
//
//}
