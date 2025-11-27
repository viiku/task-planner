package com.vikku.emailservice.controller;

import com.vikku.emailservice.model.dto.request.EmailRequest;
import com.vikku.emailservice.model.dto.response.EmailResponse;
import com.vikku.emailservice.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<EmailResponse> sendEmail(@Valid @RequestBody EmailRequest emailRequest){
        return  ResponseEntity.ok(emailService.sendEmail(emailRequest));
    }
}
