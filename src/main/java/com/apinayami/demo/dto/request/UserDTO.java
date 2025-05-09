package com.apinayami.demo.dto.request;

import com.apinayami.demo.util.Enum.Role;
import com.apinayami.demo.util.Validation.EnumValue;
import com.apinayami.demo.util.Validation.PhoneNumber;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO{
    private Long userId;
    @NotBlank(message = "UserName must be not blank")
    @NotNull
    private String userName;

    @Email(message = "Email invalid format")
    @NotBlank(message = "Email must be not blank")
    @NotNull
    private String email;
//    @NotBlank(message = "Password must be not blank")
    @Size(min = 6, message = "Username must be greater 6 ")
    private String password;
    @PhoneNumber(message = "phone invalid format")
    private String phoneNumber;

    @EnumValue(enumClass = Role.class, message = "Invalid role. Must be ADMIN, STAFF, or USER")
    @NotNull
    private String type;

    private boolean active;
}
