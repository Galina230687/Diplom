package ru.netology.cloudStorage.controller;

import ru.netology.cloudStorage.DTO.FileDTO;
import ru.netology.cloudStorage.service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FileControllerTest {

    private static final String AUTH_TOKEN = "auth-token";
    private static final String VALUE_TOKEN = "Bearer auth-token";
    private static final String FILE_NAME = "filename";
    private static final String MY_FILE_NAME = "fileName.txt";
    private static final String URL_FILE = "/file";

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private FileService fileService;

    @BeforeEach
    void setUp() {
        fileService = mock(FileService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new FileController(fileService)).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void test_uploadFile() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                MY_FILE_NAME,
                MediaType.TEXT_PLAIN_VALUE,
                "MyTestFile".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart(URL_FILE)
                        .file(multipartFile)
                        .param(FILE_NAME, "file")
                        .header(AUTH_TOKEN, VALUE_TOKEN))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void test_downloadFile() throws Exception {
        FileDTO fileDTO = FileDTO.builder()
                .fileName(MY_FILE_NAME)
                .fileByte("MyTestFile".getBytes())
                .type(MediaType.TEXT_PLAIN_VALUE).build();

        Mockito.when(fileService.downloadFile(MY_FILE_NAME)).thenReturn(fileDTO);

        mockMvc.perform(get(URL_FILE)
                        .param(FILE_NAME, MY_FILE_NAME)
                        .header(AUTH_TOKEN, VALUE_TOKEN))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE));
    }

    @Test
    void test_renameFile() throws Exception {
        FileDTO newName = new FileDTO();
        newName.setFileName("NewTestFile.txt");

        mockMvc.perform(put(URL_FILE)
                        .param(FILE_NAME, MY_FILE_NAME)
                        .header(AUTH_TOKEN, VALUE_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newName)))
                .andExpect(status().isOk());
    }

    @Test
    void test_deleteFile() throws Exception {
        mockMvc.perform(delete(URL_FILE)
                        .param(FILE_NAME, MY_FILE_NAME)
                        .header(AUTH_TOKEN, VALUE_TOKEN))
                .andExpect(status().isOk());
    }

    @Test
    void test_getAllFiles() throws Exception {
        List<FileDTO> listFile = List.of(
                FileDTO.builder().size(1425L).fileName("MyTestFile1.txt").build(),
                FileDTO.builder().size(1926L).fileName("MyTestFile2.txt").build(),
                FileDTO.builder().size(3258L).fileName("MyTestFile3.txt").build());
        Mockito.when(fileService.getAllFiles(3)).thenReturn(listFile);

        mockMvc.perform(get("/list")
                        .header(AUTH_TOKEN, VALUE_TOKEN)
                        .param("limit", "3"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(listFile)));
    }
}
