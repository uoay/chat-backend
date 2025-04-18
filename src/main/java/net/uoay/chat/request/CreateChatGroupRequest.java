package net.uoay.chat.request;

import jakarta.validation.constraints.Pattern;
import net.uoay.chat.Utils;

public record CreateChatGroupRequest(
    @Pattern(regexp = Utils.groupSearchIdPattern, message = Utils.groupSearchIdInvalidMessage)
    String searchId,
    @Pattern(regexp = Utils.displayNamePattern, message = Utils.displayNameInvalidMessage)
    String displayName
) {}
