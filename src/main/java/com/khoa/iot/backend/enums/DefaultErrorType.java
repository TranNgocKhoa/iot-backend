package com.khoa.iot.backend.enums;

import com.khoa.iot.backend.exception.ExceptionPassable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DefaultErrorType implements ExceptionPassable<DefaultErrorType> {
    HTTP_400(400, "HTTP 400 error occurred."),
    HTTP_404(404, "HTTP 404 error occurred."),
    HTTP_415(415, "HTTP 415 error occurred."),
    HTTP_500(500, "HTTP 500 error occurred.");

    private final int code;
    private final String message;
}
