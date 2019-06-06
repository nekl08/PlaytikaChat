package ua.nekl08.demo.service;

import ua.nekl08.demo.model.User;

public interface UserService {
    void save(User user);

    User findByUsername(String username);
}
