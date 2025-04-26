package net.uoay.chat.request;

public record PartialFileUploadRequest(String uuid, long chunk, String md5, byte[] bytes) {}
