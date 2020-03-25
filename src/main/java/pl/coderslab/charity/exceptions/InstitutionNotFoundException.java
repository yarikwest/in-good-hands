package pl.coderslab.charity.exceptions;

import javax.persistence.EntityNotFoundException;

public class InstitutionNotFoundException extends EntityNotFoundException {

    public InstitutionNotFoundException(long id) {
        super("Institution with id " + id + " not exists");
    }
}
