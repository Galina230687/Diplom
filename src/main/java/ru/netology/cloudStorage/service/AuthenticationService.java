package ru.netology.cloudStorage.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import ru.netology.cloudStorage.DTO.UserDTO;
import ru.netology.cloudStorage.entity.User;
import ru.netology.cloudStorage.exception.InvalidInputDataException;
import ru.netology.cloudStorage.exception.UserNotFoundException;
import ru.netology.cloudStorage.model.Token;
import ru.netology.cloudStorage.repository.UserRepository;
import ru.netology.cloudStorage.security.JwtProvider;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public Token login(@NonNull UserDTO userDTO) {
        User user = findUserInStorage(userDTO.getLogin());
        if (isEquals(userDTO, user)) {
            String accessToken = jwtProvider.generateAccessToken(user);
            return new Token(accessToken);
        } else {
            throw new InvalidInputDataException("Wrong password", 0);
        }
    }

    public String logout(String authToken, HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = findUserInStorage(auth.getName());
        SecurityContextLogoutHandler securityContextLogoutHandler =
                new SecurityContextLogoutHandler();
        if (user != null) {
            securityContextLogoutHandler.logout(request, response, auth);
            jwtProvider.addAuthTokenInBlackList(authToken);
            return user.getLogin();
        }
        return null;
    }

    private User findUserInStorage(String login) {
        return userRepository.findUserByLogin(login).orElseThrow(() ->
                new UserNotFoundException("User not found by login", 0));
    }

    private boolean isEquals(UserDTO userDTO, User userFromDatabase) {
        return passwordEncoder.matches(userDTO.getPassword(), userFromDatabase.getPassword());
    }
}
