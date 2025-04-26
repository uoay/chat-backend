package net.uoay.chat.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

@Component
public class RandomAccessUploadStrategy extends SliceUploadTemplate {
    private static final Logger logger = LoggerFactory.getLogger(RandomAccessUploadStrategy.class);

    @Value("${file.chunk_bytes}")
    private long defaultChunkBytes;

    @Override
    public boolean upload(String filePath, long chunk, byte[] bytes) {
        RandomAccessFile randomAccessFile = null;
        try {
            File tmpFile = new File(filePath);
            randomAccessFile = new RandomAccessFile(tmpFile, "rw");
            long offset = defaultChunkBytes * chunk;
            randomAccessFile.seek(offset);
            randomAccessFile.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    logger.warn(e.getMessage());
                }
            }
        }
        return true;
    }
}
