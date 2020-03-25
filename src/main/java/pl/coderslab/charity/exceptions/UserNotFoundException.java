package pl.coderslab.charity.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityNotFoundException;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException() {
        super("User not found");
    }

    public UserNotFoundException(long id) {
        super("User with id " + id + " not exists");
    }
}
