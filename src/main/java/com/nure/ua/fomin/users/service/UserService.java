package com.nure.ua.fomin.users.service;

import com.nure.ua.fomin.users.entity.Filter;
import com.nure.ua.fomin.users.entity.User;
import com.nure.ua.fomin.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService {

    private final List<UserRepository> repositories;

    public List<User> getAll() {
        return repositories
                .stream()
                .map(UserRepository::getAll)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public List<User> search(List<Filter> searchCriteria) {
        return repositories
                .stream()
                .map(repository -> repository.search(searchCriteria))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
