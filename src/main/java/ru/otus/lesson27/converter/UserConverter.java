package ru.otus.lesson27.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.lesson27.dto.UserDto;
import ru.otus.lesson27.model.Role;
import ru.otus.lesson27.model.User;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserConverter {

    public UserDto entityToDto(User user) {
        return new UserDto(user.getId(),user.getCreated(),user.getUpdated(),user.getStatus(),user.getUsername(),user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword());
    }

    public User DtoToEntity(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        List<Role> roles = new ArrayList<>();
        user.setRoles(roles);
        return user;
    }
}
