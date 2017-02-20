package com.github.anlcnydn.models.attachment.upload;

import com.github.anlcnydn.interfaces.BotApiObject;
import com.github.anlcnydn.interfaces.Uploadable;

public class File implements Uploadable, BotApiObject {

  private java.io.File file;

  private File(String path) {
    this.file = new java.io.File(path);
  }

  private File(java.io.File file) {
    this.file = file;
  }

  public static File create(String path) {
    return new File(path);
  }

  public static File create(java.io.File file) {
    return new File(file);
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
