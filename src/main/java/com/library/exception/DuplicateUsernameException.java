// src/main/java/com/library/exception/DuplicateUsernameException.java
package com.library.exception;

public class DuplicateUsernameException extends Exception {
    public DuplicateUsernameException(String message) {
        super(message);
    }
}