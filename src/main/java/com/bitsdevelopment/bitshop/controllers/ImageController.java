package com.bitsdevelopment.bitshop.controllers;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.sql.SQLException;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bitsdevelopment.bitshop.dto.ImageDTO;
import com.bitsdevelopment.bitshop.exceptions.ResourceNotFoundException;
import com.bitsdevelopment.bitshop.model.Image;
import com.bitsdevelopment.bitshop.response.APIResponse;
import com.bitsdevelopment.bitshop.service.image.ImageServiceInterface;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/images")
public class ImageController {
  private final ImageServiceInterface imageServiceInterface;

  @PostMapping("/upload")
  public ResponseEntity<APIResponse> saveImages( @RequestParam Long productId, @RequestParam List<MultipartFile> files) {
    try {
      List<ImageDTO> imageDtos = imageServiceInterface.saveImages(productId, files);
      return ResponseEntity.ok(new APIResponse("SUCCESS! Image Uploaded.", imageDtos));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new APIResponse("FAILED! Failed to Upload Image.", e.getMessage()));
    }
  }


  @PutMapping("/image/{imageId}/update")
  public ResponseEntity<APIResponse> updateImage(@PathVariable Long imageId, @RequestBody MultipartFile file) {
    try {
      Image image = imageServiceInterface.getImageById(imageId);
      if(image != null) {
        imageServiceInterface.updateImage(imageId, file);
        return ResponseEntity.ok(new APIResponse("SUCCESS! Image Updated.", null));
      }
    } catch (ResourceNotFoundException e) {
        return  ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage(), null));
    }
    return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new APIResponse("FALIED! Failed to Update Image.", INTERNAL_SERVER_ERROR));
  }

  
  @DeleteMapping("/image/{imageId}/delete")
  public ResponseEntity<APIResponse> deleteImage(@PathVariable Long imageId) {
    try {
      Image image = imageServiceInterface.getImageById(imageId);
      if(image != null) {
          imageServiceInterface.deleteImageById( imageId);
          return ResponseEntity.ok(new APIResponse("DELETE! Image Deleted.", null));
      }
    } catch (ResourceNotFoundException e) {
        return  ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage(), null));
    }
    return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new APIResponse("FAILED! Failed to Delete Image.", INTERNAL_SERVER_ERROR));
  }

  
  @GetMapping("/image/{imageId}/download")
  public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) throws SQLException {
    Image image = imageServiceInterface.getImageById(imageId);
    ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1, (int)image.getImage().length()));
    return
      ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(image.getFileType()))
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +image.getFileName() + "\"")
        .body(resource);
  }

}
