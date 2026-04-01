package com.harshkumar0614jain.worksphere.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResourceAlreadyExistsException extends RuntimeException{
    private String key;
    private String message;

}
