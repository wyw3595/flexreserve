package com.flexreserve.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private Integer code;
    private String message;
    private T data;
    public static <T> Result<T> success(T data) {
        return new Result<T>(200, "success", data);
    }
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<T>(code, message, null);
    }
}
