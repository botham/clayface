package com.github.anlcnydn;

import java.util.Optional;

public class BotHttpSuccess extends BotHttpResult {
  private int statusCode;
  private String value;

  public BotHttpSuccess(int statusCode) {
    this.statusCode = statusCode;
  }

  public BotHttpSuccess(int statusCode, String value) {
    this.statusCode = statusCode;
    this.value = value;
  }

  @Override
  public boolean isSuccess() {
    return true;
  }

  @Override
  public int getStatusCode() {
    return statusCode;
  }

  public Optional<String> getValue() {
    return Optional.ofNullable(value);
  }
}
