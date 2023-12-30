package com.project.fashionshops.models;


import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.*;

import java.time.LocalDateTime;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass

public class BaseEntity {
//BaseEntitylớp này cung cấp một
// cấu trúc chung để các thực thể khác
// tự động theo dõi dấu thời gian tạo và
// cập nhật lần cuối

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @PrePersist
    protected void onCreate(){
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void  onUpdate(){
        updatedAt = LocalDateTime.now();
    }

}
