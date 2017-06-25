package com.github.anlcnydn.models.quickreply;


import com.github.anlcnydn.Constants;
import com.github.anlcnydn.FacebookApiException;
import com.github.anlcnydn.interfaces.BotApiObject;
import com.github.anlcnydn.logger.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Optional;

public class QuickReply implements BotApiObject {
  private static final String LOG_TAG = QuickReply.class.getSimpleName();

  private static final String CONTENT_TYPE = "content_type";
  private static final String TITLE = "title";
  private static final String PAYLOAD = "payload";
  private static final String IMAGE_URL = "image_url";

  public enum ContentType {
    TEXT("text"), LOCATION("location");

    private String type;

    ContentType(final String text) {
      this.type = text;
    }

    public String getType() {
      return type;
    }
  }

  private ContentType contentType;

  // Caption of button, required only if content_type is text
  private Optional<String> title = Optional.empty();

  // Custom data that will be sent back to you via webhook, required only if content_type is text
  private Optional<String> payload = Optional.empty();

  //URL of image for text quick replies, not required
  private Optional<String> imageUrl = Optional.empty();

  private QuickReply(JSONObject node) {
    try {
      this.contentType = ContentType.valueOf(node.getString(CONTENT_TYPE));
      if (node.has(TITLE)) {
        this.title = Optional.of(node.getString(TITLE));
      }
      if (node.has(PAYLOAD)) {
        this.payload = Optional.of(node.getString(PAYLOAD));
      }
      if (node.has(IMAGE_URL)) {
        this.imageUrl = Optional.of(node.getString(IMAGE_URL));
      }
    } catch (JSONException e) {
      Log.error(LOG_TAG + ".constructor", Constants.JSON_EXCEPTION_ERROR_MESSAGE, e);
    }
  }

  private QuickReply(ContentType contentType) {
    this.contentType = contentType;
  }

  private QuickReply(ContentType contentType, String title) {
    this.contentType = contentType;
    this.title = Optional.of(title);
  }


  private QuickReply(ContentType contentType, String title, String payload) {
    this.contentType = contentType;
    this.title = Optional.of(title);
    this.payload = Optional.of(payload);
  }

  private QuickReply(ContentType contentType, String title, String payload, String imageUrl) {
    this.contentType = contentType;
    this.title = Optional.of(title);
    this.payload = Optional.of(payload);
    this.imageUrl = Optional.of(imageUrl);
  }

  public static QuickReply create(ContentType contentType) throws FacebookApiException {
    if (contentType == ContentType.TEXT) {
      // title and payload required if contentType is text
      throw new FacebookApiException();
    }
    return new QuickReply(contentType);
  }

  public static QuickReply create(ContentType contentType, String title)
      throws FacebookApiException {
    if (contentType == ContentType.TEXT) {
      // title and payload required if contentType is text
      throw new FacebookApiException();
    }
    return new QuickReply(contentType, title);
  }


  public static QuickReply create(ContentType contentType, String title, String payload) {
    return new QuickReply(contentType, title, payload);
  }

  public static QuickReply create(ContentType contentType, String title, String payload,
      String imageUrl) {
    return new QuickReply(contentType, title, payload, imageUrl);
  }

  public static QuickReply create(JSONObject node) {
    return new QuickReply(node);
  }

  /**
   *
   * @return
   * {
   *    "content_type":"text",
   *    "title":"Red",
   *    "payload":"DEVELOPER_DEFINED_PAYLOAD_FOR_PICKING_RED",
   *    "image_url":"http://petersfantastichats.com/img/red.png
   * }
   */
  public JSONObject toJson() {
    try {
      JSONObject quickReply = new JSONObject();
      quickReply.put(CONTENT_TYPE, contentType.getType());
      if (title.isPresent()) {
        quickReply.put(TITLE, title.get());
      }
      if (payload.isPresent()) {
        quickReply.put(PAYLOAD, payload.get());
      }
      if (imageUrl.isPresent()) {
        quickReply.put(IMAGE_URL, imageUrl.get());
      }
      return quickReply;
    } catch (JSONException e) {
      Log.error(LOG_TAG + ".toJson()", Constants.JSON_EXCEPTION_ERROR_MESSAGE, e);
      return null;
    }
  }

}
