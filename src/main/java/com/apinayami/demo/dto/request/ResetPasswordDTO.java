package com.apinayami.demo.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResetPasswordDTO {
    @NotBlank
    @Size(min = 6, message = "Mật khẩu phải có độ dài lớn hơn hoặc bằng 6")
    private String newPassword;
}
