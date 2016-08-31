package socnet.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import java.io.IOException;


@ControllerAdvice
@ResponseBody
public class ExceptionToHttpStatusHandler {
    private static final Logger LOGGER = LogManager.getLogger(ExceptionToHttpStatusHandler.class);
    
    @ExceptionHandler(NoResultException.class)
    public ResponseEntity handleNoResultException(Throwable e) {
        LOGGER.error(e);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    
    @ExceptionHandler({EntityExistsException.class, ConstraintViolationException.class})
    public ResponseEntity handleEntityExistsException(Throwable e) {
        LOGGER.error(e);
        return new ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY);
    }
    
    @ExceptionHandler(EmptyRequestException.class)
    public ResponseEntity handleEmptyRequestException(Throwable e) {
        LOGGER.error(e);
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(IOException.class)
    public ResponseEntity handleIOException(Throwable e) {
        LOGGER.error(e);
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}