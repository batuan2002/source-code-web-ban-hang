package com.project.fashionshops.services;

import com.project.fashionshops.dtos.OrderDTO;
import com.project.fashionshops.exceptions.DataNotFoundException;
import com.project.fashionshops.models.Order;
import com.project.fashionshops.models.OrderStatus;
import com.project.fashionshops.models.User;
import com.project.fashionshops.repositories.OrderRepository;
import com.project.fashionshops.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{


   private final UserRepository userRepository;
   private final OrderRepository orderRepository;
   private final ModelMapper modelMapper;
    @Override
    public Order createOrder(OrderDTO orderDTO) throws Exception {
        //tìm xem user_id có tồn ta hay ko đã rồi ta mới làm tiếp hiểu kko cu
        userRepository.findById(orderDTO.getUserId());
        User user = userRepository
                .findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException
                        ("Cannot find user with id: " +orderDTO.getUserId()));
       // conver orderDto -> order
        // dung 1 thu vien -> model mapper pom lay ve
        // tạo 1 luồng bảng ánh xạ riêng để kiểm soát việc ánh xạ
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));

        // cập nhật các trường của đơn hàng từ orderDTO
        //tạo 1 đói tượng order trông
        Order order = new Order();
        modelMapper.map(orderDTO, order);
        order.setUser(user);
        order.setOrderDate(new Date()); // lấy thời điểm hiện tại
        order.setStatus(OrderStatus.PENDING);
       // kiểm tra shipping date phải >= ngày hôm nay
        LocalDate shippingDate = orderDTO.getShippingDate() ==  null
                ? LocalDate.now(): orderDTO.getShippingDate();
        if (shippingDate.isBefore(LocalDate.now())){
           throw new DataNotFoundException("Date must be at least today!");
       }
        // dung lenh xoa delete from order where id= 1;

        order.setShippingDate(shippingDate);
       // ko the de don hang ko active dc
        order.setActive(true);
       // luu vao db
        orderRepository.save(order);

      return order;
    }

    @Override
    public Order getOrder(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public Order updateOrder(Long id, OrderDTO orderDTO)
            throws DataNotFoundException {
Order order = orderRepository.findById(id)
        .orElseThrow(()
                -> new DataNotFoundException
                ("Cannot find order id" + id));

User existingUser = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(()
                        -> new DataNotFoundException
                        ("Cannot find user id" + id));

//tạo một luồng bảng an xạ riêng để kiểm xoát việc ánh xạ
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        // cập nhạt các trường của đơn hàng từ orderDTO

        modelMapper.map(orderDTO,order);
        order.setUser(existingUser);
        // luu lai
      return   orderRepository.save(order);


    }

    @Override
    public void deleteOrder(long id) {

        Order order = orderRepository.findById(id).orElse(null);
        //ko xoa cung (hard-delete)=> ma hay xoa mem(soft-delete)
        if (order != null) {
            order.setActive(false);
            orderRepository.save(order);
// day la xoa mem
   }
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
