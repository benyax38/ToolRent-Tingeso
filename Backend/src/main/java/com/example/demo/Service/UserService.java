package com.example.demo.Service;

import com.example.demo.Entity.UserEntity;
import com.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) { this.userRepository = userRepository; }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public UserEntity createUsers(UserEntity user) {
        return userRepository.save(user);
    }

    public void deleteUsersById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe usuario con id: " + id);
        }
        userRepository.deleteById(id);
    }
}
