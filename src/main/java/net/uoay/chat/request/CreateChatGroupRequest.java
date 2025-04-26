package net.uoay.chat.request;

import jakarta.validation.constraints.Pattern;
import net.uoay.chat.util.ValidUtils;

public record CreateChatGroupRequest(
    @Pattern(regexp = ValidUtils.groupSearchIdPattern, message = ValidUtils.groupSearchIdInvalidMessage)
    String searchId,
    @Pattern(regexp = ValidUtils.displayNamePattern, message = ValidUtils.displayNameInvalidMessage)
    String displayName
) {}
