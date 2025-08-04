package com.ecommerce.sbecom.services;

import com.ecommerce.sbecom.models.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAllCategories();

    void createCategory(Category category);

    String deleteCategory(Long categoryId);
}
