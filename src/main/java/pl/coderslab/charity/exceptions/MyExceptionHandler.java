package pl.coderslab.charity.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
class MyExceptionHandler {

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ModelAndView handle404Error(EntityNotFoundException e) {
        return new ModelAndView("error/404", "errorMsg", e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handle500Error(RuntimeException e) {
        return new ModelAndView("error/500", "errorMsg", e.getMessage());
    }
}
