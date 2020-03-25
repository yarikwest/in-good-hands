package pl.coderslab.charity.exceptions;

import javax.persistence.EntityNotFoundException;

public class CategoryNotFoundException extends EntityNotFoundException {

    public CategoryNotFoundException(long id) {
        super("Category with id " + id + " not exists");
    }
}
