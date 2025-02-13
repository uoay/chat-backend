package net.uoay.chat.request;

import jakarta.validation.constraints.Size;

public final class RegistrationRequest {

    @Size(min = 1, max = 16)
    public String username;

    @Size(min = 8, max = 16)
    public String password;

    @Size(min = 1, max = 32)
    public String display_name;
}
