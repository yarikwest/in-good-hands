package pl.coderslab.charity.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.coderslab.charity.exceptions.DonationNotFoundException;
import pl.coderslab.charity.model.Donation;
import pl.coderslab.charity.repository.DonationRepository;
import pl.coderslab.charity.service.DonationService;

import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
class DonationServiceImpl implements DonationService {
    private final DonationRepository donationRepository;

    DonationServiceImpl(DonationRepository donationRepository) {
        this.donationRepository = donationRepository;
    }

    @Override
    public Set<Donation> getAll() {
        return new HashSet<>(donationRepository.findAll());
    }

    @Override
    public Donation getById(long id) {
        return donationRepository.findById(id).orElseThrow(() -> {
            log.warn("IN getById(id): donation with id {} not founded", id);
            return new DonationNotFoundException(id);
        });
    }

    @Override
    public Donation create(Donation donation) {
        return donationRepository.save(donation);
    }
}
