package com.project.fashionshops.services;

import com.project.fashionshops.dtos.CategoryDTO;
import com.project.fashionshops.dtos.OrderDetailDTO;
import com.project.fashionshops.exceptions.DataNotFoundException;
import com.project.fashionshops.models.Category;
import com.project.fashionshops.models.OrderDetail;

import java.util.List;

public interface IOrderDetailService {

    OrderDetail createOrderDetail(OrderDetailDTO neworderDetailDTO) throws Exception;
    OrderDetail getOrderDetail(Long id) throws DataNotFoundException;

    OrderDetail updateOrderDetail
            (Long id, OrderDetailDTO newOrderDetailData) throws DataNotFoundException;
    void  deleteById(Long id);

    List<OrderDetail> findByOrderId(Long orderId); // xoa cung
}

