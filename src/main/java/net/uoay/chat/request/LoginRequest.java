package net.uoay.chat.request;

import jakarta.validation.constraints.Size;

public class LoginRequest {

    @Size(min = 1, max = 16)
    public String username;

    @Size(min = 8, max = 16)
    public String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
