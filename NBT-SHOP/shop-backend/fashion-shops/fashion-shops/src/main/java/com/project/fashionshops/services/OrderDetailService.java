package com.project.fashionshops.services;

import com.project.fashionshops.dtos.OrderDetailDTO;
import com.project.fashionshops.exceptions.DataNotFoundException;
import com.project.fashionshops.models.Order;
import com.project.fashionshops.models.OrderDetail;
import com.project.fashionshops.models.Product;
import com.project.fashionshops.repositories.OrderDetailRepository;
import com.project.fashionshops.repositories.OrderRepository;
import com.project.fashionshops.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private  final ProductRepository productRepository;
    @Override
    public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception {
        //tìm xem orderId có tồn tại ko
        Order order = orderRepository.findById(orderDetailDTO.getOrderId()).orElseThrow
                (()-> new DataNotFoundException("Cannot find Order with id : "
                        +orderDetailDTO.getOrderId()));

        // Tìm product theo id

        Product product = productRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find product with id : "
                +orderDetailDTO.getProductId()));
        // tao ra 1 đói tượng order mới
        OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .product(product)
                .price(orderDetailDTO.getPrice())
                .numberOfProducts(orderDetailDTO.getNumberOfProducts())
                .totalMoney(orderDetailDTO.getTotalMoney())
                .color(orderDetailDTO.getColor())
                .build();
        // save vao db
       return orderDetailRepository.save(orderDetail);

    }

    @Override
    public OrderDetail getOrderDetail(Long id) throws DataNotFoundException {
        return orderDetailRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException
                        ("cannot find OrderDetail with id" +id)

        );
    }

    @Override
    public OrderDetail updateOrderDetail
            (Long id, OrderDetailDTO orderDetailDTO)
            throws DataNotFoundException {
      // tim xem order detail co ton tai hya ko
        OrderDetail existingOrderDetail = orderDetailRepository
                .findById(id)
                .orElseThrow(() -> new DataNotFoundException("cannot with order detail with id" + id));
      // order id co thuoc ve mot existing nao ko
        Order existingOrder = orderRepository
                .findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new DataNotFoundException("cannot with order with id" + id));

    //xem san pham product co ton tai ko
        Product existingProduct = productRepository
                .findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("cannot with product with id" + id));

        existingOrderDetail.setPrice(orderDetailDTO.getPrice());
        existingOrderDetail.setNumberOfProducts(orderDetailDTO.getNumberOfProducts());
        existingOrderDetail.setTotalMoney(orderDetailDTO.getTotalMoney());
        existingOrderDetail.setColor(orderDetailDTO.getColor());
        existingOrderDetail.setOrder(existingOrder);
        existingOrderDetail.setProduct(existingProduct);
        return orderDetailRepository.save(existingOrderDetail);
    }

    @Override
    public void deleteById(Long id) {
     orderDetailRepository.deleteById(id);
    }


    @Override
    public List<OrderDetail> findByOrderId(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }
}
