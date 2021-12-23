package com.khoa.iot.backend.exception;

public interface ExceptionPassable<E extends Enum<E>> {
    Class<E> getDeclaringClass();

    int getCode();

    String getMessage();
}