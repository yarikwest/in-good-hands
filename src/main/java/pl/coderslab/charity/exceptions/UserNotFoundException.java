package pl.coderslab.charity.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
    }

    public UserNotFoundException(String message) {
        super("User with " + message + " not exists");
    }
}
