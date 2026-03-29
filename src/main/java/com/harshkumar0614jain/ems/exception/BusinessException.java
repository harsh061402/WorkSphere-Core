package com.harshkumar0614jain.ems.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class BusinessException extends RuntimeException {
    private String key;
    private String message;
}
