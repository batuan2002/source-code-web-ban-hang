package com.project.fashionshops.services;

import com.project.fashionshops.dtos.UserDTO;
import com.project.fashionshops.exceptions.DataNotFoundException;
import com.project.fashionshops.models.User;

public interface IUserService {
    User createUser(UserDTO userDTO) throws DataNotFoundException;
    String login(String phoneNumber, String password) throws  Exception;
}
