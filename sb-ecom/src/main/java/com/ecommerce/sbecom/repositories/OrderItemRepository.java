package com.ecommerce.sbecom.repositories;

import com.ecommerce.sbecom.models.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItems, Long> {
}
