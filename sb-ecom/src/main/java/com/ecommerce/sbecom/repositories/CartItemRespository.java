package com.ecommerce.sbecom.repositories;

import com.ecommerce.sbecom.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRespository extends JpaRepository<CartItem, Long> {
    @Query(
            "SELECT ci FROM CartItem ci WHERE ci.cart.cartId = ?1 AND ci.product.productId = ?2"
    )
    CartItem findCartItemByProductIdAndCartId(Long cartId, Long productId);

    @Query("DELETE FROM CartItem ci where ci.product.productId = ?1 AND ci.cart.cartId = ?2")
    @Modifying
    void deleteCartItemByProductIdAndCartId(Long productId, Long cartId);
}
