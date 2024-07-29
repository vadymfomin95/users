package com.nure.ua.fomin.users.repository;

import com.nure.ua.fomin.users.entity.Filter;
import com.nure.ua.fomin.users.entity.User;

import java.util.List;

public interface UserRepository {

    List<User> getAll();

    List<User> search(List<Filter> searchCriteria);
}
