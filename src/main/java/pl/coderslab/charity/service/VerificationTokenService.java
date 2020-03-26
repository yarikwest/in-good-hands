package pl.coderslab.charity.service;

import org.springframework.stereotype.Service;
import pl.coderslab.charity.model.User;
import pl.coderslab.charity.model.VerificationToken;
import pl.coderslab.charity.repository.VerificationTokenRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class VerificationTokenService {
    private final VerificationTokenRepository tokenRepository;

    VerificationTokenService(VerificationTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public VerificationToken createVerificationToken(User user) {
        VerificationToken myToken = VerificationToken.of(UUID.randomUUID().toString(), user);
        return tokenRepository.save(myToken);
    }

    public VerificationToken getVerificationToken(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token).orElseThrow(() -> new RuntimeException("Token not found"));

        if (LocalDateTime.now().isAfter(verificationToken.getExpiryDate())) {
            throw new RuntimeException("Token expired");
        }

        return verificationToken;
    }
}
