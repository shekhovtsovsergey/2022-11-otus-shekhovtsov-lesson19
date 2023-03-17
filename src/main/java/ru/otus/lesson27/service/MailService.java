package ru.otus.lesson27.service;

public interface MailService {

    public void sendMessage(String to, String subject, String text);

}
