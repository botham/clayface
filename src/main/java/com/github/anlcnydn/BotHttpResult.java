package com.github.anlcnydn;

import com.github.anlcnydn.interfaces.BotApiObject;

public abstract class BotHttpResult implements BotApiObject {

  public abstract boolean isSuccess();

  public abstract int getStatusCode();
}
