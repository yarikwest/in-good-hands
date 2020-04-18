package pl.coderslab.charity.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.coderslab.charity.exceptions.InstitutionIsNullException;
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

    public Institution create(Institution institution) {
        if (institution == null) {
            throw new InstitutionIsNullException();
        }
        return institutionRepository.save(institution);
    }

    public Institution update(long id, Institution institution) {
        if (!institutionRepository.existsById(id)) {
            log.warn("IN update(): institution with id {} not exists", id);
            throw new InstitutionNotFoundException(id);
        }
        return institutionRepository.save(institution);
    }

    public void delete(long id) {
        if (!institutionRepository.existsById(id)) {
            log.warn("IN delete(): institution with id {} not exists", id);
            throw new InstitutionNotFoundException(id);
        }
        institutionRepository.deleteById(id);
    }

    public long getCountAll() {
        return institutionRepository.count();
    }
}
