package pl.coderslab.charity.exceptions;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
class MyExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public String handleErrors(Throwable e, HttpServletRequest request, Model model) {

        String requestURI = request.getRequestURI();
        model.addAttribute("error", e.getMessage());
        return requestURI;
    }
}
