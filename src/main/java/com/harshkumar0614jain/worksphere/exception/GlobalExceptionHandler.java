package com.harshkumar0614jain.worksphere.exception;

import com.harshkumar0614jain.worksphere.model.ExceptionResponseModel;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tools.jackson.databind.exc.InvalidFormatException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponseModel> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {

        Throwable cause = e.getCause();
        if (cause instanceof InvalidFormatException invalidFormatException) {

            Class<?> targetType = invalidFormatException.getTargetType();

            if (targetType.isEnum()) {
                Object[] enumConstants = targetType.getEnumConstants();

                String message = "Invalid value. Allowed values are: " +
                        Arrays.toString(enumConstants);

                // Enum name
                String enumName = targetType.getSimpleName();

                Map<String,String> error = new HashMap<>();
                error.put(enumName, "Given Value is invalid");
                ExceptionResponseModel response = new ExceptionResponseModel(message,error);

                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        }
        Map<String,String> error = new HashMap<>();
        error.put("message" , "Malformed JSON request");
        ExceptionResponseModel response = new ExceptionResponseModel("Malformed JSON request",error);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<ExceptionResponseModel> handleHttpMessageConversionException(HttpMessageConversionException ex) {
        Map<String,String> error = new HashMap<>();
        error.put("exceptionClass" , ex.getClass().getName());
        error.put("message","Invalid request body: " + ex.getMostSpecificCause().getMessage());
        ExceptionResponseModel  response  = new ExceptionResponseModel(
                "Invalid request body",error);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponseModel> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        Map<String,String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(
                error -> errors.put(error.getField(), error.getDefaultMessage()));

        ExceptionResponseModel response = new ExceptionResponseModel("Validation failed",errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ExceptionResponseModel> handleDuplicateKeyException(DuplicateKeyException e) {

        Map<String, String> error = new HashMap<>();
        error.put("message", "In the given data, constraint have duplicate key value");
        ExceptionResponseModel response = new ExceptionResponseModel(
                "Unique constraint violation",error);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponseModel> handleResourceNotFoundException(ResourceNotFoundException e) {
        Map<String,String> error = new HashMap<>();
        error.put(e.getKey(), e.getMessage());
        ExceptionResponseModel response = new ExceptionResponseModel("Data not found",error);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponseModel> handleResourceAlreadyExistsException(
            ResourceAlreadyExistsException e) {
        Map<String,String> error = new HashMap<>();
        error.put(e.getKey(),e.getMessage());
        ExceptionResponseModel response = new ExceptionResponseModel("Duplicate Entry Value",error);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }


    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ExceptionResponseModel> handleBusinessException(BusinessException e) {
        Map<String,String> error = new HashMap<>();
        error.put(e.getKey(),e.getMessage());
        ExceptionResponseModel response = new ExceptionResponseModel("Business rule violation",error);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponseModel> handleIllegalArgumentException(IllegalArgumentException e) {
        Map<String,String> error = new HashMap<>();
        error.put("error",e.getMessage() );
        ExceptionResponseModel response = new ExceptionResponseModel(e.getMessage(),error);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponseModel> handleRuntimeException(RuntimeException e) {
        Map<String,String> error = new HashMap<>();
        error.put("error",e.getMessage());
        ExceptionResponseModel response = new ExceptionResponseModel(
                "Request could not be processed",error);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseModel> handleException(Exception e) {
        Map<String,String> error = new HashMap<>();
        error.put("exceptionClass" , e.getClass().getName());
        ExceptionResponseModel response  = new ExceptionResponseModel(
                "Something Went Wrong",error);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
