package net.uoay.chat.file;

import net.uoay.chat.util.FileUtils;
import net.uoay.chat.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.File;
import java.io.IOException;

public abstract class SliceUploadTemplate implements SliceUploadStrategy {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public abstract boolean upload(String filePath, long chunk, byte[] bytes);

    protected boolean createTmpFile(String filename, String path) {
        File tmpDir = new File(path);
        File tmpFile = new File(path, filename);
        if (!tmpDir.exists()) {
           tmpDir.mkdirs();
        }
        try {
            return tmpFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean sliceUpload(String username, String uuid, long chunk, byte[] bytes) {
        var filenameKey = RedisUtils.fileNameKey(username, uuid);
        var filename = stringRedisTemplate.opsForValue().get(filenameKey);
        var result = upload(FileUtils.filePath(username, uuid, filename), chunk, bytes);
        if (result) {
            var uploadKey = RedisUtils.fileUploadKey(username, uuid);
            var leftChunks = stringRedisTemplate.opsForValue().decrement(uploadKey);
            assert leftChunks != null;
            if (leftChunks == 0) {
                completeFileUpload(username, uuid);
            }
        }
        return result;
    }

    private void completeFileUpload(String username, String uuid) {
        var uploadKey = RedisUtils.fileUploadKey(username, uuid);
        stringRedisTemplate.delete(uploadKey);
        var filenameKey = RedisUtils.fileNameKey(username, uuid);
        stringRedisTemplate.delete(filenameKey);
    }
}
