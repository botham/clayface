package com.github.anlcnydn.logger;

import java.io.IOException;

public class FileHandler extends java.util.logging.FileHandler {

  private static final String filePattern = "./ClayfaceBot%g.%u.log";

  public FileHandler() throws IOException, SecurityException {
    super(filePattern, 1024 * 1024 * 10, 50, true);
    setFormatter(new FileFormatter());
  }

  public FileHandler(int limit, int count) throws IOException, SecurityException {
    super(filePattern, limit, count);
    setFormatter(new FileFormatter());
  }

  public FileHandler(String pattern) throws IOException, SecurityException {
    super(pattern);
    setFormatter(new FileFormatter());
  }

  public FileHandler(String pattern, boolean append) throws IOException, SecurityException {
    super(pattern, append);
    setFormatter(new FileFormatter());
  }

  public FileHandler(String pattern, int limit, int count) throws IOException, SecurityException {
    super(pattern, limit, count);
    setFormatter(new FileFormatter());
  }

  public FileHandler(String pattern, int limit, int count, boolean append)
      throws IOException, SecurityException {
    super(pattern, limit, count, append);
    setFormatter(new FileFormatter());
  }
}
