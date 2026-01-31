package com.m01project.taskmanager.service;

import com.m01project.taskmanager.domain.User;
import com.m01project.taskmanager.dto.request.UserCreateRequestDto;
import com.m01project.taskmanager.dto.request.UserUpdateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User create(UserCreateRequestDto createRequestDto);
    User update(String email, UserUpdateRequestDto updateRequestDto);
    User findByEmail(String email);
    Page<User> getUsers(Pageable pageable);
    Page<User> getUsersOnly(Pageable pageable);
    Page<User> getAdminsOnly(Pageable pageable);
    void delete(String email);
    Page<User> search(String query, Pageable pageable);

}
