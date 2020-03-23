package pl.coderslab.charity.exceptions;

public class InstitutionNotFoundException extends RuntimeException {
    public InstitutionNotFoundException() {
    }

    public InstitutionNotFoundException(long id) {
        super("Institution with id " + id + " not exists");
    }
}
