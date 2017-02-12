package com.github.anlcnydn.models.attachment.template;

import com.github.anlcnydn.Constants;
import com.github.anlcnydn.FacebookApiException;
import com.github.anlcnydn.interfaces.AttachmentPayload;
import com.github.anlcnydn.logger.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

public class Button {
  private static final String LOG_TAG = Button.class.getName();

  private static final String TYPE = "type";
  private static final String TITLE = "title";
  private static final String URL = "url";
  private static final String PAYLOAD = "payload";

  public enum ButtonType {
    WEB_URL("web_url"), POSTBACK("postback"), CALL("phone_number"), SHARE("element_share"), BUY("payment"),
    LOGIN("account_link"), LOGOUT("account_unlink");

    private String text;

    ButtonType(final String text) {
      this.text = text;
    }

    public String getText() {
      return text;
    }
  }

  /**
   * Can be WEB_URL, POSTBACK, CALL, SHARE, BUY, LOGIN or LOGOUT
   */
  private ButtonType type;

  /**
   * title has a 20 character limit
   */
  private String title;

  /**
   * Required if type is web_url
   */
  private Optional<String> url = Optional.empty();

  /**
   * Required if type is postback or phone_number.
   * payload has a 1000 character limit.
   * phone_number payload format mush be ‘+’ prefix followed by the country code, area code and local number
   */
  private Optional<AttachmentPayload> payload = Optional.empty();

  /**
   * Private constructor for Button with WEB_URL type.
   * @param type
   * @param title
   * @param url
   */
  private Button(ButtonType type, String title, String url) {
    this.type = type;
    this.title = title.length() > 20 ? title.substring(0, 20) : title;
    this.url = Optional.of(url);
  }

  /**
   * Private constructor for Button with POSTBACK and CALL
   * @param type
   * @param title
   * @param payload
   */
  private Button(ButtonType type, String title, AttachmentPayload payload) {
    this.type = type;
    this.title = title.length() > 20 ? title.substring(0, 20) : title;
    this.payload = Optional.of(payload);
  }

  /**
   * Static create method for Button with WEB_URL type
   * @param type
   * @param title
   * @param url
   * @return
   */
  public static Button create(ButtonType type, String title, String url) {
    return new Button(type, title, url);
  }

  /**
   * Static create method for Button with POSTBACK and CALL
   * @param type
   * @param title
   * @param payload
   * @return
   */
  public static Button create(ButtonType type, String title, AttachmentPayload payload) {
    return new Button(type, title, payload);
  }

  public JSONObject toJson() {
    JSONObject button = new JSONObject();
    try {
      button.put(TYPE, type.getText());
      button.put(TITLE, title);
      if (type.getText().equals(ButtonType.WEB_URL.getText()) && url.isPresent()) {
        button.put(URL, url.get());
      } else if (payload.isPresent()){
        button.put(PAYLOAD, payload.get().toJson());
      } else {
        //TODO: Decide what to do
      }
    } catch (JSONException e) {
      Log.error(LOG_TAG + ".toJson()", Constants.JSON_EXCEPTION_ERROR_MESSAGE, e);
    }
    return button;
  }
}
