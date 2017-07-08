package com.github.anlcnydn;

public class Constants {
  //HTTP Results
  public static final int OK = 200;
  public static final int INTERNAL_SERVER_ERROR = 500;
  public static final int UNAUTHORIZED = 401;
  public static final int BAD_REQUEST = 400;

  public static final String URL = "https://graph.facebook.com/v2.6/me/messages?access_token=";
  public static final String UPLOAD_URL =
      "https://graph.facebook.com/v2.6/me/message_attachments?access_token=";

  public static final String JSON_EXCEPTION_ERROR_MESSAGE =
      "Something went wrong while trying to convert to json.";

  static final class ValidationConstants {
    static final String ModeField = "hub.mode";
    static final String VerifyTokenField = "hub.verify_token";
    static final String ChallengeField = "hub.challenge";
  }
}
