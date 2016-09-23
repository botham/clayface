package com.github.anlcnydn.models;

import com.github.anlcnydn.interfaces.BotApiObject;

public class BotHttpResult implements BotApiObject {
  private String code;
  private String value;

  public BotHttpResult(String code) {
    this.code = code;
  }

  public BotHttpResult(String code, String value) {
    this.code = code;
    this.value = value;
  }

  public String getCode() {
    return code;
  }

  public String getValue() {
    return value;
  }
}
