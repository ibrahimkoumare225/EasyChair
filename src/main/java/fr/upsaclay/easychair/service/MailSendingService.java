package fr.upsaclay.easychair.service;

public interface MailSendingService {

    void sendMailforPhase(Long ConferenceId);

    void sendMail(String to, String subject, String body);
}
