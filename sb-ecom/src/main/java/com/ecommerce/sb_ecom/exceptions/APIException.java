package com.ecommerce.sb_ecom.exceptions;

public class APIException extends RuntimeException
{
    String resourceName;
    String fieldName;
    String field;
    Long fieldId;


    public APIException(String message) {
        super(message);

    }
}
