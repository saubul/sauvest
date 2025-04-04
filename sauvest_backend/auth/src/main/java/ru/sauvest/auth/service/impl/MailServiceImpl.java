package ru.sauvest.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.sauvest.auth.dto.NotificationEmailDto;
import ru.sauvest.auth.service.MailService;
import ru.sauvest.baseservices.exception.SauvestException;
import ru.sauvest.baseservices.service.FormatUtilService;

import static ru.sauvest.baseservices.exception.ExceptionCodeConstants.SEND_MAIL_ERROR;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    @Async
    public void sendMail(NotificationEmailDto notificationEmailDto) {
        MimeMessagePreparator mimeMessagePreparator = (mimeMessage) -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(notificationEmailDto.getRecipient());
            mimeMessageHelper.setSubject(notificationEmailDto.getSubject());
            mimeMessageHelper.setText(notificationEmailDto.getBody());
        };
        try {
            javaMailSender.send(mimeMessagePreparator);
        } catch (MailException e) {
            throw new SauvestException(SEND_MAIL_ERROR,
                    String.format("При попытке отправить сообщение на почту \"%s\" произошла ошибка: %s",
                            notificationEmailDto.getRecipient(), FormatUtilService.getNonNullExceptionMessage(e)),
                    e);
        }

    }
}
