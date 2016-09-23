package com.github.anlcnydn.logger;


import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {
  //TODO
  private static final Logger logger = Logger.getLogger("Facebook Bots Api");

  public static void debug(String tag, String message) {
    System.out.println("[DEBUG] -> " + String.format("%s - %s", tag, message));
    logger.fine(String.format("%s - %s", tag, message));
  }

  public static void debug(String tag, Throwable throwable) {
    throwable.printStackTrace();
    System.out.println("[DEBUG] -> " + String.format("%s - %s", tag, throwable.getClass().getSimpleName()));
    logger.log(Level.FINE, tag, throwable);
  }

  public static void debug(String tag, String message, Throwable throwable) {
    System.out.println("[DEBUG] -> " + String.format("%s - %s - %s", tag, message, throwable.getClass().getSimpleName()));
    logger.log(Level.FINE, String.format("%s - %s", tag, message), throwable);
  }

  public static void error(String tag, String message) {
    System.out.println("[ERROR] -> " + String.format("%s - %s", tag, message));
    logger.severe(String.format("%s - %s", tag, message));
  }

  public static void error(String tag, Throwable throwable) {
    System.out.println("[ERROR] -> " + String.format("%s - %s", tag, throwable.getClass().getSimpleName()));
    logger.log(Level.SEVERE, tag, throwable);
  }

  public static void error(String tag, String message, Throwable throwable) {
    System.out.println("[ERROR] -> " + String.format("%s - %s - %s", tag, message, throwable.getClass().getSimpleName()));
    logger.log(Level.SEVERE, String.format("%s - %s", tag, message), throwable);
  }

}
