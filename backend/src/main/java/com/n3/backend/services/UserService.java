package com.n3.backend.services;

import com.n3.backend.dto.ApiResponse;
import com.n3.backend.dto.DtoPage;
import com.n3.backend.dto.User.User;
import com.n3.backend.dto.User.UserRequest;
import com.n3.backend.entities.UserEntity;
import com.n3.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserEntity getByEmail(String email){
        return userRepository.getByEmail(email);
    }

    public UserEntity addNewUser(UserEntity user){
        try {
            userRepository.save(user);
            return user;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public UserEntity getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return getByEmail(userDetails.getUsername());
        } catch (Exception e){
            return null;
        }
    }

    public ApiResponse getListUser(UserRequest request){
        try {
            Page data = userRepository.findByFullnameContainingIgnoreCaseAndEmailContainingIgnoreCaseAndIsAdmin(request.getFullname(), request.getEmail(), false, PageRequest.of(request.getPage() - 1, request.getSize(), Sort.by(request.isReverse() ? Sort.Direction.DESC : Sort.Direction.ASC, request.getSort())));

            List<UserEntity> list = data.getContent();
            int totalItem = (int) data.getTotalElements();
            int totalPage = data.getTotalPages();

            return new ApiResponse(true, 200, new DtoPage(totalPage, request.getPage(), totalItem, User.getList(list)), "success");
        }
        catch (Exception e){
            return new ApiResponse(false, 500, null, e.getMessage());
        }
    }

    public UserEntity save(UserEntity user){
        return userRepository.save(user);
    }

    public boolean existsById(int id){
        return userRepository.existsById(id);
    }

    public UserEntity getById(int id){
        return userRepository.getOne(id);
    }
}
