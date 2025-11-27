package com.vikku.emailservice.service.impl;

import com.vikku.emailservice.model.dto.request.EmailRequest;
import com.vikku.emailservice.model.dto.response.EmailResponse;
import com.vikku.emailservice.model.entity.EmailEntity;
import com.vikku.emailservice.repository.EmailRepository;
import com.vikku.emailservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {

    private final EmailRepository emailRepository;
    private final JavaMailSender javaMailSender;

    public EmailResponse sendEmail(EmailRequest emailRequest){
        try{
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

            simpleMailMessage.setTo(emailRequest.getRecipient());
            simpleMailMessage.setSubject(emailRequest.getSubject());
            simpleMailMessage.setText(emailRequest.getBody());
            javaMailSender.send(simpleMailMessage);

            EmailEntity emailToSave = new EmailEntity();
            emailToSave.setRecipient(emailRequest.getRecipient());
            emailToSave.setSubject(emailRequest.getSubject());
            emailToSave.setBody(emailRequest.getBody());
            emailRepository.save(emailToSave);
            return EmailResponse.builder()
                    .recipient(emailRequest.getRecipient())
                    .subject(emailRequest.getSubject())
                    .body(emailRequest.getBody())
                    .build();
        }catch (Exception e){
            return EmailResponse.builder()
                    .recipient(null)
                    .subject(null)
                    .body(null)
                    .error(e.getMessage())
                    .build();
        }
    }
}
