package com.bitsdevelopment.bitshop.controllers;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bitsdevelopment.bitshop.dto.ProductDTO;
import com.bitsdevelopment.bitshop.exceptions.AlreadyExistsException;
import com.bitsdevelopment.bitshop.exceptions.ResourceNotFoundException;
import com.bitsdevelopment.bitshop.model.Product;
import com.bitsdevelopment.bitshop.request.AddProductRequest;
import com.bitsdevelopment.bitshop.request.UpdateProductRequest;
import com.bitsdevelopment.bitshop.response.APIResponse;
import com.bitsdevelopment.bitshop.service.product.ProductServiceInterface;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductController {
  private final ProductServiceInterface productServiceInterface;
  
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PostMapping("/product/add")
  public ResponseEntity<APIResponse> addProduct(@RequestBody AddProductRequest product) {
    try {
      Product newProduct = productServiceInterface.addProduct(product);
      ProductDTO productDTO = productServiceInterface.convertToDTO(newProduct);
      return ResponseEntity.ok(new APIResponse("SUCCESS! Product added to the store!", productDTO));
    } catch (AlreadyExistsException e) {
      return ResponseEntity.status(CONFLICT).body(new APIResponse(e.getMessage(), null));
    }
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PutMapping("/product/{productId}/update")
  public ResponseEntity<APIResponse> updateProduct(@RequestBody UpdateProductRequest request, @PathVariable Long productId) {
    try {
      Product updatedProduct = productServiceInterface.updateProduct(request, productId);
      ProductDTO productDTO = productServiceInterface.convertToDTO(updatedProduct);
      return ResponseEntity.ok(new APIResponse("SUCCESS! The product details have been updated.", productDTO));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage(), null));
    }
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @DeleteMapping("/product/{productId}/delete")
  public ResponseEntity<APIResponse> deleteProduct(@PathVariable Long productId) {
    try {
      productServiceInterface.deleteProductById(productId);
      return ResponseEntity.ok(new APIResponse("SUCCESS! Product removed from the store!", productId));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/all")
  public ResponseEntity<APIResponse> getAllProducts() {
    List<Product> products = productServiceInterface.getAllProducts();
    List<ProductDTO> DTOconvertedProducts = productServiceInterface.getDTOConvertedProducts(products);
    return ResponseEntity.ok(new APIResponse("success", DTOconvertedProducts));
  }

  @GetMapping("/product/{productId}")
  public ResponseEntity<APIResponse> getProductById(@PathVariable Long productId) {
    try {
      Product product = productServiceInterface.getProductById(productId);
      ProductDTO productDTO = productServiceInterface.convertToDTO(product);
      return ResponseEntity.ok(new APIResponse("success", productDTO));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/product/by/brand&name")
  public ResponseEntity<APIResponse> getProductByBrandAndName(@RequestParam String brand, @RequestParam String product) {
    try {
      List<Product> products = productServiceInterface.getProductsByBrandAndName(brand, product);
      if (products.isEmpty()) {
        return ResponseEntity.status(NOT_FOUND).body(new APIResponse("No products found.", null));
      }
      List<ProductDTO> DTOconvertedProducts = productServiceInterface.getDTOConvertedProducts(products);
      return ResponseEntity.ok(new APIResponse("SUCCESS", DTOconvertedProducts));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new APIResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/product/by/brand&category")
  public ResponseEntity<APIResponse> getProductByBrandAndCategory(@RequestParam String brand,
      @RequestParam String category) {
    try {
      List<Product> products = productServiceInterface.getProductByBrandAndCategory(brand, category);
      if (products.isEmpty()) {
        return ResponseEntity.status(NOT_FOUND).body(new APIResponse("No products found.", null));
      }
      List<ProductDTO> DTOconvertedProducts = productServiceInterface.getDTOConvertedProducts(products);
      return ResponseEntity.ok(new APIResponse("SUCCESS", DTOconvertedProducts));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new APIResponse("ERROR", e.getMessage()));
    }
  }

  @GetMapping("/product/by/name")
  public ResponseEntity<APIResponse> getProductByName(@RequestParam String name) {
    try {
      List<Product> products = productServiceInterface.getProductsByName(name);
      if (products.isEmpty()) {
        return ResponseEntity.status(NOT_FOUND).body(new APIResponse("No products found.", null));
      }
      List<ProductDTO> DTOconvertedProducts = productServiceInterface.getDTOConvertedProducts(products);
      return ResponseEntity.ok(new APIResponse("SUCCESS", DTOconvertedProducts));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new APIResponse("ERROR", e.getMessage()));
    }
  }

  @GetMapping("/product/by/brand")
  public ResponseEntity<APIResponse> findProductByBrand(@RequestParam String brand) {
    try {
      List<Product> products = productServiceInterface.getProductsByBrand(brand);
      if (products.isEmpty()) {
        return ResponseEntity.status(NOT_FOUND).body(new APIResponse("No products found.", null));
      }
      List<ProductDTO> DTOconvertedProducts = productServiceInterface.getDTOConvertedProducts(products);
      return ResponseEntity.ok(new APIResponse("SUCCESS", DTOconvertedProducts));
    } catch (Exception e) {
      return ResponseEntity.ok(new APIResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/product/by/category/{category}/all")
  public ResponseEntity<APIResponse> findProductByCategory(@PathVariable String category) {
    try {
      List<Product> products = productServiceInterface.getProductsByCategory(category);
      if (products.isEmpty()) {
        return ResponseEntity.status(NOT_FOUND).body(new APIResponse("No products found.", null));
      }
      List<ProductDTO> DTOconvertedProducts = productServiceInterface.getDTOConvertedProducts(products);
      return ResponseEntity.ok(new APIResponse("SUCCESS", DTOconvertedProducts));
    } catch (Exception e) {
      return ResponseEntity.ok(new APIResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/product/count/by/brand&name")
  public ResponseEntity<APIResponse> countProductsByBrandAndName(@RequestParam String brand,
      @RequestParam String name) {
    try {
      var productCount = productServiceInterface.countProductsByBrandAndName(brand, name);
      return ResponseEntity.ok(new APIResponse("SUCCESS", productCount));
    } catch (Exception e) {
      return ResponseEntity.ok(new APIResponse(e.getMessage(), null));
    }
  }

}
