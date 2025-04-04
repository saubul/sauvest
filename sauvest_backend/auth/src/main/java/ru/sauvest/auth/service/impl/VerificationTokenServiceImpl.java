package ru.sauvest.auth.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sauvest.auth.entity.VerificationToken;
import ru.sauvest.auth.repository.VerificationTokenRepository;
import ru.sauvest.auth.service.VerificationTokenService;
import ru.sauvest.baseservices.exception.SauvestException;
import ru.sauvest.baseservices.service.FormatUtilService;

import java.util.Optional;
import java.util.UUID;

import static ru.sauvest.baseservices.exception.ExceptionCodeConstants.RESOURCE_NOT_FOUND;
import static ru.sauvest.baseservices.exception.ExceptionCodeConstants.UNKNOWN_ERROR;

@Service
@RequiredArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;

    private final UserService userService;

    @Override
    @Transactional
    public String createForUser(String username) {
        try {
            String verificationTokenString = UUID.randomUUID().toString();
            VerificationToken verificationToken = VerificationToken.builder()
                    .token(verificationTokenString)
                    .user(userService.findByUsername(username))
                    .build();
            return verificationTokenRepository.save(verificationToken).getToken();
        } catch (Exception e) {
            throw new SauvestException(UNKNOWN_ERROR,
                    String.format("При попытке создать токен верификации для пользователя с логином \"%s\" произошла ошибка: %s",
                            username, FormatUtilService.getNonNullExceptionMessage(e)),
                    e);
        }
    }

    @Override
    public VerificationToken findByToken(String token) {
        Optional<VerificationToken> verificationTokenOptional = verificationTokenRepository.findByToken(token);
        return verificationTokenOptional.orElseThrow(
                () -> new SauvestException(RESOURCE_NOT_FOUND,
                        String.format("При попытке найти токен верификации произошла ошибка: токен верификации \"%s\" не найден", token)
                )
        );
    }

    @Override
    public void delete(VerificationToken verificationToken) {
        verificationTokenRepository.delete(verificationToken);
    }
}
