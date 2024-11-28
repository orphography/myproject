package ru.roh.springdemo.utils;

public class NotUpdateException extends RuntimeException {
    public NotUpdateException(String message) {
        super(message);
    }
}
