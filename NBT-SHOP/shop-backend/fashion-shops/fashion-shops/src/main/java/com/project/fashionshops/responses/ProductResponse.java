package com.project.fashionshops.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.fashionshops.models.Product;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse extends  BaseResponse{


   private String name;

    private Float price; // giá cả

    private String thumbnail;// đường link tới file ảnh

    private String description;// mô tả

    @JsonProperty("category_id")//giúp ánh xạ giữa tên của thuộc tính trong Java
    private Long categoryId;

    // tra ve chinh productrepon
 public  static  ProductResponse fromProduct(Product product){
  ProductResponse productResponse = ProductResponse.builder()
          .name(product.getName())
          .price(product.getPrice())
          .thumbnail(product.getThumbnail())
          .description(product.getDescription())
          .categoryId(product.getCategory().getId())
          .build();
  productResponse.setCreatedAt(product.getCreatedAt());
  productResponse.setUpdatedAt(product.getUpdatedAt());
  return productResponse;
 }


}
