package com.project.ecommerce.service;

import com.project.ecommerce.dto.request.UserRequestDTO;
import com.project.ecommerce.dto.response.UserResponseDTO;
import com.project.ecommerce.entity.User;
import com.project.ecommerce.repository.UserRepo;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepo userRepo;
    private final ModelMapper modelMapper;

    public UserService(UserRepo userRepo, ModelMapper modelMapper){
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO){
        logger.info("Creating user with email: {}", userRequestDTO.getEmail());
        if(userRepo.existsByEmail(userRequestDTO.getEmail())){
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .name(userRequestDTO.getName())
                .phone(userRequestDTO.getContact())
                .build();

        User savedUser = userRepo.save(user);

        logger.info("User: {} created successfully",userRequestDTO.getEmail());

        return convertToDTOResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(int id){
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return convertToDTOResponse(user);
    }

    @Transactional
    public UserResponseDTO updateUser(int id, UserRequestDTO userRequestDTO){
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(userRequestDTO.getName());
        user.setPhone(userRequestDTO.getContact());
        user.setEmail(userRequestDTO.getEmail());

        User updatedUser = userRepo.save(user);
        return convertToDTOResponse(updatedUser);
    }

    public UserResponseDTO convertToDTOResponse(User user){
        UserResponseDTO dto = modelMapper.map(user, UserResponseDTO.class);
        return dto;
    }
}
