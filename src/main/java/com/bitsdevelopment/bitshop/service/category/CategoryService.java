package com.bitsdevelopment.bitshop.service.category;

import org.springframework.stereotype.Service;

import com.bitsdevelopment.bitshop.exceptions.AlreadyExistsException;
import com.bitsdevelopment.bitshop.exceptions.CategoryNotFoundException;
import com.bitsdevelopment.bitshop.model.Category;
import com.bitsdevelopment.bitshop.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements CategoryServiceInterface {
  private final CategoryRepository categoryRepository;

  @Override
  public void deleteCategoryById(Long id) {
    categoryRepository.findById(id)
      .ifPresentOrElse(categoryRepository::delete, () -> {
        throw new CategoryNotFoundException("Category Not Found!");
      });  
  }

  @Override
  public Category addCategory(Category category) {
    return Optional.of(category).filter(c -> !categoryRepository.existsByName(c.getName()))
      .map(categoryRepository::save)
      .orElseThrow(() -> new AlreadyExistsException(category.getName()+" already exists"));
  }

  @Override
  public Category updateCategory(Category category, Long id) {
    return Optional.ofNullable(getCategoryById(id)).map(oldCategory -> {
      oldCategory.setName(category.getName());
      return categoryRepository.save(oldCategory);
    }).orElseThrow(() -> new CategoryNotFoundException("Category Not Found!"));
  }

  @Override
  public List<Category> getAllCategories() {
    return categoryRepository.findAll();
  }

  @Override
  public Category getCategoryById(Long id) {
    return categoryRepository.findById(id)
      .orElseThrow(() -> new CategoryNotFoundException("Category Not Found!"));  
  }

  @Override
  public Category getCategoryByName(String name) {
      return categoryRepository.findByName(name);
  }

}
