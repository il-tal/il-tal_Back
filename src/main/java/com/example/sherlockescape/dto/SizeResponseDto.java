package com.example.sherlockescape.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SizeResponseDto<T,V> {
    private boolean success;
    private T size;
    private V data;
    private Error error;

    public static <T,V> SizeResponseDto<T,V> success(T size, V data) {
        return new SizeResponseDto<>(true, size, data, null);
    }

    public static <T,V> SizeResponseDto<T,V> fail(Integer httpStatus, String message, String detail){
        return new SizeResponseDto<>(false, null, null, new Error(httpStatus, message, detail));
    }

    @Getter
    @AllArgsConstructor
    static class Error{
        private Integer httpStatus;
        private String message;
        private String detail;
    }
}