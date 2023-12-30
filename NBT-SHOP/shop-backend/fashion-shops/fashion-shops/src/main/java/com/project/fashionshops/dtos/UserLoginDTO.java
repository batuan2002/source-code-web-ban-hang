package com.project.fashionshops.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserLoginDTO {
    @JsonProperty("phone_number")
    @NotBlank(message = "yêu cầu bắt buộc phải nhập vào ko đc để trống")
    private String phoneNumber;


    @NotBlank(message = "không đc để trống")
    private String password;
}
