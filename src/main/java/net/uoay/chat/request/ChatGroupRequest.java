package net.uoay.chat.request;

import jakarta.validation.constraints.Pattern;
import net.uoay.chat.util.ValidUtils;

public record ChatGroupRequest(
    @Pattern(regexp = ValidUtils.groupSearchIdPattern, message = ValidUtils.groupSearchIdInvalidMessage)
    String id
) {}
