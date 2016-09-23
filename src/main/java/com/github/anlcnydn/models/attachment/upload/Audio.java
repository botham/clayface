package com.github.anlcnydn.models.attachment.upload;

import com.github.anlcnydn.interfaces.BotApiObject;
import com.github.anlcnydn.interfaces.Uploadable;

import java.io.File;

public class Audio implements Uploadable, BotApiObject {

  private File file;

  public Audio(String path) {
    this.file = new File(path);
  }

  public Audio(File file) {
    this.file = file;
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
