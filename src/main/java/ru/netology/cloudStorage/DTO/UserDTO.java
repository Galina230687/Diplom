package ru.netology.cloudStorage.DTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Getter
public class UserDTO {
    private String login;
    private String password;
}
