package com.apinayami.demo.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupDTO {
    @Email(message = "Email invalid format")
    @NotBlank(message = "Email must be not blank")
    @NotNull
    private String email;
    @NotBlank(message = "Password must be not blank")
    @Size(min = 6, message = "Username must be greater 6 ")
    private String password;

    @NotBlank(message="Fullname must be not blank")
    private String fullName;

    @NotBlank(message="Fullname must be not blank")
    private String phoneNumber;
}
