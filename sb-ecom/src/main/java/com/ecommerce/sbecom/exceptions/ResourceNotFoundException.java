package com.ecommerce.sbecom.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    String resourceName;
    String field;
    String fieldName;
    Long filedId;

    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String resourceName, String fieldName, String field) {
        super(String.format("%s not found with %s: %s", resourceName, fieldName, field));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.field = field;
    }

    public ResourceNotFoundException(String resourceName, String field, Long filedId) {
        super(String.format("%s not found with %s: %d", resourceName, field, filedId));
        this.resourceName = resourceName;
        this.field = field;
        this.filedId = filedId;
    }

    public ResourceNotFoundException(String resourceName) {
        super(String.format("%s are not found or are empty", resourceName));
        this.resourceName = resourceName;
    }
}
