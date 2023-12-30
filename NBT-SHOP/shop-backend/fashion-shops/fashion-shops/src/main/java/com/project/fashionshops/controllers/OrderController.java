package com.project.fashionshops.controllers;

import com.project.fashionshops.dtos.OrderDTO;
import com.project.fashionshops.models.Order;
import com.project.fashionshops.services.IOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    // tham chieu orderservice
    private final IOrderService orderService;
    @PostMapping("")
    public ResponseEntity<?> createOrder(
            @RequestBody @Valid OrderDTO orderDTO,
            BindingResult result) {
        try {
            if (result.hasErrors()) {

                List<FieldError> fieldErrorList = result.getFieldErrors();
                List<String> errorMessages = fieldErrorList
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);

            }

            Order orderResponse = orderService.createOrder(orderDTO);
            return ResponseEntity.ok(orderResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/user/{user_id}") // thêm dẫn "user_id"
    //http://localhost:8088/api/v1/orders/user/2
    public ResponseEntity<?> getOrders(@Valid @PathVariable
            ("user_id") Long userId) {
        try {
            List<Order> orders = orderService.findByUserId(userId);
            return ResponseEntity.ok(orders);//"lấy ra danh sách order từ user_id"
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //http://localhost:8088/api/v1/orders/2
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@Valid @PathVariable("id") Long orderId) {
        try {
            Order existingOrder = orderService.getOrder(orderId);
            return ResponseEntity.ok(existingOrder); //"lấy ra chi tiết 1 order nào đó từ order_id"
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PutMapping("/{id}")
    // put http://localhost:8088/api/v1/orders/2
//cv cua admim
    public ResponseEntity<?> updateOrder(
            @Valid @PathVariable long id,
            @Valid @RequestBody OrderDTO orderDTO // orderDTO sau khi da cap nhat
    ) {

        try {
            Order order = orderService.updateOrder(id,orderDTO);
            return ResponseEntity.ok(order);
        }catch (Exception e){
         return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

@DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@Valid @PathVariable long id){
        // xoá mềm => cập nhật trường active = fales
    orderService.deleteOrder(id);
    return ResponseEntity.ok("order deleted successfully");
}

}
