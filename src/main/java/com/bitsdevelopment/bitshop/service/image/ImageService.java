package com.bitsdevelopment.bitshop.service.image;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bitsdevelopment.bitshop.dto.ImageDTO;
import com.bitsdevelopment.bitshop.exceptions.ResourceNotFoundException;
import com.bitsdevelopment.bitshop.model.Image;
import com.bitsdevelopment.bitshop.model.Product;
import com.bitsdevelopment.bitshop.repository.ImageRepository;
import com.bitsdevelopment.bitshop.service.product.ProductServiceInterface;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

@Service
@RequiredArgsConstructor
public class ImageService implements ImageServiceInterface {
  private final ImageRepository imageRepository;
  private final ProductServiceInterface productServiceInterface;

  @Override
  public void deleteImageById(Long id) {
    imageRepository.findById(id).ifPresentOrElse(imageRepository::delete, () -> {
      throw new ResourceNotFoundException("No image found with id: " + id);
    });
  }

  @Override
  public void updateImage(Long imageId, MultipartFile file) {
    Image image = getImageById(imageId);
    try {
      image.setFileName(file.getOriginalFilename());
      image.setFileType(file.getContentType());
      image.setImage(new SerialBlob(file.getBytes()));
      imageRepository.save(image);
    } catch (IOException | SQLException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  @Override
  public Image getImageById(Long id) {
    return imageRepository.findById(id).orElseThrow(
      () -> new ResourceNotFoundException("No image found with id: " + id)
    );
  }

  @Override
  public List<ImageDTO> saveImages(Long productId, List<MultipartFile> files) {
    Product product = productServiceInterface.getProductById(productId);

    List<ImageDTO> savedImageDto = new ArrayList<>();
    for (MultipartFile file : files) {
        try {
            Image image = new Image();
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            image.setProduct(product);

            String apiPathString = "/api/v1/images/image/download/";
            String downloadUrl = apiPathString + image.getId();
            image.setDownloadURL(downloadUrl);

            Image savedImage = imageRepository.save(image);
            savedImage.setDownloadURL(apiPathString + savedImage.getId());
            imageRepository.save(savedImage);

            ImageDTO imageDto = new ImageDTO();
            imageDto.setId(savedImage.getId());
            imageDto.setFileName(savedImage.getFileName());
            imageDto.setDownloadUrl(savedImage.getDownloadURL());
            savedImageDto.add(imageDto);

        }   catch(IOException | SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }
    return savedImageDto;
  }

}
