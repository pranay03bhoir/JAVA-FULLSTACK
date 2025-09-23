package com.ecommerce.sbecom.controllers;

import com.ecommerce.sbecom.config.AppConstants;
import com.ecommerce.sbecom.payload.CategoryDTO;
import com.ecommerce.sbecom.payload.CategoryResponse;
import com.ecommerce.sbecom.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    //    @RequestMapping(value = "/public/categories", method = RequestMethod.GET)
    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_ORDER, required = false) String sortOrder) {
        CategoryResponse categoryResponse = categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    @PostMapping("/public/categories")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO savedCategoryDTO = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(savedCategoryDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deteleCategory(@PathVariable Long categoryId) {
        CategoryDTO deleteCategoryDTO = categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(deleteCategoryDTO, HttpStatus.OK);
    }

    @PutMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@Valid @PathVariable long categoryId, @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO savedCategory = categoryService.updateCategory(categoryId, categoryDTO);
        return new ResponseEntity<>(savedCategory, HttpStatus.OK);
    }
}
