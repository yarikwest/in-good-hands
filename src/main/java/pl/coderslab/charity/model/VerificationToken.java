package pl.coderslab.charity.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "verification_token")
public class VerificationToken {
    private static final long EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String token;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    User user;

    LocalDateTime expiryDate;

    @PrePersist
    private void calculateExpiryDate() {
        expiryDate = LocalDateTime.now().plusMinutes(EXPIRATION);
    }
}
