package com.khoa.iot.backend.controller;

import com.khoa.iot.backend.enums.DefaultErrorType;
import com.khoa.iot.backend.exception.ApiRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandlerController {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiRuntimeException internalServerError(HttpServletRequest request, Exception e) {
        this.printLog(request, e, true);

        return new ApiRuntimeException(DefaultErrorType.HTTP_500);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class, MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiRuntimeException badRequest(HttpServletRequest request, Exception e) {
        this.printLog(request, e, false);
        return new ApiRuntimeException(DefaultErrorType.HTTP_400);
    }

    @ExceptionHandler({NoHandlerFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiRuntimeException notFound(HttpServletRequest request, Exception e) {
        this.printLog(request, e, false);
        return new ApiRuntimeException(DefaultErrorType.HTTP_404);
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class, HttpMediaTypeNotSupportedException.class})
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ApiRuntimeException unsupportedMediaType(HttpServletRequest request, Exception e) {
        this.printLog(request, e, false);
        return new ApiRuntimeException(DefaultErrorType.HTTP_415);
    }

    @ExceptionHandler({ApiRuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiRuntimeException apiRuntimeException(HttpServletRequest request, ApiRuntimeException e) {
        if (e.getResultCode() == DefaultErrorType.HTTP_404.getCode()) {
            return this.notFound(request, e);
        } else if (e.getResultCode() == DefaultErrorType.HTTP_400.getCode()) {
            return this.badRequest(request, e);
        } else {
            return e;
        }
    }

    private void printLog(HttpServletRequest request, Exception e, boolean isPrintStacktrace) {
        if (isPrintStacktrace) {
            log.error("method => {} || url => {} || e => {}",
                    request.getMethod(), request.getRequestURI(), e);
        } else {
            log.error("method => {} || url => {}",
                    request.getMethod(), request.getRequestURI());
        }
    }
}