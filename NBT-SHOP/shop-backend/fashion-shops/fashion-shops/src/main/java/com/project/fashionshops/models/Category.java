package com.project.fashionshops.models;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "categories")
@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "name" , nullable = false)
    private String name;
}
