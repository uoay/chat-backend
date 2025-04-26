package net.uoay.chat.request;

import jakarta.validation.constraints.Pattern;
import net.uoay.chat.util.ValidUtils;

public record LoginRequest(
    @Pattern(regexp = ValidUtils.usernamePattern, message = ValidUtils.usernameInvalidMessage)
    String username,
    @Pattern(regexp = ValidUtils.passwordPattern, message = ValidUtils.passwordInvalidMessage)
    String password
) {}
