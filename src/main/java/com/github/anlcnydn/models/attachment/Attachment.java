package com.github.anlcnydn.models.attachment;

import com.github.anlcnydn.Constants;
import com.github.anlcnydn.interfaces.AttachmentPayload;
import com.github.anlcnydn.interfaces.BotApiObject;
import com.github.anlcnydn.logger.Log;
import org.json.JSONException;
import org.json.JSONObject;

public class Attachment implements BotApiObject {
  private static final String LOG_TAG = Attachment.class.getName();

  private static final String TYPE = "type";
  private static final String PAYLOAD = "payload";
  private static final String URL = "url";
  private static final String LOCATION = "location";
  private static final String LAT = "coordinates.lat";
  private static final String LONG = "coordinates.long";

  private String type;
  private AttachmentPayload payload;

  private Attachment(String type, AttachmentPayload payload) {
    this.type = type;
    this.payload = payload;
  }

  public Attachment(JSONObject node) {
    try {
      this.type = node.getString(TYPE);
      if (type.equals(LOCATION)) {
        this.payload = Location.create(
                node.getJSONObject(PAYLOAD).getDouble(LAT),
                node.getJSONObject(PAYLOAD).getDouble(LONG)
        );
      } else {
        this.payload = Multimedia.create(node.getJSONObject(PAYLOAD).getString(URL));
      }
    } catch (JSONException e) {
      Log.error(LOG_TAG + ".constructor", Constants.JSON_EXCEPTION_ERROR_MESSAGE, e);
    }
  }

  public static Attachment create(String type, AttachmentPayload payload) {
    return new Attachment(type, payload);
  }

  public JSONObject toJson() {
    try {
      JSONObject attachment = new JSONObject();
      JSONObject payloadObj = payload.toJson();
      attachment.put(TYPE, type);
      attachment.put(PAYLOAD, payloadObj);
      return attachment;
    } catch (JSONException e) {
      Log.error(LOG_TAG + ".toJson()", Constants.JSON_EXCEPTION_ERROR_MESSAGE, e);
      return null;
    }
  }

  public String getType() {
    return type;
  }

  public AttachmentPayload getPayload() {
    return payload;
  }
}
