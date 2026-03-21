package com.vikku.emailservice.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequest {

    @NotBlank(message = "Recipient is required")
    private String  recipient;

    @NotBlank(message = "Subject or email title is required")
    private String subject;

    @NotBlank(message = "Body of email is required")
    private String  body;
}
