package com.bitsdevelopment.bitshop.controllers;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bitsdevelopment.bitshop.exceptions.AlreadyExistsException;
import com.bitsdevelopment.bitshop.exceptions.ResourceNotFoundException;
import com.bitsdevelopment.bitshop.model.Category;
import com.bitsdevelopment.bitshop.response.APIResponse;
import com.bitsdevelopment.bitshop.service.category.CategoryServiceInterface;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
  private final CategoryServiceInterface categoryService;

  @GetMapping("/all")
  public ResponseEntity<APIResponse> getAllCategories() {
    try {
      List<Category> categories = categoryService.getAllCategories();
      return ResponseEntity.ok(new APIResponse("SUCCESS! Categories successfully loaded!", categories));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new APIResponse("ERROR", INTERNAL_SERVER_ERROR));
    }
  }

  @PostMapping("/category/add")
  public ResponseEntity<APIResponse> addCategory(@RequestBody Category name) {
    try {
      Category theCategory = categoryService.addCategory(name);
      return ResponseEntity.ok(new APIResponse("SUCCESS! The category has been added.", theCategory));
    } catch (AlreadyExistsException e) {
      return ResponseEntity.status(CONFLICT).body(new APIResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/category/{id}")
  public ResponseEntity<APIResponse> getCategoryById(@PathVariable Long id) {
    try {
      Category theCategory = categoryService.getCategoryById(id);
      return ResponseEntity.ok(new APIResponse("SUCCESS! Category is here!", theCategory));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/category/{name}")
  public ResponseEntity<APIResponse> getCategoryByName(@PathVariable String name) {
    try {
      Category theCategory = categoryService.getCategoryByName(name);
      return ResponseEntity.ok(new APIResponse("SUCCESS! Category is here!", theCategory));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage(), null));
    }
  }

  @DeleteMapping("/category/{id}/delete")
  public ResponseEntity<APIResponse> deleteCategory(@PathVariable Long id) {
    try {
      categoryService.deleteCategoryById(id);
      return ResponseEntity.ok(new APIResponse("SUCCESS! Category deleted. Your catalog is now updated.", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage(), null));
    }
  }

  @PutMapping("/category/{id}/update")
  public ResponseEntity<APIResponse> updateCategory(@PathVariable Long id, @RequestBody Category category) {
    try {
      Category updatedCategory = categoryService.updateCategory(category, id);
      return ResponseEntity.ok(new APIResponse("Success! Your catalog is now updated.", updatedCategory));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage(), null));
    }
  }

}
