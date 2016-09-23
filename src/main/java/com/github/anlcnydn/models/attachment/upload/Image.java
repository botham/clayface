package com.github.anlcnydn.models.attachment.upload;

import com.github.anlcnydn.interfaces.BotApiObject;
import com.github.anlcnydn.interfaces.Uploadable;

import java.io.File;

public class Image implements Uploadable, BotApiObject {

  private File file;

  public Image(String path) {
    this.file = new File(path);
  }

  public Image(File file) {
    this.file = file;
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
