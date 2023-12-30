package com.project.fashionshops.repositories;

import com.project.fashionshops.models.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail,Long> {
List<OrderDetail> findByOrderId(Long orderId);
}
