package net.uoay.chat;

public class Utils {

    public static final String usernamePattern = "[\\w]{1,16}";
    public static final String passwordPattern = "[\\x21-\\x7e]{8,32}";
    public static final String displayNamePattern = "[\\S]{1,32}";

    public static final String usernameInvalidMessage = "Invalid username";
    public static final String passwordInvalidMessage = "Invalid password";
    public static final String displayNameInvalidMessage = "Invalid display name";

    public static String friendSetKey(String username) {
        return username + ":friendSet";
    }

    public static String chatGroupSetKey(String username) {
        return username + ":chatGroupSet";
    }

    public static String chatGroupMemberSetKey(String groupId) {
        return "chatGroupMemberSet:" + groupId;
    }

}
