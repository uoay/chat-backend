package net.uoay.chat.file;

import net.uoay.chat.util.FileUtils;
import net.uoay.chat.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class FileService {
    @Autowired
    private SliceUploadTemplate sliceUploadTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public Optional<String> requestToUpload(
        String filename,
        String md5,
        long totalBytes,
        long chunks,
        String username
    ) {
        var fileKey = RedisUtils.fileKey(md5, totalBytes);
        if (stringRedisTemplate.hasKey(fileKey)) {
            return Optional.empty();
        }
        var uuid = UUID.randomUUID().toString();
        var uploadKey = RedisUtils.fileUploadKey(username, uuid);
        stringRedisTemplate.opsForValue().set(uploadKey, String.valueOf(chunks));
        var filenameKey = RedisUtils.fileNameKey(username, uuid);
        stringRedisTemplate.opsForValue().set(filenameKey, filename);
        sliceUploadTemplate.createTmpFile(filename, "file/" + username + "/" + uuid);
        return Optional.of(uuid);
    }

    public boolean upload(String username, String uuid, long chunk, String md5, byte[] bytes) {
        if (FileUtils.compareMd5(md5, bytes)) {
            return sliceUploadTemplate.sliceUpload(username, uuid, chunk, bytes);
        }
        return false;
    }
}
