package com.github.anlcnydn;

public class FacebookApiException extends RuntimeException {

  public FacebookApiException() {
    super();
  }

  public FacebookApiException(String message) {
    super(message);
  }

  public FacebookApiException(String message, Throwable cause) {
    super(message, cause);
  }
}
