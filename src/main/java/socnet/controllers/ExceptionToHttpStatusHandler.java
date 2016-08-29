package socnet.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.NoResultException;


@ControllerAdvice
@ResponseBody
public class WebExceptionHandler {
    private static final Logger LOGGER = LogManager.getLogger(WebExceptionHandler.class);
    
    @ExceptionHandler(NoResultException.class)
    public ResponseEntity handleNoResultException(Throwable e) {
        LOGGER.error(e);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
