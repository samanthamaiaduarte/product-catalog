package com.samanthamaiaduarte.catalog.exception;

public class CategoryNotFoundException extends RuntimeException{
    public CategoryNotFoundException() { super("Category not found."); }

    public CategoryNotFoundException(String message) { super(message); }
}
