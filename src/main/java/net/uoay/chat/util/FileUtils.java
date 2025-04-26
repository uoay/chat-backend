package net.uoay.chat.util;

import org.springframework.util.DigestUtils;

public class FileUtils {
    public static String filePath(String username, String uuid, String filename) {
        return "file/" + username + "/" + uuid + "/" + filename;
    }

    public static boolean compareMd5(String md5, byte[] bytes) {
        return DigestUtils.md5DigestAsHex(bytes).equals(md5);
    }
}
