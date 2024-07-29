package com.nure.ua.fomin.users.controller;

import com.nure.ua.fomin.users.api.UsersApi;
import com.nure.ua.fomin.users.api.dto.FilterDTO;
import com.nure.ua.fomin.users.api.dto.UserDTO;
import com.nure.ua.fomin.users.entity.Filter;
import com.nure.ua.fomin.users.mappers.FilterMapper;
import com.nure.ua.fomin.users.mappers.UserMapper;
import com.nure.ua.fomin.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController implements UsersApi {

    private final UserService userService;

    private final UserMapper userMapper;

    private final FilterMapper filterMapper;

    @Override
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return new ResponseEntity<>(userMapper.toDTO(userService.getAll()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<UserDTO>> searchUsers(@RequestBody List<FilterDTO> searchCriteria) {
        List<Filter> filters = filterMapper.toEntity(searchCriteria);
        return new ResponseEntity<>(userMapper.toDTO(userService.search(filters)), HttpStatus.OK);
    }

}
