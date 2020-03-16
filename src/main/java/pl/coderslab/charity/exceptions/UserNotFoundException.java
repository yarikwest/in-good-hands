package pl.coderslab.charity.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("User not found");
    }

    public UserNotFoundException(long id) {
        super("User with id " + id + " not exists");
    }
}
