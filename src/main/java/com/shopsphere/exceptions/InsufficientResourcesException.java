package com.shopsphere.exceptions;

public class InsufficientResourcesException extends RuntimeException {

    protected String resourceName;
    protected String resourceType;
    protected Integer quantity;

    public InsufficientResourcesException(String resourceName, String resourceType, Integer quantity) {
        super(String.format("%s %s contains only %d of resources in the inventory", resourceName, resourceType,
                quantity));
        this.quantity = quantity;
        this.resourceName = resourceName;
        this.resourceType = resourceType;
    }
}
