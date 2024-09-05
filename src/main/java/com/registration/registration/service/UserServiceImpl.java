package com.registration.registration.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.registration.registration.model.User;
import com.registration.registration.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    public UserRepository userRepository;

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUsers(User user) {
        Optional<User> updateEmp = userRepository.findById(user.getId());

        User emp= updateEmp.get();

        emp.setId(user.getId());
        emp.setFirstname(user.getFirstname());
        emp.setLastname(user.getLastname());
        emp.setEmail(user.getEmail());

       return userRepository.save(emp);
    }

    @Override
    public List<User> listerUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUsers(Integer id) {
        userRepository.deleteById(id);
    }
    
}
