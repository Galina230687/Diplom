package ru.netology.cloudStorage.service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.netology.cloudStorage.DTO.UserDTO;
import ru.netology.cloudStorage.entity.User;
import ru.netology.cloudStorage.enums.Role;
import ru.netology.cloudStorage.exception.UserAlreadyExistsException;
import ru.netology.cloudStorage.exception.UserNotFoundException;
import ru.netology.cloudStorage.repository.UserRepository;
import ru.netology.cloudStorage.utils.MapperUtils;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserRepository userRepository;
    private final MapperUtils mapperUtils;
    private final PasswordEncoder passwordEncoder;

    //Проверяем если ли уже такой пользователь в базе данных. Если есть, бросаем исключениче, если нет, регистрируем:
    public UserDTO registerUser(UserDTO userDTO) {
        User user = mapperUtils.toUserEntity(userDTO);
        userRepository.findUserByLogin(user.getLogin()).ifPresent(s -> {
            throw new UserAlreadyExistsException("User already exists", user.getId());
        });
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(Role.ROLE_USER));
        user.setRole(Role.ROLE_USER.getAuthority());
        return mapperUtils.toUserDto(userRepository.save(user));
    }

    //Ищем пользователя по ID, если нет, бросаем исключение:
    public UserDTO getUser(Long id) {
        User foundUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found", id));
        return mapperUtils.toUserDto(foundUser);
    }

    //Удаляем пользователя по ID. Если нет, бросаем исключение:
    public void deleteUser(Long id) {
        User foundUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found", id));
        userRepository.deleteById(id);
    }
}
