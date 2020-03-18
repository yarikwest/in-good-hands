package pl.coderslab.charity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.coderslab.charity.model.Donation;
import pl.coderslab.charity.model.DonationStatus;
import pl.coderslab.charity.model.User;

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

    @Query("select coalesce(sum(d.quantity), 0) from Donation d where d.user = ?1")
    long sumAllQuantityByUser(User user);

    long countAllByUser(User user);

    @Query(value = "select count(*) from (select count(*)from donations d where d.user_id = ?1 " +
            "group by d.institution_id) as dc", nativeQuery = true)
    long countAllByUserIdGroupByInstitution(long userId);

    Set<Donation> findAllByUser(User user);

    Page<Donation> findAllByUser(User user, Pageable pageable);
}
