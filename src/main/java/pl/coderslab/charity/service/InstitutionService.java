package pl.coderslab.charity.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.coderslab.charity.exceptions.InstitutionNotFoundException;
import pl.coderslab.charity.model.Institution;
import pl.coderslab.charity.repository.InstitutionRepository;

import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class InstitutionService {
    private final InstitutionRepository institutionRepository;

    InstitutionService(InstitutionRepository institutionRepository) {
        this.institutionRepository = institutionRepository;
    }

    public Set<Institution> getAll() {
        return new HashSet<>(institutionRepository.findAll());
    }

    public Institution getById(long id) {
        return institutionRepository.findById(id).orElseThrow(() -> {
            log.warn("IN getById(id): institution with id {} not founded", id);
            return new InstitutionNotFoundException(id);
        });
    }
}
