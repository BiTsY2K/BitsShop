package com.bitsdevelopment.bitshop.service.product;

import java.util.List;

import com.bitsdevelopment.bitshop.dto.ProductDTO;
import com.bitsdevelopment.bitshop.model.Product;
import com.bitsdevelopment.bitshop.request.AddProductRequest;
import com.bitsdevelopment.bitshop.request.UpdateProductRequest;

public interface ProductServiceInterface {
  void deleteProductById(Long id);
  Product getProductById(Long id);
  Product addProduct(AddProductRequest product);
  Product updateProduct(UpdateProductRequest product, Long productId);

  List<Product> getAllProducts();
  List<Product> getProductsByCategory(String category);
  List<Product> getProductsByBrand(String brand);
  List<Product> getProductByBrandAndCategory(String brand, String category);
  List<Product> getProductsByName(String name);
  List<Product> getProductsByBrandAndName(String brand, String name);

  Long countProductsByBrandAndName(String brand, String name);

  List<ProductDTO> getDTOConvertedProducts(List<Product> products);
  ProductDTO convertToDTO(Product product);
}
