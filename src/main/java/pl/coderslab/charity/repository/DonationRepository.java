package pl.coderslab.charity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.coderslab.charity.model.Donation;
import pl.coderslab.charity.model.DonationStatus;

import java.time.LocalDateTime;
import java.util.Set;

public interface DonationRepository extends JpaRepository<Donation, Long> {

    @Query("select coalesce(sum(d.quantity), 0) from Donation d")
    long sumAllQuantity();

    long countAllByCreatedGreaterThan(LocalDateTime period);

    @Query("select coalesce(sum(d.quantity), 0) from Donation d where d.created > ?1")
    long sumAllQuantityFromLastMonth(LocalDateTime period);

    @Query(value = "select count(*) from (select count(*)from donations d where d.created > ?1 " +
            "group by d.institution_id) as dc", nativeQuery = true)
    long countAllGroupByInstitutionAndCreatedGreaterThan(LocalDateTime period);

    Set<Donation> getAllByStatusEquals(DonationStatus status);
}
