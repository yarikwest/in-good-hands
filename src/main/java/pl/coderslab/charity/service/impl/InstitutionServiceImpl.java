package pl.coderslab.charity.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.coderslab.charity.exceptions.InstitutionNotFoundException;
import pl.coderslab.charity.model.Institution;
import pl.coderslab.charity.repository.InstitutionRepository;
import pl.coderslab.charity.service.InstitutionService;

import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
class InstitutionServiceImpl implements InstitutionService {
    private final InstitutionRepository institutionRepository;

    InstitutionServiceImpl(InstitutionRepository institutionRepository) {
        this.institutionRepository = institutionRepository;
    }

    @Override
    public Set<Institution> getAll() {
        return new HashSet<>(institutionRepository.findAll());
    }

    @Override
    public Institution getById(long id) {
        return institutionRepository.findById(id).orElseThrow(() -> {
            log.warn("IN getById(id): institution with id {} not founded", id);
            return new InstitutionNotFoundException(id);
        });
    }
}
