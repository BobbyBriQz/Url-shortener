package cc.lnkd.urlshortener.exceptions;

import cc.lnkd.urlshortener.models.response.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
public class GeneralExceptionsHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<APIResponse> handleOrderBadRequestException(Exception ex) {
        APIResponse error = new APIResponse(false, ex.getMessage(), null);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public void sqlExceptionHandler(Exception ex) {
        System.out.println(ex.getMessage());
        //Catch SQL Insert Constraints and do nothing
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<APIResponse> nullPointerExceptionHandler(Exception ex) {
        APIResponse error = new APIResponse(false, "Null Pointer Exception", null);
        ex.printStackTrace();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<APIResponse> userNameNotFoundExceptionHandler(Exception ex) {
        APIResponse error = new APIResponse(false, ex.getMessage()+ " not registered", null);
        System.out.println(ex.getMessage() + " Not Found");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<APIResponse> badCredentialsExceptionHandler(Exception ex) {
        APIResponse error = new APIResponse(false, ex.getMessage(), null);
        System.out.println(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse> exceptionHandler(Exception ex) {
        APIResponse error = new APIResponse(false, ex.getMessage(), null);
        System.out.println(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
