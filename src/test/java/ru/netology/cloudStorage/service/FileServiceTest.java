package ru.netology.cloudStorage.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloudStorage.DTO.FileDTO;
import ru.netology.cloudStorage.entity.User;
import ru.netology.cloudStorage.enums.Role;
import ru.netology.cloudStorage.repository.FileRepository;
import ru.netology.cloudStorage.entity.File;
import ru.netology.cloudStorage.repository.UserRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class FileServiceTest {

    private static final String MY_FILE_NAME = "fileName.txt";
    @Autowired
    private FileService fileService;
    @MockitoBean
    private FileRepository fileRepository;
    @MockitoBean
    private UserRepository userRepository;
    private User user;
    private File file;

    @BeforeEach
    public void init() {
        user = User.builder()
                .id(1L)
                .login("testLogin@test.ru")
                .password("testPassword")
                .roles(Collections.singleton(Role.ROLE_USER))
                .build();

        file = File.builder()
                .Id(10L)
                .fileName(MY_FILE_NAME)
                .hash("4f749020ea967d8302c47b7545187ea7")
                .type(MediaType.TEXT_PLAIN_VALUE)
                .size(4L)
                .fileByte("MyTestFile".getBytes())
                .createdDate(LocalDateTime.now())
                .user(user)
                .build();
    }

    @Test
    @WithMockUser(username = "testLogin@test.ru",password = "testPassword")
    void test_uploadFile() throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Mockito.when(userRepository.findUserByLogin(auth.getName())).thenReturn(Optional.ofNullable(user));
        System.out.println("TEST-TEST-TEST FILE SIZE IS: " + user.getLogin());

        String hash = "4f749020ea967d8302c47b7545187ea7";
        MultipartFile multipartFile =
                new MockMultipartFile("file", MY_FILE_NAME,
                        MediaType.TEXT_PLAIN_VALUE, "MyTestFile".getBytes());

        Mockito.when(fileRepository.findFileByUserIdAndFileName(1L, MY_FILE_NAME))
                .thenReturn(Optional.empty());

        File createdFile = File.builder()
                .Id(10L)
                .hash(hash)
                .fileName(MY_FILE_NAME)
                .type(multipartFile.getContentType())
                .size(multipartFile.getSize())
                .fileByte(multipartFile.getBytes())
                .user(User.builder().id(1L).build())
                .build();

        fileService.uploadFile(multipartFile, MY_FILE_NAME);

        Assertions.assertEquals(10L, createdFile.getSize());
    }

    @Test
    @WithMockUser(username = "testLogin@test.ru",password = "testPassword")
    void test_downloadFile() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Mockito.when(userRepository.findUserByLogin(auth.getName())).thenReturn(Optional.ofNullable(user));
        Mockito.when(fileRepository.findFileByUserIdAndFileName(1L, MY_FILE_NAME))
                .thenReturn(Optional.ofNullable(file));

        FileDTO downloadFile = fileService.downloadFile(MY_FILE_NAME);

        Assertions.assertEquals(MY_FILE_NAME, downloadFile.getFileName());
    }

    @Test
    @WithMockUser(username = "testLogin@test.ru",password = "testPassword")
    void test_editFileName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Mockito.when(userRepository.findUserByLogin(auth.getName())).thenReturn(Optional.ofNullable(user));

        FileDTO newName = new FileDTO();
        newName.setFileName("newName.txt");
        Mockito.when(fileRepository.findFileByUserIdAndFileName(1L, MY_FILE_NAME))
                .thenReturn(Optional.ofNullable(file));

        fileService.editFileName(MY_FILE_NAME, newName);

        Mockito.verify(fileRepository, Mockito.times(1)).save(file);
    }

    @Test
    @WithMockUser(username = "testLogin@test.ru",password = "testPassword")
    void test_deleteFile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Mockito.when(userRepository.findUserByLogin(auth.getName())).thenReturn(Optional.ofNullable(user));

        Mockito.when(fileRepository.findFileByUserIdAndFileName(1L, MY_FILE_NAME))
                .thenReturn(Optional.ofNullable(file));

        fileService.deleteFile(MY_FILE_NAME);

        Mockito.verify(fileRepository, Mockito.times(1)).save(file);
    }

    @Test
    @WithMockUser(username = "testLogin@test.ru",password = "testPassword")
    void test_getAllFiles() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Mockito.when(userRepository.findUserByLogin(auth.getName())).thenReturn(Optional.ofNullable(user));
        int limit = 3;
        List<File> listFile = List.of(
                File.builder().size(1425L).fileName("MyTestFile1.txt").build(),
                File.builder().size(1926L).fileName("MyTestFile2.txt").build(),
                File.builder().size(3258L).fileName("MyTestFile3.txt").build());

        Mockito.when(fileRepository.findFilesByUserIdWithLimit(user.getId(), limit)).thenReturn(listFile);

        List<FileDTO> files = fileService.getAllFiles(limit);

        Assertions.assertEquals("MyTestFile1.txt", files.get(0).getFileName());
    }
}
