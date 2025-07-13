package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExceptionTest {
    @Test
    void testNotFoundException() {
        String message = "Entity not found";
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            throw new NotFoundException(message);
        });

        assertEquals(message, exception.getMessage());
    }

    @Test
    void testValidationException() {
        String message = "Validation failed";
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            throw new ValidationException(message);
        });

        assertEquals(message, exception.getMessage());
    }

    @Test
    void testEmailAlreadyExistsException() {
        String message = "Email already exists";
        EmailAlreadyExistsException exception = assertThrows(EmailAlreadyExistsException.class, () -> {
            throw new EmailAlreadyExistsException(message);
        });

        assertEquals(message, exception.getMessage());
    }
}
