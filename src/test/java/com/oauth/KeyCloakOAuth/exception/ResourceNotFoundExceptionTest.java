package com.oauth.KeyCloakOAuth.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.junit.jupiter.api.Assertions.*;

public class ResourceNotFoundExceptionTest {

    @Test
    public void testConstructorWithMessage() {
        String message = "Resource not found";
        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause()); // No cause should be set
    }

    @Test
    public void testExceptionInheritance() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Test");

        assertInstanceOf(RuntimeException.class, exception);
        assertInstanceOf(Exception.class, exception);
        assertInstanceOf(Throwable.class, exception);
    }

    @Test
    public void testResponseStatusAnnotation() {
        Class<ResourceNotFoundException> exceptionClass = ResourceNotFoundException.class;
        ResponseStatus annotation = exceptionClass.getAnnotation(ResponseStatus.class);

        assertNotNull(annotation, "ResponseStatus annotation should be present");
        assertEquals(HttpStatus.NOT_FOUND, annotation.value(), "Status should be NOT_FOUND");
    }

    @Test
    public void testExceptionWithCause() {
        String message = "Resource not found";
        Throwable cause = new IllegalArgumentException("Cause");
        ResourceNotFoundException exception = new ResourceNotFoundException(message) {
            // Anonymous subclass to test cause (since base class doesn't have a cause constructor)
            {
                initCause(cause);
            }
        };

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}