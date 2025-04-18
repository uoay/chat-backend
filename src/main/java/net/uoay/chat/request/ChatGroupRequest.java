package net.uoay.chat.request;

import jakarta.validation.constraints.Pattern;
import net.uoay.chat.Utils;

public record ChatGroupRequest(
    @Pattern(regexp = Utils.groupSearchIdPattern, message = Utils.groupSearchIdInvalidMessage)
    String id
) {}
