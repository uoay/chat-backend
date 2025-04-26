package net.uoay.chat.util;

public class ValidUtils {
    public static final String usernamePattern = "[\\w]{1,16}";
    public static final String passwordPattern = "[\\x21-\\x7e]{8,32}";
    public static final String displayNamePattern = "[\\S]{1,32}";
    public static final String groupSearchIdPattern = usernamePattern;

    public static final String usernameInvalidMessage = "Invalid username";
    public static final String passwordInvalidMessage = "Invalid password";
    public static final String displayNameInvalidMessage = "Invalid display name";
    public static final String groupSearchIdInvalidMessage = "Invalid search id";
}
