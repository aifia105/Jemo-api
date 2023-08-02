package com.PersonalProject.Jemo.services;

import com.PersonalProject.Jemo.dto.ModifyPasswordDto;
import com.PersonalProject.Jemo.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto save(UserDto userDto);
    UserDto findById(Integer id);
    List<UserDto> findAll();
    void delete(Integer id);
    UserDto findByEmail(String email);
    UserDto ChangePassword(ModifyPasswordDto modifyPasswordDto);
}
