package com.project.fashionshops.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderDTO {
    @JsonProperty("user_id")
    @Min(value = 1, message = "user's id must be > 0")
    private Long userId;  // cho biết ai đặt hàng ai nhận việc đặt hàng đó

    @JsonProperty("fullname")
    private String fullName;


    private String email;

    @JsonProperty("phone_number")
    @NotBlank(message = "phone number is required")
    @Size(min = 5, message = "phone number must be at least 5 characters")
    private String phoneNumber;

    private String address;

    private String note;

    @JsonProperty("total_money")
    @Min(value = 0 , message = "Total money must be >= 0")
    private Float totalMoney;

    @JsonProperty("shipping_method")
    private String shippingMethod;

    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonProperty("shipping_date")
    private LocalDate shippingDate;

    @JsonProperty("payment_method")
    private String paymentMethod;

}

