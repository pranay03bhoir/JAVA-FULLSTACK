package com.ecommerce.sbecom.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String productName;
    private String productDescription;
    private String productImage;
    private Integer productQuantity;
    private double productPrice;
    private double productDiscount;
    private double specialPrice;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
