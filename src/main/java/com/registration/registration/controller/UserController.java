package com.registration.registration.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.registration.registration.model.User;
import com.registration.registration.service.UserServiceImpl;

@RestController
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @PostMapping("/adduser")
    public ResponseEntity<User> addUsers(@RequestBody User users) {
        return ResponseEntity.ok(userServiceImpl.addUser(users));
    }

    @DeleteMapping("/{iduser}")
    public void  deleteUsers(@PathVariable Integer iduser) {
        userServiceImpl.deleteUsers(iduser);
        
    }

    @PutMapping("/update_user")
    public ResponseEntity<User> updateUsers(@RequestBody User users) {
        return ResponseEntity.ok(userServiceImpl.updateUsers(users));
    }

    @GetMapping("/findusers")
    public ResponseEntity<List<User>> listerUsers() {
        return ResponseEntity.ok(userServiceImpl.listerUsers());
    }
  
}
