package com.withus.be.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto {
    @NotNull
    private String email;
    @NotNull
    private String certNum;
}
