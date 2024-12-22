package com.bitsdevelopment.bitshop.service.image;

import org.springframework.web.multipart.MultipartFile;

import com.bitsdevelopment.bitshop.dto.ImageDTO;
import com.bitsdevelopment.bitshop.model.Image;

import java.util.List;

public interface ImageServiceInterface {
  void deleteImageById(Long id);
  void updateImage(Long imageId, MultipartFile file);
  Image getImageById(Long id);
  List<ImageDTO> saveImages(Long productId, List<MultipartFile> files);
}
