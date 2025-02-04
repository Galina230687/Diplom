package ru.netology.cloudStorage.utils;
import org.springframework.stereotype.Service;
import ru.netology.cloudStorage.DTO.UserDTO;
import ru.netology.cloudStorage.entity.User;

@Service
public class MapperUtils {
    //Преобразовываем из DTO в Entity
    public User toUserEntity(UserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin());
        user.setPassword(userDTO.getPassword());
        return user;
    }

    //Преобразовываем из Entity в DTO
    public UserDTO toUserDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin(user.getLogin());
        userDTO.setPassword(user.getPassword());
        return userDTO;
    }
}
