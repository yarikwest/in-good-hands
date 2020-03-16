package pl.coderslab.charity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.coderslab.charity.model.Role;
import pl.coderslab.charity.model.User;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("select u from User u where ?1 member of u.roles")
    Set<User> findAllByRole(Role role);

    @Query("select count(u) from User u where ?1 member of u.roles")
    long countAllByRole(Role role);

    @Query("select count(u) from User u where ?1 member of u.roles and u.created > ?2")
    long countAllByRoleAndCreatedFromLastMonth(Role role, LocalDateTime period);
}
