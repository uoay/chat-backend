package net.uoay.chat.util;

public class RedisUtils {
    public static String fileKey(String md5, long totalBytes) {
        return "file:" + md5 + ":" + totalBytes;
    }

    public static String fileUploadKey(String username, String uuid) {
        return "file:upload:" + username + ":" + uuid;
    }

    public static String fileNameKey(String username, String uuid) {
        return "file:name:" + username + ":" + uuid;
    }

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
