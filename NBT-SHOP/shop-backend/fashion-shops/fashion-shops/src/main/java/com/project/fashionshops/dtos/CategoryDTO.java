package com.project.fashionshops.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data


public class CategoryDTO {
    @NotEmpty(message = "Category's name not be empty") // ko cho phép để trống
    private String name;
}
