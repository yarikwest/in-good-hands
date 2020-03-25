package pl.coderslab.charity.service;

import org.springframework.stereotype.Service;
import pl.coderslab.charity.model.User;
import pl.coderslab.charity.model.VerificationToken;
import pl.coderslab.charity.repository.VerificationTokenRepository;

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
        return tokenRepository.findByToken(token);
    }
}
