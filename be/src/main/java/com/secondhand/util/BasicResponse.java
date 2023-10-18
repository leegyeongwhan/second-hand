package com.secondhand.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BasicResponse<T> {

    private int httpStatus;
    private String message;
    private int apiStatus;
    private boolean success;
    private T data;


    public static <T> BasicResponse<T> send(int httpStatus, String message, T memberResponseDTO) {
        return BasicResponse.<T>builder()
                .success(true)
                .message(message)
                .apiStatus(20000)
                .httpStatus(httpStatus)
                .data(memberResponseDTO)
                .build();
    }

    public static <T> BasicResponse<T> send(int httpStatus, String message) {
        return BasicResponse.<T>builder()
                .success(true)
                .message(message)
                .apiStatus(20000)
                .httpStatus(httpStatus)
                .build();
    }
}
