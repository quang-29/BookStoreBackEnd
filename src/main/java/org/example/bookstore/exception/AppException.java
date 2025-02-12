package org.example.bookstore.exception;

import org.example.bookstore.enums.ErrorCode;

public class AppException extends RuntimeException {
  private ErrorCode errorCode;

  public AppException(ErrorCode errorCode) {
    this.errorCode = errorCode;
  }

  public ErrorCode getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(ErrorCode errorCode) {
    this.errorCode = errorCode;
  }
}
