package com.project.fashionshops.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;


/*
hãy validate các thông tin:
-phonenumber,roleId phải nhập
-nếu có facebook_account,google_account_id thì password,retypepassword ko cần phải nhập
-roleId nếu ko nhập th nó mặc định là 1 => code controller
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

public class UserDTO {
    @JsonProperty("fullname")// nó ánh xạ các thuộc tính trên java
    private String fullName;

    @JsonProperty("phone_number")
    @NotBlank(message = "yêu cầu bắt buộc phải nhập vào ko đc để trống")
    private String phoneNumber;

    private String address;

    @NotBlank(message = "không đc để trống")
    private String password;

    @JsonProperty("retype_password")
    private String retypePassword; // nhập lại mk

    @JsonProperty("date_of_birth")
    private Date dateOfBirth;
    @JsonProperty("facebook_account_id")

    private int facebookAccountId;
    @JsonProperty("google_account_id")
    private int googleAccountId;



    @NotNull(message = "tôi muốn biet a co vai trò j role hay admin")
    @JsonProperty("role_id")
    private Long roleId; // sở dĩ bảng role là Long trong database là kiểu int tương đương

}
