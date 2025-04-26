package net.uoay.chat.request;

import org.springframework.web.multipart.MultipartFile;

public record FileUploadRequest(
    String path,
    MultipartFile file,
    Integer chunk,
    Integer chunks,
    String md5,
    Integer chunkBytes
) {}
