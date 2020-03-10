package pl.coderslab.charity.service;

import pl.coderslab.charity.model.Category;

import java.util.Set;

public interface CategoryService {
    Set<Category> getAll();

    Category getById(long id);
}
