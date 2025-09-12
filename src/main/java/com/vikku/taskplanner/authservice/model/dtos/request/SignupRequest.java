package com.vikku.taskplanner.authservice.model.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Email
    @Size(max = 50)
    private String email;

    @NotBlank
//    @Size(min = 6, max = 40)
    private String password;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Invalid phone number")
    private String phoneNumber;
}
