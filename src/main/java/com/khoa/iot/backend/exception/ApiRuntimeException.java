package com.khoa.iot.backend.exception;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIncludeProperties({"successful", "resultCode", "resultMessage"})
public class ApiRuntimeException extends RuntimeException {
    private boolean successful;
    private int resultCode;
    private String resultMessage;

    public ApiRuntimeException(ExceptionPassable<? extends Enum<?>> passToException) {
        super(passToException.getMessage());
        this.successful = false;
        this.resultCode = passToException.getCode();
        this.resultMessage = passToException.getMessage();
    }

    public ApiRuntimeException(ExceptionPassable<? extends Enum<?>> passToException, Throwable throwable) {
        super(passToException.getMessage());
        this.successful = false;
        this.resultCode = passToException.getCode();
        this.resultMessage = passToException.getMessage();
    }
}