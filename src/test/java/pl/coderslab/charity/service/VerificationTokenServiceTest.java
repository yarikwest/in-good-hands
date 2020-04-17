package pl.coderslab.charity.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.coderslab.charity.exceptions.UserIsNullException;
import pl.coderslab.charity.model.User;
import pl.coderslab.charity.model.VerificationToken;
import pl.coderslab.charity.repository.VerificationTokenRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class VerificationTokenServiceTest {

    @Mock
    VerificationTokenRepository repository;

    @InjectMocks
    VerificationTokenService service;

    @Test
    void shouldCreateAndReturnVerificationTokenForUser() {
        //given
        given(repository.save(any(VerificationToken.class))).willReturn(new VerificationToken());

        //when
        VerificationToken createdToken = service.createVerificationToken(new User());

        //then
        then(repository).should().save(any(VerificationToken.class));
        assertThat(createdToken).isNotNull();
    }

    @Test
    void ifUserIsNullForCreatingTokenThenThrowException() {
        //when //then
        assertThrows(UserIsNullException.class, () -> service.createVerificationToken(null));
    }

    @Test
    void shouldReturnVerificationToken() {
        //given
        VerificationToken verificationToken = VerificationToken.of("token", new User());
        given(repository.findByToken(anyString())).willReturn(Optional.of(verificationToken));

        //when
        VerificationToken tokenFromDB = service.getVerificationToken(verificationToken.getToken());

        //then
        then(repository).should().findByToken(anyString());
        assertThat(tokenFromDB).isNotNull();
    }

    @Test
    void ifTokenNotFoundThrowException() {
        //given
        given(repository.findByToken(anyString())).willReturn(Optional.empty());

        //when //then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.getVerificationToken("token"));
        assertEquals("Token not found", exception.getMessage());
    }

    @Test
    void ifTokenIsExpiredThenThrowException() {
        //given
        VerificationToken verificationToken = VerificationToken.of("token", new User());
        verificationToken.setExpiryDate(LocalDateTime.now().minusMinutes(1));
        given(repository.findByToken(anyString())).willReturn(Optional.of(verificationToken));

        //when //then
        Exception exception = assertThrows(RuntimeException.class,
                () -> service.getVerificationToken(verificationToken.getToken()));
        assertEquals("Token expired", exception.getMessage());
    }
}
