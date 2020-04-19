package pl.coderslab.charity.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.coderslab.charity.exceptions.DonationNotFoundException;
import pl.coderslab.charity.model.Donation;
import pl.coderslab.charity.model.DonationStatus;
import pl.coderslab.charity.model.User;
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

    public long getCountAllDonationsByUser(User user) {
        return donationRepository.countAllByUser(user);
    }

    public long getSumOfAllPackageByUser(User user) {
        return donationRepository.sumAllQuantityByUser(user);
    }

    public long getCountAllSupportedInstitutionsByUser(User user) {
        return donationRepository.countAllByUserIdGroupByInstitution(user.getId());
    }

    public Set<Donation> getAllByUser(User user) {
        return donationRepository.findAllByUser(user);
    }

    public Page<Donation> getAllByUserSorted(User user, Pageable pageable) {
        return donationRepository.findAllByUser(user, pageable);
    }
}
