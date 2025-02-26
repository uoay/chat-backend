package net.uoay.chat.request;

import jakarta.validation.constraints.Pattern;
import net.uoay.chat.Utils;


public class LoginRequest {

    @Pattern(regexp = Utils.usernamePattern, message = Utils.usernameInvalidMessage)
    public String username;

    @Pattern(regexp = Utils.passwordPattern, message = Utils.passwordInvalidMessage)
    public String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
