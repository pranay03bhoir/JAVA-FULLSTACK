package com.ecommerce.sbecom.services;

import com.ecommerce.sbecom.exceptions.APIExceptions;
import com.ecommerce.sbecom.exceptions.ResourceNotFoundException;
import com.ecommerce.sbecom.models.Category;
import com.ecommerce.sbecom.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        List<Category> allCategories = categoryRepository.findAll();
        if (allCategories.isEmpty()) {
            throw new APIExceptions("No categories present.");
        }
        return allCategories;
    }

    @Override
    public void createCategory(Category category) {
        Category existingCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if (existingCategory != null) {
            throw new APIExceptions("Category with the name " + category.getCategoryName() + " already exists");
        }
        categoryRepository.save(category);
    }

    @Override
    public String deleteCategory(Long categoryId) {
////        List<Category> categories = categoryRepository.findAll();
//        List<Category> categories = getAllCategories();
//        Category category = categories.stream()
//                .filter(c -> c.getCategoryId().equals(categoryId))
//                .findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found"));


//        Category category = categories.stream()
//                .filter(c -> c.getCategoryId().equals(categoryId))
//                .findFirst().orElse(null);
//
//        if (category == null) {
//            return "Catrgory not found";
//        }

//        categoryRepository.delete(category);
//        return "Category with categoryId " + categoryId + " deleted successfully !!";
        Optional<Category> existingCategory = categoryRepository.findById(categoryId);
        Category categoryTobeDeleted = existingCategory.orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        categoryRepository.delete(categoryTobeDeleted);
//        return categoryRepository.delete(categoryTobeDeleted);
        return "Category with ID: " + categoryId + " deleted successfdully";
    }

    @Override
    public Category updateCategory(Long categoryId, Category category) {
//        List<Category> categories = categoryRepository.findAll();
//        List<Category> categories = getAllCategories();
//        Optional<Category> optionalCategory = categories.stream()
//                .filter(c -> c.getCategoryId().equals(categoryId))
//                .findFirst();
//        if (optionalCategory.isPresent()) {
//            Category existingCategory = optionalCategory.get();
//            existingCategory.setCategoryName(category.getCategoryName());
//            return categoryRepository.save(existingCategory);
//        } else {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found");
//        }

        Optional<Category> existingCategory = categoryRepository.findById(categoryId);
        Category savedCategory = existingCategory.orElseThrow(() -> new ResourceNotFoundException("Category", "categoryIs", categoryId));
        savedCategory.setCategoryName(category.getCategoryName());
        return categoryRepository.save(savedCategory);

    }
}
