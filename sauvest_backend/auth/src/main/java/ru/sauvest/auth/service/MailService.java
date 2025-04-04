package ru.sauvest.auth.service;

import ru.sauvest.auth.dto.NotificationEmailDto;

public interface MailService {

	void sendMail(NotificationEmailDto notificationEmailDto);
	
}
