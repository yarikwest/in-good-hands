package pl.coderslab.charity.exceptions;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@ControllerAdvice
class MyExceptionHandler {

    private final MessageSource messageSource;

    MyExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ModelAndView handle404Error(EntityNotFoundException e) {
        return new ModelAndView("error/404", "errorMsg", e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handle500Error(RuntimeException e) {
        return new ModelAndView("error/500", "errorMsg", e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleError(RuntimeException e, HttpServletRequest request) {
        Locale locale = request.getLocale();
        String errorMsg = e.getMessage();

        switch (e.getMessage()) {
            case "Token not found":
                errorMsg = messageSource.getMessage("auth.message.invalidToken", null, locale);
                break;
            case "Token expired":
                errorMsg = messageSource.getMessage("auth.message.tokenExpired", null, locale);
                break;
        }
        return new ModelAndView("error/custom", "errorMsg", errorMsg);
    }
}
