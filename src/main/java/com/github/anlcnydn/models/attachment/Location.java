package com.github.anlcnydn.models.attachment;

import com.github.anlcnydn.Constants;
import com.github.anlcnydn.interfaces.AttachmentPayload;
import com.github.anlcnydn.logger.Log;
import org.json.JSONException;
import org.json.JSONObject;

public class Location implements AttachmentPayload {

  private static final String LOG_TAG = Location.class.getName();

  private static final String LAT = "lat";
  private static final String LONG = "long";

  private double lat;
  private double lng;

  private Location(double lat, double lng) {
    this.lat = lat;
    this.lng = lng;
  }

  public static Location create(double lat, double lng) {
    return new Location(lat, lng);
  }

  public double getLat() {
    return lat;
  }

  public double getLng() {
    return lng;
  }

  public JSONObject toJson() {
    JSONObject payload = new JSONObject();
    try {
      JSONObject actual = new JSONObject();
      actual.put(LAT, lat);
      actual.put(LONG, lng);
      payload.put("coordinates", actual);
    } catch (JSONException e) {
      Log.error(LOG_TAG + ".toJson()", Constants.JSON_EXCEPTION_ERROR_MESSAGE, e);
    }
    return payload;
  }
}
