package com.project.fashionshops.services;

import com.project.fashionshops.components.JwtTokenUtil;
import com.project.fashionshops.dtos.UserDTO;
import com.project.fashionshops.exceptions.DataNotFoundException;
import com.project.fashionshops.models.Role;
import com.project.fashionshops.models.User;
import com.project.fashionshops.repositories.RoleRepository;
import com.project.fashionshops.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements  IUserService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    @Override
    public User createUser(UserDTO userDTO) throws DataNotFoundException {
        String phoneNumber = userDTO.getPhoneNumber();
        // kiểm tra xem sdt đã tồn tại hay chưa
        if (userRepository.existsByPhoneNumber(phoneNumber)){
            throw new DataIntegrityViolationException("số điện thoại đã tồn tại");
        }
        // convert from userDto => user chuyn doi
        User newUser = User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .password(userDTO.getPassword())
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDateOfBirth())
                .facebookAccountId(userDTO.getFacebookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId())
                .build();
        Role role = roleRepository.findById(userDTO.getRoleId())
                // neu ko tim thay
                .orElseThrow(() ->new DataNotFoundException("không tìm thấy vai trò"));
        newUser.setRole(role);

        // kiểm tra nếu có accountId, không yêu cầu password
        if (userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId() == 0){
            String password = userDTO.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
newUser.setPassword(encodedPassword);
        }
        return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password) throws  Exception{
       // lien quan nhieu en security
       Optional<User> optionalUser = userRepository.findByPhoneNumber(phoneNumber);
     if (optionalUser.isEmpty()){
throw new DataNotFoundException("Invalid phonenumber / password");
     }
     User existingUser = optionalUser.get();
     //check password
        if (existingUser.getFacebookAccountId() == 0
                && existingUser.getGoogleAccountId() == 0){
            if (!passwordEncoder.matches(password,existingUser.getPassword())){
                throw  new BadCredentialsException("Wrong phone number of password");
            }
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        phoneNumber,password,existingUser.getAuthorities());
     //authenticate whit java spring security
        authenticationManager.authenticate(authenticationToken);
     return jwtTokenUtil.generateToken(existingUser);
     //  return optionalUser.get(); // muon tra ve JWT token?
    }
}
