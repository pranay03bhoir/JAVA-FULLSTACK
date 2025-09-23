package com.ecommerce.sbecom.services;

import com.ecommerce.sbecom.exceptions.APIExceptions;
import com.ecommerce.sbecom.exceptions.ResourceNotFoundException;
import com.ecommerce.sbecom.models.Category;
import com.ecommerce.sbecom.payload.CategoryDTO;
import com.ecommerce.sbecom.payload.CategoryResponse;
import com.ecommerce.sbecom.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);
        List<Category> allCategories = categoryPage.getContent();
        if (allCategories.isEmpty()) {
            throw new APIExceptions("No categories present.");
        }
        List<CategoryDTO> categoryDTOS = allCategories.stream()
                .map((category -> modelMapper.map(category, CategoryDTO.class)))
                .toList();
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());
        return categoryResponse;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category existingCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if (existingCategory != null) {
            throw new APIExceptions("Category with the name " + category.getCategoryName() + " already exists");
        }
        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
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
        return modelMapper.map(categoryTobeDeleted, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO) {
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

        Category category = modelMapper.map(categoryDTO, Category.class);
        Optional<Category> existingCategory = categoryRepository.findById(categoryId);
        Category savedCategory = existingCategory.orElseThrow(() -> new ResourceNotFoundException("Category", "categoryIs", categoryId));
        savedCategory.setCategoryName(category.getCategoryName());
        Category updatedCategory = categoryRepository.save(savedCategory);
        return modelMapper.map(updatedCategory, CategoryDTO.class);

    }
}
