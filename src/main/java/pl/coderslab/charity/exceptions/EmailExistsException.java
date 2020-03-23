package pl.coderslab.charity.exceptions;

public class EmailExistsException extends Throwable {
    public EmailExistsException() {
        super("This email already exists");
    }

}
