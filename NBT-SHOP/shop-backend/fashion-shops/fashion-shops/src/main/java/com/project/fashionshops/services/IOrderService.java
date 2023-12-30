package com.project.fashionshops.services;

import com.project.fashionshops.dtos.OrderDTO;
import com.project.fashionshops.exceptions.DataNotFoundException;
import com.project.fashionshops.models.Order;

import java.util.List;

public interface IOrderService {
    Order createOrder(OrderDTO orderDTO) throws Exception;
    Order getOrder(Long id);
    Order updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException;
    void  deleteOrder(long id);

    List<Order> findByUserId(Long userId);
}
