package net.uoay.chat.file;

public interface SliceUploadStrategy {
    boolean sliceUpload(String username, String uuid, long chunk, byte[] bytes);
}
