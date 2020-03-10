package pl.coderslab.charity.exceptions;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException() {
    }

    public CategoryNotFoundException(long id) {
        super("Category with id " + id + " not exists");
    }
}
