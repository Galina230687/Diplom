package ru.netology.cloudStorage.service;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.netology.cloudStorage.DTO.UserDTO;
import ru.netology.cloudStorage.entity.User;
import ru.netology.cloudStorage.enums.Role;
import ru.netology.cloudStorage.repository.UserRepository;
import ru.netology.cloudStorage.utils.MapperUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Collections;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RegistrationServiceTest {

    @Autowired
    private RegistrationService registrationService;
    @MockitoBean
    private UserRepository userRepository;
    @MockitoBean
    private MapperUtils mapperUtils;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    public void init() {
        userDTO = UserDTO.builder()
                .login("testLogin@test.ru")
                .password("testPassword")
                .build();
        user = User.builder()
                .id(1L)
                .login("testLogin@test.ru")
                .password("testPassword")
                .roles(Collections.singleton(Role.ROLE_USER))
                .build();
    }

    @Test
    public void test_registerUser() {
        Mockito.when(mapperUtils.toUserEntity(userDTO)).thenReturn(user);
        Mockito.when(userRepository.findUserByLogin(user.getLogin())).thenReturn(Optional.empty());

        registrationService.registerUser(userDTO);

        Mockito.verify(userRepository, Mockito.times(1)).findUserByLogin("testLogin@test.ru");
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    public void test_getUser() {

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));

        registrationService.getUser(1L);

        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    public void test_deleteUser() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));

        registrationService.deleteUser(1L);

        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(1L);
    }
}
