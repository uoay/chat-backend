package net.uoay.chat.request;

import jakarta.validation.constraints.Pattern;

import net.uoay.chat.Utils;

public final class RegistrationRequest {

    @Pattern(regexp = Utils.usernamePattern, message = Utils.usernameInvalidMessage)
    public String username;

    @Pattern(regexp = Utils.passwordPattern, message = Utils.passwordInvalidMessage)
    public String password;

    @Pattern(regexp = Utils.displayNamePattern, message = Utils.displayNameInvalidMessage)
    public String display_name;

}
