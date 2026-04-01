package com.harshkumar0614jain.worksphere.exception;

import lombok.*;

@Getter
@AllArgsConstructor
public class ResourceNotFoundException extends RuntimeException {
    private String key;
    private String message;
}
