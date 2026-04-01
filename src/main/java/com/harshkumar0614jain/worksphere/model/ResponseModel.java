package com.harshkumar0614jain.worksphere.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseModel<T> {
    private String message;
    private T data;
}
