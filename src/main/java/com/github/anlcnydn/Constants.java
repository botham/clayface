package com.github.anlcnydn;

public class Constants {
  //HTTP Results
  public static final String OK = "Ok";
  public static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
  public static final String UNAUTHORIZED = "Unauthorized";
  public static final String BAD_REQUEST = "Bad Request";

  public static final String URL = "https://graph.facebook.com/v2.6/me/messages?access_token=";

  public static final String JSON_EXCEPTION_ERROR_MESSAGE =
      "Something went wrong while trying to convert to json.";

  static final class ValidationConstants {
    public static final String MODE_FIELD = "hub.mode";
    public static final String VERIFY_TOKEN_FIELD = "hub.verify_token";
    public static final String CHALLENGE_FIELD = "hub.challenge";
  }
}
