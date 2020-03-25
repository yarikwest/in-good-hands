package pl.coderslab.charity.exceptions;

import javax.persistence.EntityExistsException;

public class EmailExistsException extends EntityExistsException {
    public EmailExistsException() {
        super("This email already exists");
    }

}
