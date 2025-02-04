package ru.netology.cloudStorage.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.cloudStorage.DTO.UserDTO;
import ru.netology.cloudStorage.model.Token;
import ru.netology.cloudStorage.service.AuthenticationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public Token login(@RequestBody UserDTO userDTO) {
        return authenticationService.login(userDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("auth-token") String authToken,
                                    HttpServletRequest request, HttpServletResponse response)
    {
        if (authenticationService.logout(authToken, request, response) == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return ResponseEntity.ok(HttpStatus.OK);
        }
    }
}
