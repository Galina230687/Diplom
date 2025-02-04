package ru.netology.cloudStorage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.netology.cloudStorage.DTO.UserDTO;
import ru.netology.cloudStorage.service.RegistrationService;

import jakarta.validation.Valid;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(registrationService.registerUser(userDTO), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        return new ResponseEntity<>(registrationService.getUser(id), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        registrationService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
