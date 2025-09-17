package com.example.demo.Service;

import com.example.demo.DTOs.RoleDTO;
import com.example.demo.DTOs.UserDTO;
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

    public List<UserDTO> getAllUsersDTO() {
        return userRepository.findAll().stream()
                .map(user -> {
                    UserDTO userDTO = new UserDTO();
                    userDTO.setUserId(user.getUserId());
                    userDTO.setUserFirstName(user.getUserFirstName());
                    userDTO.setUserLastName(user.getUserLastName());
                    userDTO.setUserRut(user.getUserRut());
                    userDTO.setUserPhone(user.getUserPhone());
                    userDTO.setUserEmail(user.getUserEmail());
                    userDTO.setRole(new RoleDTO(user.getRoles().getRoleId(), user.getRoles().getRole()));
                    return userDTO;
                })
                .toList();
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
