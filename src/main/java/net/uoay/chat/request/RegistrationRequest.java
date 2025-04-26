package net.uoay.chat.request;

import jakarta.validation.constraints.Pattern;
import net.uoay.chat.util.ValidUtils;

public record RegistrationRequest(
    @Pattern(regexp = ValidUtils.usernamePattern, message = ValidUtils.usernameInvalidMessage)
    String username,
    @Pattern(regexp = ValidUtils.passwordPattern, message = ValidUtils.passwordInvalidMessage)
    String password,
    @Pattern(regexp = ValidUtils.displayNamePattern, message = ValidUtils.displayNameInvalidMessage)
    String display_name
) {}
