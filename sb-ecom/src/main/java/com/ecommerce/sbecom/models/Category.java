package com.ecommerce.sbecom.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;
    @NotBlank
    @Size(min = 2, message = "Category name should atleast be 2 charaters")
    private String categoryName;
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Product> products;
}
