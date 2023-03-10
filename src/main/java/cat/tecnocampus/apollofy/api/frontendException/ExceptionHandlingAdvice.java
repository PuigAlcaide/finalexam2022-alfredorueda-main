package cat.tecnocampus.apollofy.api.frontendException;

import cat.tecnocampus.apollofy.application.exceptions.ElementNotFoundInBBDD;
import cat.tecnocampus.apollofy.application.exceptions.NotAuthorizedToDoAction;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionHandlingAdvice {

    @ResponseBody
    @ExceptionHandler({ElementNotFoundInBBDD.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String objectNotFoundHandler(Exception ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler({NotAuthorizedToDoAction.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String notAuthorizedHandler(Exception ex) {
        return ex.getMessage();
    }
}
