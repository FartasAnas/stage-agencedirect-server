package stage.agencedirectserver.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice @Slf4j
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<CustomErrorResponse> NotFoundExceptionsHandler(Exception ex, WebRequest request) {
        CustomErrorResponse errors = new CustomErrorResponse();
        errors.setTimestamp(LocalDateTime.now());
        errors.setError(HttpStatus.NOT_FOUND.name().replace("_"," "));
        errors.setMessage(ex.getMessage());
        errors.setStatus(HttpStatus.NOT_FOUND.value());
        errors.setPath(request.getDescription(false).replace("uri=",""));
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(PropertyValueException.class)
    public final ResponseEntity<CustomErrorResponse> notNullExceptionsHandler(Exception ex, WebRequest request) {
        CustomErrorResponse errors = new CustomErrorResponse();
        errors.setTimestamp(LocalDateTime.now());
        errors.setError(HttpStatus.BAD_REQUEST.name().replace("_", " "));
        errors.setMessage(ex.getMessage());
        errors.setPath(request.getDescription(false).replace("uri=",""));
        errors.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public final ResponseEntity<CustomErrorResponse> conflictExceptionsHandler(Exception ex, WebRequest request) {
        if(!ex.getMessage().replace("PropertyValueException","").equals(ex.getMessage())) {
            return notNullExceptionsHandler(ex,request);
        }
        CustomErrorResponse errors = new CustomErrorResponse();
        errors.setTimestamp(LocalDateTime.now());
        errors.setError(HttpStatus.CONFLICT.name());
        errors.setMessage(ex.getMessage());
        errors.setPath(request.getDescription(false).replace("uri=",""));
        errors.setStatus(HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(errors, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(EmailNotValidException.class)
    public final ResponseEntity<CustomErrorResponse> emailNotValidExceptionsHandler(Exception ex, WebRequest request) {
        CustomErrorResponse errors = new CustomErrorResponse();
        errors.setTimestamp(LocalDateTime.now());
        errors.setError(HttpStatus.NOT_ACCEPTABLE.name().replace("_"," "));
        errors.setMessage(ex.getMessage());
        errors.setPath(request.getDescription(false).replace("uri=",""));
        errors.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
        return new ResponseEntity<>(errors, HttpStatus.NOT_ACCEPTABLE);
    }
    @ExceptionHandler(InvalidDefinitionException.class)
    public final ResponseEntity<CustomErrorResponse> InvalidExceptionsHandler(Exception ex, WebRequest request) {
        CustomErrorResponse errors = new CustomErrorResponse();
        errors.setTimestamp(LocalDateTime.now());
        errors.setError(HttpStatus.METHOD_NOT_ALLOWED.name().replace("_"," "));
        errors.setMessage(ex.getMessage());
        errors.setPath(request.getDescription(false).replace("uri=",""));
        errors.setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
        return new ResponseEntity<>(errors, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<CustomErrorResponse> AllExceptionsHandler(Exception ex, WebRequest request) {
        if(!ex.getMessage().replace("InvalidDefinitionException","").equals(ex.getMessage())) {
            return InvalidExceptionsHandler(ex,request);
        }
        CustomErrorResponse errors = new CustomErrorResponse();
        errors.setTimestamp(LocalDateTime.now());
        errors.setError(HttpStatus.INTERNAL_SERVER_ERROR.name().replace("_"," "));
        errors.setMessage(ex.getMessage());
        errors.setPath(request.getDescription(false).replace("uri=",""));
        errors.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
