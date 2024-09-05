package com.registration.registration.service;

import java.util.List;

import com.registration.registration.model.User;

public interface UserService {

    public User addUser(User user);

    public User updateUsers(User user);

    public List<User> listerUsers();

    public void deleteUsers(Integer id);
    
}
