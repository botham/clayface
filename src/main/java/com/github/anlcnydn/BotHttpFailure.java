package com.github.anlcnydn;

public class BotHttpFailure extends BotHttpResult {
  private int statusCode;
  private String message;

  public BotHttpFailure(int statusCode) {
    this.statusCode = statusCode;
  }

  public BotHttpFailure(int statusCode, String message) {
    this.statusCode = statusCode;
    this.message = message;
  }

  @Override
  public boolean isSuccess() {
    return false;
  }

  @Override
  public int getStatusCode() {
    return statusCode;
  }
}
