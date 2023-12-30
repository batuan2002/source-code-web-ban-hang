package com.project.fashionshops.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

public class ProductDTO {
    @NotBlank(message = "this is message")
    // đảm bảo rằng chuổi ko để trống

    @Size(min = 3, max = 200, message = "title must be between 3 and 200 characters")
    //it nhất phải có 3 đến 200 kí tự
    private String name;

    @Min(value = 0, message = "price must greater than or equal to 0 ")
    @Max(value = 1000000,message = "price must less than or equal to 10,000,000")
    private Float price; // giá cả

    private String thumbnail;// đường link tới file ảnh

    private String description;// mô tả

    @JsonProperty("category_id")//giúp ánh xạ giữa tên của thuộc tính trong Java
    private Long categoryId;

     // chuyể thành số nhiều để lấy nhièu files ảnh
}



