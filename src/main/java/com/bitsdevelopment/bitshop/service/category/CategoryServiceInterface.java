package com.bitsdevelopment.bitshop.service.category;

import java.util.List;

import com.bitsdevelopment.bitshop.model.Category;

public interface CategoryServiceInterface {
  void deleteCategoryById(Long id);
  Category addCategory(Category category);
  Category updateCategory(Category category, Long id);
  
  List<Category> getAllCategories();
  Category getCategoryById(Long id);
  Category getCategoryByName(String name);
}
