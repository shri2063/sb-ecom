package com.ecommerce.sb_ecom.exceptions;

public class ResourceNotFoundException extends RuntimeException
{
    String resourceName;
    String fieldName;
    String field;
    Long fieldId;


    public ResourceNotFoundException(String resourceName, String fieldName, String field) {
        super(String.format("%s  not found %s : %s",resourceName,fieldName,field ));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.field = field;
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Long fieldId) {
        super(String.format("%s  not found %s : %d",resourceName,fieldName,fieldId ));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldId = fieldId;
    }


    public ResourceNotFoundException()
    {
    }
}
