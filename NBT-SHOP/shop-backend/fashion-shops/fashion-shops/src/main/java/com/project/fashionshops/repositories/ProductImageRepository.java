package com.project.fashionshops.repositories;

import com.project.fashionshops.models.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage,Long> {
 List<ProductImage> findByProductId(Long productId);

}
