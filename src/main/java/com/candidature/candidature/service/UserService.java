package com.candidature.candidature.service;

import java.util.List;

import com.candidature.candidature.model.User;

public interface UserService {

    public User addUser(User user);

    public User updateUsers(User user);

    public List<User> listerUsers();

    public void deleteUsers(Integer id);
    
}
