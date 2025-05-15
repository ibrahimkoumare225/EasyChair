package fr.upsaclay.easychair.service.impl;

import fr.upsaclay.easychair.model.Conference;
import fr.upsaclay.easychair.model.User;
import fr.upsaclay.easychair.service.ConferenceService;
import fr.upsaclay.easychair.service.MailSendingService;
import fr.upsaclay.easychair.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class MailSendingServiceImpl implements MailSendingService {

    private final JavaMailSender mailSender;

    private final ConferenceService conferenceService;

    private final UserService userService;

    @Override
    public void sendMailforPhase(Long ConferenceId) {
        Optional<Conference> conference = conferenceService.findOne(ConferenceId);
        if (conference.isEmpty()) {
            throw new IllegalArgumentException(" bad conference Id " + ConferenceId);
        }

        String message =   "The conference " + conference.get().getTitle()
                + " turn to phase " + conference.get().getPhase().toString() +" !";
        String title = "Phase " +conference.get().getPhase().toString()+ " for Conference " + conference.get().getTitle();

        List<User> users = userService.findByConferenceId(ConferenceId);
        for (User user : users) {
            String customMsg = "Hello "+ user.getFirstName() + " " + user.getLastName()+ "!"+message;
            sendMail(user.getEmail(),title,customMsg);
        }
    }

    @Override
    public void sendMail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("mychairoff@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}

