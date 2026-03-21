package com.vikku.emailservice.service;

import com.vikku.emailservice.model.dto.request.EmailRequest;
import com.vikku.emailservice.model.dto.response.EmailResponse;

public interface EmailService {
    EmailResponse sendEmail(EmailRequest emailRequest);
}
