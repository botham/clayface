package com.github.anlcnydn.models.attachment.upload;

import com.github.anlcnydn.interfaces.BotApiObject;
import com.github.anlcnydn.interfaces.Uploadable;

import java.io.File;

public class Audio implements Uploadable, BotApiObject {

  private File file;

  private Audio(String path) {
    this.file = new File(path);
  }

  private Audio(File file) {
    this.file = file;
  }

  public static Audio create(String path) {
    return new Audio(path);
  }

  public static Audio create(File file) {
    return new Audio(file);
  }

  @Override
  public String getType() {
    return "audio";
  }

  @Override
  public File asFile() {
    return file;
  }

}
