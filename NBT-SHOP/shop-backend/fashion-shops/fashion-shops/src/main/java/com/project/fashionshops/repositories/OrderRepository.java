package com.project.fashionshops.repositories;

import com.project.fashionshops.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    //tim các đơn hàng của 1 user nào đó
    List<Order> findByUserId(Long userId);
    // list<order > no tra ve danh sach order
    // findByUserId duong dan jpa
    // Long userId dau vao

}
