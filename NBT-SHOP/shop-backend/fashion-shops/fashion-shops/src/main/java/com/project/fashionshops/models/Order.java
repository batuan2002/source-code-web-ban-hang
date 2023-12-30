package com.project.fashionshops.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "orders")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // la moi quan he 1 nhieu voi user_id
    @JoinColumn(name = "user_id") // dung de luu tru khoa ngoai 1 nhieu
    private User user;

    @Column(name = "fullname" , length = 100)
    private String fullName;

    @Column(name = "email", length = 100)
    private String email;



    // Chú thích này được sử dụng để chỉ định chi tiết của một cột trong bảng cơ sở dữ liệu
    //Thuộc tính này dùng để xác định độ dài của cột trong cơ sở dữ liệu
    //huộc tính này chỉ ra rằng cột "phone_number" không được phép có giá trị null.
    @Column(name = "phone_number",length = 100,nullable = false)
    private String phoneNumber;

    @Column(name = "address",length = 100)
    private String address;

    @Column(name = "note",length = 100)
    private String note;

    @Column(name = "order_date")
    private Date orderDate;

    @Column (name = "status")
    private String status;

    @Column(name = "total_money")
    private Integer totalMoney;

    @Column(name = "shipping_method")
    private String shippingMethod;

    @Column(name = "shipping_address")
    private String shippingAddress;

    @Column(name = "shipping_date")
    private LocalDate shippingDate;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Column(name = "payment_method")
    private String paymentMethod;


    @Column(name = "active")
    private Boolean active; // thuộc về admin

}
