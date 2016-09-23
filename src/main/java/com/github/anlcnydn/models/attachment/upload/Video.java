package com.github.anlcnydn.models.attachment.upload;

import com.github.anlcnydn.interfaces.BotApiObject;
import com.github.anlcnydn.interfaces.Uploadable;

import java.io.File;

public class Video implements Uploadable, BotApiObject {

  private File file;

  public Video(String path) {
    this.file = new File(path);
  }

  public Video(File file) {
    this.file = file;
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
