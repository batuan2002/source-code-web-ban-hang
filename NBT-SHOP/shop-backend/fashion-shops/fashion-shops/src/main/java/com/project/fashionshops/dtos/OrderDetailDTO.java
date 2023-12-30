package com.project.fashionshops.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderDetailDTO {

    @JsonProperty("order_id")
    @Min(value = 1, message = "Order's ID must be > 0")
    private Long orderId;




    @Min(value = 1, message = "Product's ID must be > 0")
    @JsonProperty("product_id")
    private Long productId;

    // @JsonProperty("product_id ")
   // @Min(value = 1, message = "Product's ID must be > 0")
   // private Long productId;

    @Min(value = 0 , message = "Price's ID must be >= 0 ")
    private Float price;

    @Min(value = 0 , message = "number_of_products's ID must be >= 0")
    @JsonProperty("number_of_products")
    private int numberOfProducts;

    @Min(value = 0 , message = "Total_money's ID must be >= 0")
    @JsonProperty("total_money")
    private Float totalMoney;


    private String color;
}
