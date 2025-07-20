package com.samanthamaiaduarte.catalog.exception;

public class CategoryHasProductsException extends RuntimeException{
    public CategoryHasProductsException() { super("Unable to delete a category with linked products."); }

    public CategoryHasProductsException(String message) { super(message); }
}
