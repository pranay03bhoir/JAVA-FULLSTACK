package com.ecommerce.sbecom.services.serviceImpl;

import com.ecommerce.sbecom.exceptions.APIExceptions;
import com.ecommerce.sbecom.exceptions.ResourceNotFoundException;
import com.ecommerce.sbecom.models.Cart;
import com.ecommerce.sbecom.models.CartItem;
import com.ecommerce.sbecom.models.Product;
import com.ecommerce.sbecom.payload.CartDTO;
import com.ecommerce.sbecom.payload.ProductDTO;
import com.ecommerce.sbecom.repositories.CartItemRespository;
import com.ecommerce.sbecom.repositories.CartRepository;
import com.ecommerce.sbecom.repositories.ProductRepository;
import com.ecommerce.sbecom.services.CartService;
import com.ecommerce.sbecom.utils.AuthUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRespository cartItemRespository;
    private final ModelMapper modelMapper;
    private final AuthUtil authUtil;

    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {

        Cart cart = createCart();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        CartItem cartItem = cartItemRespository.findCartItemByProductIdAndCartId(
                cart.getCartId(),
                productId
        );
        if (cartItem != null) {
            throw new APIExceptions("Product " + product.getProductName() + " already exist");
        }
        if (product.getProductQuantity() == 0) {
            throw new APIExceptions(product.getProductName() + " is not available");
        }
        if (product.getProductQuantity() < quantity) {
            throw new APIExceptions("Please, make an order of the " + product.getProductName() + " less than or equal to the quantity " + product.getProductQuantity());
        }

        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getProductDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());
        cart.getCartItems().add(newCartItem);
        cartItemRespository.save(newCartItem);
        product.setProductQuantity(product.getProductQuantity());
        cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice()) * quantity);
        cartRepository.save(cart);

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        List<CartItem> cartItems = cart.getCartItems();

        List<ProductDTO> productDTOStream = cartItems.stream()
                .map(item -> {
                    ProductDTO map = modelMapper.map(item.getProduct(), ProductDTO.class);
                    map.setProductQuantity(item.getQuantity());
                    return map;
                }).toList();
        cartDTO.setProducts(productDTOStream);
        return cartDTO;
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        if (carts.isEmpty()) {
            throw new APIExceptions("No cart exits");
        }
        List<CartDTO> cartDTOS = carts.stream()
                .map(cart -> {
                    CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
                    List<ProductDTO> productDTO = cart.getCartItems().stream()
                            .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).toList();
                    cartDTO.setProducts(productDTO);
                    return cartDTO;
                }).toList();

        return cartDTOS;
    }

    @Override
    public CartDTO getCart(String emailId, Long cartId) {
        Cart cart = cartRepository.findCartByEmailAndCartId(emailId, cartId);
        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "cartId", cartId);
        }
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        cart.getCartItems()
                .forEach(c -> c.getProduct().setProductQuantity(c.getQuantity()));
        List<ProductDTO> productDTO = cart.getCartItems().stream()
                .map(product -> modelMapper.map(product.getProduct(), ProductDTO.class))
                .toList();
        cartDTO.setProducts(productDTO);
        return cartDTO;
    }

    @Transactional
    @Override
    public CartDTO updateProductQuantityInCart(Long productId, Integer quantity) {

        String emailId = authUtil.loggedInEmail();
        Cart userCart = cartRepository.findCartByEmail(emailId);
        Long cartId = userCart.getCartId();
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        if (product.getProductQuantity() == 0) {
            throw new APIExceptions(product.getProductName() + " is not available");
        }
        if (product.getProductQuantity() < quantity) {
            throw new APIExceptions("Please, make an order of the " + product.getProductName() + " less than or equal to the quantity " + product.getProductQuantity());
        }

        CartItem cartItem = cartItemRespository.findCartItemByProductIdAndCartId(cartId, productId);
        if (cartItem == null) {
            throw new APIExceptions("Product " + product.getProductName() + " not avilable in the cart");
        }

        int newQuantity = cartItem.getQuantity() + quantity;

        if (newQuantity < 0) {
            throw new APIExceptions("The resulting quantity cannot be less than zero");
        }

        if (newQuantity == 0) {
            deleteProductFromCart(cartId, productId);
        } else {
            cartItem.setProductPrice(product.getSpecialPrice());
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setDiscount(product.getProductDiscount());
            cart.setTotalPrice(cart.getTotalPrice() + (cartItem.getProductPrice() * quantity));
            cartRepository.save(cart);
        }
        CartItem updatedCartItem = cartItemRespository.save(cartItem);
        if (updatedCartItem.getQuantity() == 0) {
            cartItemRespository.deleteById(updatedCartItem.getCartItemId());
        }
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        List<CartItem> cartItems = cart.getCartItems();
        Stream<ProductDTO> productStream = cartItems.stream()
                .map(item -> {
                    ProductDTO prd = modelMapper.map(item.getProduct(), ProductDTO.class);
                    prd.setProductQuantity(item.getQuantity());
                    return prd;
                });
        cartDTO.setProducts(productStream.toList());
        return cartDTO;
    }

    @Transactional
    @Override
    public String deleteProductFromCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));
        CartItem cartItem = cartItemRespository.findCartItemByProductIdAndCartId(cartId, productId);
        if (cartItem == null) {
            throw new ResourceNotFoundException("Product", "productId", productId);
        }
        cart.setTotalPrice(
                cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity()));
        cartItemRespository.deleteCartItemByProductIdAndCartId(productId, cartId);

        return "Product "
                + cartItem.getProduct().getProductName()
                + " has been removed from the cart !!!";
    }

    @Override
    public void updateProductsInCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        CartItem cartItem = cartItemRespository.findCartItemByProductIdAndCartId(cartId, productId);
        if (cartItem == null) {
            throw new APIExceptions("Product " + product.getProductName() + " not available in cart");
        }

        double cartPrice = cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity());
        cartItem.setProductPrice(product.getSpecialPrice());
        cart.setTotalPrice(cartPrice + (cartItem.getProductPrice() * cartItem.getQuantity()));
        cartItem = cartItemRespository.save(cartItem);
    }

    private Cart createCart() {
        Cart userCart = cartRepository.findCartByEmail((authUtil.loggedInEmail()));
        if (userCart != null) {
            return userCart;
        }
        Cart cart = new Cart();
        cart.setTotalPrice(0.00);
        cart.setUser(authUtil.loggedInUser());
        return cartRepository.save(cart);
    }
}
