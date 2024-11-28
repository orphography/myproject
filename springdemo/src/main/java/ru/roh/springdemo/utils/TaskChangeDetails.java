package ru.roh.springdemo.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskChangeDetails {
    private String field;
    private Object previousValue;
    private Object newValue;
}