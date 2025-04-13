package com.apinayami.demo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class DashBoardDateDTO implements Serializable {
    @NotNull(message = "Start date cannot be null")
    private LocalDate startDate;
    @NotNull(message = "End date cannot be null")
    private LocalDate endDate;
}
