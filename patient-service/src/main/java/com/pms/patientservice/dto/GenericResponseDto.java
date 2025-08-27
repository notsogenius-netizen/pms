package com.pms.patientservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenericResponseDto<T> {

    private String status;
    private T data;

    public static <T> GenericResponseDto<T> success(T data) {return new GenericResponseDto<>("success", data);}

    public static <T> GenericResponseDto<T> failure(T message) {return new GenericResponseDto<>("error", message);}
}
