package com.github.anlcnydn.models.attachment;

import com.github.anlcnydn.Constants;
import com.github.anlcnydn.interfaces.AttachmentPayload;
import com.github.anlcnydn.logger.Log;
import org.json.JSONException;
import org.json.JSONObject;

public class Multimedia implements AttachmentPayload {

  private static final String LOG_TAG = Multimedia.class.getName();

  private static final String URL = "url";

  private String url;

  private Multimedia(String url) {
    this.url = url;
  }

  public String getUrl() {
    return url;
  }

  public static Multimedia create(String url) {
    return new Multimedia(url);
  }

  public JSONObject toJson() {
    try {
      return new JSONObject().put(URL, url);
    } catch (JSONException e) {
      Log.error(LOG_TAG + ".toJson()", Constants.JSON_EXCEPTION_ERROR_MESSAGE, e);
      return null;
    }
  }
}
