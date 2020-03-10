package pl.coderslab.charity.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.coderslab.charity.exceptions.CategoryNotFoundException;
import pl.coderslab.charity.model.Category;
import pl.coderslab.charity.repository.CategoryRepository;
import pl.coderslab.charity.service.CategoryService;

import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Set<Category> getAll() {
        return new HashSet<>(categoryRepository.findAll());
    }

    @Override
    public Category getById(long id) {
        return categoryRepository.findById(id).orElseThrow(() -> {
            log.warn("IN getById(long id): category with id {} not founded", id);
            return new CategoryNotFoundException(id);
        });
    }
}
