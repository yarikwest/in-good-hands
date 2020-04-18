package pl.coderslab.charity.exceptions;

public class InstitutionIsNullException extends RuntimeException {
    public InstitutionIsNullException() {
        super("Institution is null");
    }
}
