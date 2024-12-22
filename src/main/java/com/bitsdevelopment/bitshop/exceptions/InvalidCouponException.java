package com.bitsdevelopment.bitshop.exceptions;

public class InvalidCouponException extends RuntimeException {
  public InvalidCouponException(String message) {
    super(message);
  }
}
