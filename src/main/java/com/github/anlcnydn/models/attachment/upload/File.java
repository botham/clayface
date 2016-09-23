package com.github.anlcnydn.models.attachment.upload;

import com.github.anlcnydn.interfaces.BotApiObject;
import com.github.anlcnydn.interfaces.Uploadable;

public class File implements Uploadable, BotApiObject {

  private java.io.File file;

  public File(String path) {
    this.file = new java.io.File(path);
  }

  public File(java.io.File file) {
    this.file = file;
  }

  @Override
  public String getType() {
    return "file";
  }

  @Override
  public java.io.File asFile() {
    return file;
  }

}
