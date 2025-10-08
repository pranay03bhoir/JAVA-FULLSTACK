package com.ecommerce.sbecom.services.serviceImpl;

import com.ecommerce.sbecom.exceptions.ResourceNotFoundException;
import com.ecommerce.sbecom.models.Category;
import com.ecommerce.sbecom.models.Product;
import org.springframework.beans.factory.annotation.Value;
import com.ecommerce.sbecom.payload.ProductDTO;
import com.ecommerce.sbecom.payload.ProductResponse;
import com.ecommerce.sbecom.repositories.CategoryRepository;
import com.ecommerce.sbecom.repositories.ProductRepository;
import com.ecommerce.sbecom.services.FileService;
import com.ecommerce.sbecom.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {


    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final FileService fileService;
    private final ModelMapper modelMapper;
    @Value("${project.images}")
    private String path;

    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "Category_id", categoryId));
        Product product = modelMapper.map(productDTO, Product.class);
        product.setProductImage("default.png");
        product.setCategory(category);
        double specialPrice = product.getProductPrice() - ((product.getProductDiscount() * 0.01) * product.getProductPrice());
        product.setSpecialPrice(specialPrice);
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductResponse searchByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "Category", categoryId));
        List<Product> products = productRepository.findByCategoryOrderByProductPriceAsc(category);
        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductResponse searchProductByKeyword(String keyword) {
        List<Product> products = productRepository.findByProductNameLikeIgnoreCase("%" + keyword + "%");
        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Product productFromDB = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "ProductId", productId));
        Product product = modelMapper.map(productDTO, Product.class);
        productFromDB.setProductName(product.getProductName());
        productFromDB.setProductDescription(product.getProductDescription());
        productFromDB.setProductQuantity(product.getProductQuantity());
        productFromDB.setProductDiscount(product.getProductDiscount());
        productFromDB.setProductPrice(product.getProductPrice());
        double specialPrice = product.getProductPrice() - ((product.getProductDiscount() * 0.01) * product.getProductPrice());
        product.setSpecialPrice(specialPrice);
        Product savedProduct = productRepository.save(productFromDB);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        productRepository.delete(product);
        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product productFromDB = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        //Upload image to the server.
        //Get the file name of uploaded image.
//        String path = "/images";
        String fileName = fileService.uploadImage(path, image);
        // updating the new file name to product.
        productFromDB.setProductImage(fileName);
        // save updated product
        Product updatedProduct = productRepository.save(productFromDB);
        //return DTO after mapping product to DTO
        return modelMapper.map(productFromDB, ProductDTO.class);
    }


}
