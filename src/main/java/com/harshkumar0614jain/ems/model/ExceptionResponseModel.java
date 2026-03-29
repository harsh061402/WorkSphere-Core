package com.harshkumar0614jain.ems.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;
@Data
@AllArgsConstructor
public class ExceptionResponseModel {
    private String message;
    private Map<String,String> data;
}
