package net.uoay.chat.request;

import jakarta.validation.constraints.Pattern;

import net.uoay.chat.Utils;

public class CreateChatGroupRequest {

    @Pattern(regexp = Utils.displayNamePattern, message = Utils.displayNameInvalidMessage)
    public String displayName;

}
