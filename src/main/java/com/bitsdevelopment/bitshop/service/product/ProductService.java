package com.bitsdevelopment.bitshop.service.product;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.bitsdevelopment.bitshop.dto.ImageDTO;
import com.bitsdevelopment.bitshop.dto.ProductDTO;
import com.bitsdevelopment.bitshop.exceptions.AlreadyExistsException;
import com.bitsdevelopment.bitshop.exceptions.ProductNotFoundException;
import com.bitsdevelopment.bitshop.model.Category;
import com.bitsdevelopment.bitshop.model.Image;
import com.bitsdevelopment.bitshop.model.Product;
import com.bitsdevelopment.bitshop.repository.CategoryRepository;
import com.bitsdevelopment.bitshop.repository.ImageRepository;
import com.bitsdevelopment.bitshop.repository.ProductRepository;
import com.bitsdevelopment.bitshop.request.AddProductRequest;
import com.bitsdevelopment.bitshop.request.UpdateProductRequest;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements ProductServiceInterface {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;
  private final ImageRepository imageRepository;
  private final ModelMapper modelmapper;

  private boolean productExists(String name , String brand) {
    return productRepository.existsByBrandAndName(name, brand);
  }

  @Override
  public void deleteProductById(Long id) {
    productRepository.findById(id).ifPresentOrElse(productRepository::delete, () -> { 
      throw new ProductNotFoundException("Product Not Found!");
    });
  }

  @Override
  public Product getProductById(Long id) {
    return productRepository.findById(id).orElseThrow(() -> { 
      throw new ProductNotFoundException("Product Not Found!"); 
    });
  }

  @Override
  public Product addProduct(AddProductRequest request) {
    if (productExists(request.getName(), request.getBrand())){
      throw new AlreadyExistsException(request.getBrand() +" "+request.getName()+ " already exists, you may update this product instead!");
    }

    Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
      .orElseGet(() -> {
        Category newCategory = new Category(request.getCategory().getName());
        return categoryRepository.save(newCategory);
      });
    request.setCategory(category);
    return productRepository.save(createProduct(request, category));
  }

  private Product createProduct(AddProductRequest request, Category category) {
    return new Product(
      request.getName(),
      request.getBrand(),
      request.getPrice(),
      request.getDescription(),
      request.getInventory(),
      category
    );
  }

  @Override
  public Product updateProduct(UpdateProductRequest request, Long productId) {
    return productRepository.findById(productId)
      .map(existingProduct -> updateExisProduct(existingProduct, request))
      .map(productRepository::save)
      .orElseThrow(() -> new ProductNotFoundException("Product Not Found!"));
  }

  private Product updateExisProduct(Product existingProduct, UpdateProductRequest request) {
    existingProduct.setName(request.getName());
    existingProduct.setBrand(request.getBrand());
    existingProduct.setPrice(request.getPrice());
    existingProduct.setDescription(request.getDescription());
    existingProduct.setInventory(request.getInventory());

    Category category = categoryRepository.findByName(request.getCategory().getName());
    existingProduct.setCategory(category);
    return existingProduct;
  }

  @Override
  public List<Product> getAllProducts() {
    return productRepository.findAll();
  }

  @Override
  public List<Product> getProductsByCategory(String category) {
    return productRepository.findByCategoryName(category);
  }

  @Override
  public List<Product> getProductsByBrand(String brand) {
    return productRepository.findByBrand(brand);
  }

  @Override
  public List<Product> getProductByBrandAndCategory(String category, String brand) {
    return productRepository.findByCategoryNameAndBrand(category, brand);
  }

  @Override
  public List<Product> getProductsByName(String name) {
    return productRepository.findByName(name);
  }

  @Override
  public List<Product> getProductsByBrandAndName(String brand, String name) {
    return productRepository.findByBrandAndName(brand, name);
  }

  @Override
  public Long countProductsByBrandAndName(String brand, String name) {
    return productRepository.countByBrandAndName(brand, name);
  }

  @Override
    public List<ProductDTO> getDTOConvertedProducts(List<Product> products) {
      return products.stream().map(this::convertToDTO).toList();
    }

  @Override
  public ProductDTO convertToDTO(Product product) {
    ProductDTO productDTO = modelmapper.map(product, ProductDTO.class);
    List<Image> images = imageRepository.findByProductId(product.getId());
    List<ImageDTO> imageDTOs = images.stream().map(image -> modelmapper.map(image, ImageDTO.class)).toList();
    productDTO.setImages((imageDTOs));
    return productDTO;
  }

}
