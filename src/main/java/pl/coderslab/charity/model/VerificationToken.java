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
@Table(name = "verification_tokens")
public class VerificationToken extends BaseEntity {
    private static final long EXPIRATION = 60 * 24;

    String token;

    @ManyToOne
    User user;

    LocalDateTime expiryDate;

    @PrePersist
    private void calculateExpiryDate() {
        expiryDate = LocalDateTime.now().plusMinutes(EXPIRATION);
    }
}
