package ru.practicum.shareit.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import ru.practicum.shareit.exception.handler.ErrorHandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ExceptionTest {
    @InjectMocks
    private ErrorHandler errorHandler;

    @BeforeEach
    void setUp() {
        errorHandler = new ErrorHandler();
    }


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

    @Test
    void handleNotFound_ShouldReturnErrorResponse() {
        NotFoundException exception = new NotFoundException("Entity not found");
        ErrorResponse response = errorHandler.handleNotFound(exception);

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals("Entity not found", response.getMessage());
    }

    @Test
    void handleEmailExists_ShouldReturnErrorResponse() {
        EmailAlreadyExistsException exception = new EmailAlreadyExistsException("Email already exists");
        ErrorResponse response = errorHandler.handleEmailExists(exception);

        assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
        assertEquals("Email already exists", response.getMessage());
    }

    @Test
    void handleAll_ShouldReturnErrorResponse() {
        Exception exception = new Exception("Internal server error");
        ErrorResponse response = errorHandler.handleAll(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
        assertEquals("Internal server error", response.getMessage());
    }
}
