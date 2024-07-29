package com.nure.ua.fomin.users.mappers;

import com.nure.ua.fomin.users.api.dto.UserDTO;
import com.nure.ua.fomin.users.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    List<UserDTO> toDTO(List<User> users);

    UserDTO toDTO(User user);
}
