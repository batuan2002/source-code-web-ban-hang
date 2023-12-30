package com.project.fashionshops.controllers;

import com.project.fashionshops.dtos.OrderDetailDTO;
import com.project.fashionshops.exceptions.DataNotFoundException;
import com.project.fashionshops.models.OrderDetail;
import com.project.fashionshops.repositories.OrderRepository;
import com.project.fashionshops.responses.OrderDetailResponse;
import com.project.fashionshops.services.OrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order_detail")
@RequiredArgsConstructor
public class OrderDetailController {

    private final OrderDetailService orderDetailService;

    // Thêm mới 1 order_detail
    //http://localhost:8088/api/v1/order_detail
    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(
            @Valid @RequestBody OrderDetailDTO orderDetailDTO
    ) {
        try {
            OrderDetail newOrderDetail = orderDetailService.createOrderDetail(orderDetailDTO);
            return ResponseEntity.ok().body(OrderDetailResponse.fromOrderDetail(newOrderDetail));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }




    //Sử dụng tham số @Valid để kiểm tra tính hợp lệ của tham số đầu vào (ID).
    //Trả về một đối tượng ResponseEntity chứa thông báo với ID của đơn đặt hàng.
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail
    (@Valid @PathVariable("id") Long id) throws DataNotFoundException {
        OrderDetail orderDetail = orderDetailService.getOrderDetail(id);
        return ResponseEntity.ok().body(OrderDetailResponse.fromOrderDetail(orderDetail));
       // return ResponseEntity.ok(orderDetail);
    }





    //lấy ra danh sách các order_detailo cuar 1 order nao do
    @GetMapping("/order/{orderId}")
    //@PathVariable để trích xuất giá trị từ URL và chuyển nó thành tham số của phương thức.
    public ResponseEntity<?> getOrderDetails(
            @Valid @PathVariable("orderId") Long orderId
    ) {
        List<OrderDetail> orderDetails = orderDetailService.findByOrderId(orderId);
      // dung phep anh xa
        List<OrderDetailResponse> orderDetailResponses = orderDetails
                .stream()
                .map(OrderDetailResponse::fromOrderDetail).toList();
        return ResponseEntity.ok(orderDetailResponses);
    }


    // sữa đổi một order_detail nào đó
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(
            @Valid @PathVariable("id") Long id,
            @RequestBody OrderDetailDTO orderDetailDTO
    ) {
        try {
       OrderDetail orderDetail = orderDetailService.updateOrderDetail(id, orderDetailDTO);
            return ResponseEntity.ok().body
                    (orderDetail);
        } catch (DataNotFoundException e) {
           return ResponseEntity.badRequest().body(e.getMessage());
        }
    }




    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(
            @Valid @PathVariable("id") Long id
    ) {
        orderDetailService.deleteById(id);
        return  ResponseEntity.ok().body("delete order detail with id:"
                +id+ "thanh cong");
       // return ResponseEntity.noContent().build();
    }


}
