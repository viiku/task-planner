package com.vikku.emailservice.model.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailResponse {

    private String  recipient;
    private String subject;
    private String  body;
    private String error;
}
