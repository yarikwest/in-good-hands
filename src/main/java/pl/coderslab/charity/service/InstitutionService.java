package pl.coderslab.charity.service;

import pl.coderslab.charity.model.Institution;

import java.util.Set;

public interface InstitutionService {
    Set<Institution> getAll();

    Institution getById(long id);
}
