package ru.itis.services.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.itis.dto.UserDto;
import ru.itis.models.User;
import ru.itis.services.interfaces.MailService;
import ru.itis.services.interfaces.TemplateDrawer;

import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class MailServiceImpl implements MailService {
    private static final String CONFIRM_URL = "http://localhost:8080/confirm/";
    private final JavaMailSender sender;
    private final TemplateDrawer templateDrawer;

    private ExecutorService executorService = Executors.newCachedThreadPool();

    public MailServiceImpl(JavaMailSender sender, TemplateDrawer templateDrawer) {
        this.sender = sender;
        this.templateDrawer = templateDrawer;
    }

    @Value("${mail.username}")
    private String FROM;

    @Override
    public void sendEmailConfirmationLink(User user) {
        String mailText = CONFIRM_URL + user.getConfirmLink();

        Map<String, Object> model = new HashMap<>();
        model.put("user", user);
        model.put("link", mailText);

        String templateAsString = templateDrawer.getPageAsString("mail", model);
        execSendTemplate(user.getEmail(), "emai confirmation", templateAsString);
    }

    private void execSendTemplate(String email, String subject, String text) {
        executorService.submit(() -> {
            MimeMessage message = sender.createMimeMessage();
            try {
                MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
                messageHelper.setTo(email);
                messageHelper.setSubject(subject);
                messageHelper.setText(text, true);
                messageHelper.setFrom(FROM);
            } catch (javax.mail.MessagingException e) {
                throw new IllegalArgumentException(e);
            }
            sender.send(message);
        });
    }
}
