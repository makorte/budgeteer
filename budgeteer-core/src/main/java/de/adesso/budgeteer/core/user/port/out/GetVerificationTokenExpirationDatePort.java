package de.adesso.budgeteer.core.user.port.out;

import java.time.LocalDateTime;

public interface GetVerificationTokenExpirationDatePort {
    LocalDateTime getVerificationTokenExpirationDate(String token);
}
