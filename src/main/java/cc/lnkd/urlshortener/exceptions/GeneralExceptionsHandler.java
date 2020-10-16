package cc.lnkd.urlshortener.exceptions;

import cc.lnkd.urlshortener.models.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
public class GeneralExceptionsHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<APIResponse> handleOrderBadRequestException(Exception ex) {
        APIResponse error = new APIResponse();
        error.setMessage(ex.getMessage());
        error.setData(null);
        error.setStatus(false);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public void sqlExceptionHandler(Exception ex) {
        System.out.println(ex.getMessage());
        //Catch SQL Insert Constraints and do nothing
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse> exceptionHandler(Exception ex) {
        APIResponse error = new APIResponse();
        ex.printStackTrace();
        error.setStatus(false);
        error.setMessage(ex.getMessage());
        error.setData(null);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
