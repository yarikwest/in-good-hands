package pl.coderslab.charity.exceptions;

public class UserIsNullException extends RuntimeException {
    public UserIsNullException() {
        super("User is null");
    }
}
