package com.bitsdevelopment.bitshop.dto;

import lombok.Data;

@Data
public class ImageDTO {
  private Long id;
  private String fileName;
  private String downloadUrl;
}
