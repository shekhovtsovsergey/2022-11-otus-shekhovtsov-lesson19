package ru.otus.lesson27.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendMessage(String to, String subject, String text) {
        SimpleMailMessage message = new  SimpleMailMessage();
        message.setFrom("medappsuppor@rambler.ru");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
