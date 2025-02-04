package ru.netology.cloudStorage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloudStorage.DTO.FileDTO;
import ru.netology.cloudStorage.service.FileService;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FileController {
    private final FileService fileService;

    @PostMapping("/file")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<Void> uploadFile(@NotNull @RequestPart("file") MultipartFile file,
                                           @RequestParam("filename") String fileName) {

        fileService.uploadFile(file, fileName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/file")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<Void> deleteFile(@RequestParam("filename") String fileName) {
        fileService.deleteFile(fileName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/file")
    @ResponseBody
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<byte[]> downloadFile(@RequestParam String filename) {
        FileDTO file = fileService.downloadFile(filename);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(file.getFileByte());
    }

    @PutMapping("/file")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<Void> EditFileName(@RequestParam String filename, @RequestBody FileDTO fileDTO) {
        fileService.editFileName(filename, fileDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<List<FileDTO>> getAllFiles(@Min(1) @RequestParam int limit) {
        return new ResponseEntity<>(fileService.getAllFiles(limit), HttpStatus.OK);
    }
}