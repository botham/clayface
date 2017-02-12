package com.github.anlcnydn.models.attachment.upload;

import com.github.anlcnydn.interfaces.BotApiObject;
import com.github.anlcnydn.interfaces.Uploadable;

import java.io.File;

public class Image implements Uploadable, BotApiObject {

  private File file;

  private Image(String path) {
    this.file = new File(path);
  }

  private Image(File file) {
    this.file = file;
  }

  public static Image create(String path) {
    return new Image(path);
  }

  public static Image create(File file) {
    return new Image(file);
  }

  @Override
  public String getType() {
    return "image";
  }

  @Override
  public File asFile() {
    return file;
  }

}
