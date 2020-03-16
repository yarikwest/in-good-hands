package pl.coderslab.charity.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.coderslab.charity.exceptions.DonationNotFoundException;
import pl.coderslab.charity.model.Donation;
import pl.coderslab.charity.model.DonationStatus;
import pl.coderslab.charity.repository.DonationRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class DonationService {
    private final DonationRepository donationRepository;

    DonationService(DonationRepository donationRepository) {
        this.donationRepository = donationRepository;
    }

    public Set<Donation> getAll() {
        return new HashSet<>(donationRepository.findAll());
    }

    public Donation getById(long id) {
        return donationRepository.findById(id).orElseThrow(() -> {
            log.warn("IN getById(id): donation with id {} not founded", id);
            return new DonationNotFoundException(id);
        });
    }

    public Donation create(Donation donation) {
        return donationRepository.save(donation);
    }

    public long getCountAllDonations() {
        return donationRepository.count();
    }

    public long getSumOfAllPackage() {
        return donationRepository.sumAllQuantity();
    }

    public long getCountAllDonationsFromLastMonth() {
        LocalDateTime period = LocalDateTime.now().minusMonths(1);
        return donationRepository.countAllByCreatedGreaterThan(period);
    }

    public long getCountAllPackagesFromLastMonth() {
        LocalDateTime period = LocalDateTime.now().minusMonths(1);
        return donationRepository.sumAllQuantityFromLastMonth(period);
    }

    public long getCountAllSupportedInstitutionsFromLastMonth() {
        LocalDateTime period = LocalDateTime.now().minusMonths(1);
        return donationRepository.countAllGroupByInstitutionAndCreatedGreaterThan(period);
    }

    public Set<Donation> getAllNotReceivedDonations() {
        return donationRepository.getAllByStatusEquals(DonationStatus.MISSED);
    }
}
