package net.uoay.chat.request;

public record UploadRequest(String filename, String md5, long totalBytes , long totalChunks) {}
