package com.ecommerce.sbecom.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long productId;
    private String productName;
    private String productImage;
    private Integer productQuantity;
    private double productPrice;
    private double productDiscount;
    private double specialprice;
}
