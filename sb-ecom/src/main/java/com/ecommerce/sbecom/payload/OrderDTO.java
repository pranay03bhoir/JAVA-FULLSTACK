package com.ecommerce.sbecom.payload;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private Long orderId;
    private String email;
    private List<OrderItemDTO> orderItem;
    private LocalDate orderDate;
    private PaymentDTO paymentDTO;
    private Double totalAmount;
    private String orderStatus;
    private Long addressId;

}
