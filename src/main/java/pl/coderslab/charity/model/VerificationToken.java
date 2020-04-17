package pl.coderslab.charity.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor(staticName = "of")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "verification_tokens")
public class VerificationToken extends BaseEntity {
    private static final long EXPIRATION = 60 * 24;

    @NonNull String token;

    @ManyToOne
    @NonNull User user;

    LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(EXPIRATION);
}
