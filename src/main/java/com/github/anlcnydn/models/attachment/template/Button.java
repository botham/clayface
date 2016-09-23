package com.github.anlcnydn.models.attachment.template;

import com.github.anlcnydn.Constants;
import com.github.anlcnydn.interfaces.AttachmentPayload;
import com.github.anlcnydn.logger.Log;
import org.json.JSONException;
import org.json.JSONObject;

public class Button {
  private static final String LOG_TAG = Button.class.getName();

  private static final String WEB_URL = "web_url";
  private static final String TYPE = "type";
  private static final String TITLE = "title";
  private static final String URL = "url";
  private static final String PAYLOAD = "payload";

  /**
   * Can be web_url, postback, phone_number
   */
  private String type;

  /**
   * title has a 20 character limit
   */
  private String title;

  /**
   * Required if type is web_url
   */
  private String url;

  /**
   * Required if type is postback or phone_number.
   * payload has a 1000 character limit.
   * phone_number payload format mush be ‘+’ prefix followed by the country code, area code and local number
   */
  private AttachmentPayload payload;

  /**
   * Constructor for Button with web_url type.
   * @param type
   * @param title
   * @param url
   */
  public Button(String type, String title, String url) {
    this.type = type;
    this.title = title.length() > 20 ? title.substring(0, 20) : title;
    this.url = url;
  }

  /**
   * Constructor for Button with postback and phone_number
   * @param type
   * @param title
   * @param payload
   */
  public Button(String type, String title, AttachmentPayload payload) {
    this.type = type;
    this.title = title.length()>20 ? title.substring(0, 20) : title;
    this.payload = payload;
  }

  public JSONObject toJson() {
    JSONObject button = new JSONObject();
    try {
      button.put(TYPE, type);
      button.put(TITLE, title);
      if(type.equals(WEB_URL)) {
        button.put(URL, url);
      } else {
        button.put(PAYLOAD, payload.toJson());
      }
    } catch (JSONException e) {
      Log.error(LOG_TAG + ".toJson()", Constants.JSON_EXCEPTION_ERROR_MESSAGE, e);
    }
    return button;
  }
}
