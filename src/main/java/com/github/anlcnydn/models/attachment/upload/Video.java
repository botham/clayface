package com.github.anlcnydn.models.attachment.upload;

import com.github.anlcnydn.interfaces.BotApiObject;
import com.github.anlcnydn.interfaces.Uploadable;

import java.io.File;

public class Video implements Uploadable, BotApiObject {

  private File file;

  private Video(String path) {
    this.file = new File(path);
  }

  private Video(File file) {
    this.file = file;
  }

  public static Video create(String path) {
    return new Video(path);
  }

  public static Video create(File file) {
    return new Video(file);
  }

  @Override
  public String getType() {
    return "video";
  }

  @Override
  public File asFile() {
    return file;
  }

}
