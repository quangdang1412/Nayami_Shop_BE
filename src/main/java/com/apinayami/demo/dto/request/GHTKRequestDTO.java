package com.apinayami.demo.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GHTKRequestDTO {
    @JsonProperty("province")
    @NotBlank(message = "Province is required")
    @Size(max = 100, message = "Province must not exceed 100 characters")
    @Pattern(regexp = "^[\\p{L}\\s]+$", message = "Province must contain only letters and spaces")
    private String province;

    @JsonProperty("district")
    @NotBlank(message = "District is required")
    @Size(max = 100, message = "District must not exceed 100 characters")
    @Pattern(regexp = "^[\\p{L}\\s]+$", message = "District must contain only letters and spaces")
    private String district;

    @JsonProperty("ward")
    @Size(max = 100, message = "Ward must not exceed 100 characters")
    @Pattern(regexp = "^[\\p{L}\\s]*$", message = "Ward must contain only letters and spaces")
    private String ward;

    @JsonProperty("address")
    @NotBlank(message = "Address is required")
    @Size(max = 255, message = "Address must not exceed 255 characters")
    @Pattern(regexp = "^[\\p{L}\\p{N}\\s,./\\-]+$", message = "Address contains invalid characters")
    private String address;

    @JsonProperty("phone")
    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^[0-9]{10,11}$", message = "Phone must be 10-11 digits")
    private String phone;

    @JsonProperty("name")
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    @Pattern(regexp = "^[\\p{L}\\s]+$", message = "Name must contain only letters and spaces")
    private String name;
}
