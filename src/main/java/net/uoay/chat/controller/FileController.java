package net.uoay.chat.controller;

import net.uoay.chat.file.FileService;
import net.uoay.chat.request.PartialFileUploadRequest;
import net.uoay.chat.request.UploadRequest;
import net.uoay.chat.user.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("file")
public class FileController {
    @Autowired
    private FileService fileService;

    @Autowired
    private AccountService accountService;

    @PostMapping("/request_upload")
    public ResponseEntity<String> requestToUpload(
        @RequestBody UploadRequest request
    ) {
        return fileService
            .requestToUpload(
                request.filename(),
                request.md5(),
                request.totalChunks(),
                request.totalBytes(),
                accountService.getUsername()
            )
            .map(uuid -> new ResponseEntity<>(
                uuid,
                HttpStatus.OK
            ))
            .orElse(new ResponseEntity<>(HttpStatus.FORBIDDEN));
    }

    @PostMapping("/upload")
    public long upload(@RequestBody PartialFileUploadRequest request) {
        var result = fileService.upload(
            accountService.getUsername(),
            request.uuid(),
            request.chunk(),
            request.md5(),
            request.bytes()
        );
        if (result) {
            return request.chunk();
        }
        return -request.chunk();
    }
}
