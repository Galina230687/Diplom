package ru.netology.cloudStorage.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.netology.cloudStorage.DTO.UserDTO;
import ru.netology.cloudStorage.service.RegistrationService;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationControllerTest {

    private static final String AUTH_TOKEN = "auth-token";
    private static final String VALUE_TOKEN = "Bearer auth-token";
    private static final String LOGIN = "testLogin@test.ru";
    private static final String PASSWORD = "testPassword";

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private RegistrationService registrationService;

    @BeforeEach
    void setUp() {
        registrationService = mock(RegistrationService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new RegistrationController(registrationService)).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void test_registerUser() throws Exception {
        UserDTO userDTO = UserDTO.builder()
                .login(LOGIN)
                .password(PASSWORD)
                .build();

        Mockito.when(registrationService.registerUser(userDTO)).thenReturn(userDTO);

        mockMvc.perform(post("/user/register")
                        .header(AUTH_TOKEN, VALUE_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(userDTO)));
    }

    @Test
    void test_getUser() throws Exception {
        UserDTO userDTO = UserDTO.builder()
                .login(LOGIN).password(PASSWORD).build();
        Mockito.when(registrationService.getUser(2L)).thenReturn(userDTO);

        mockMvc.perform(get("/user/{id}", "2")
                        .header(AUTH_TOKEN, VALUE_TOKEN))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(userDTO)));
    }

    @Test
    void test_deleteUser() throws Exception {
        mockMvc.perform(delete("/user/delete/{id}", "2")
                        .header(AUTH_TOKEN, VALUE_TOKEN))
                .andExpect(status().isOk());
    }
}
