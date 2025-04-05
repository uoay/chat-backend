package net.uoay.chat.request;

import jakarta.validation.constraints.Pattern;
import net.uoay.chat.Utils;

public record RegistrationRequest(
    @Pattern(regexp = Utils.usernamePattern, message = Utils.usernameInvalidMessage)
    String username,
    @Pattern(regexp = Utils.passwordPattern, message = Utils.passwordInvalidMessage)
    String password,
    @Pattern(regexp = Utils.displayNamePattern, message = Utils.displayNameInvalidMessage)
    String display_name
) {}
