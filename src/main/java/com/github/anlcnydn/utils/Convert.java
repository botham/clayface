package com.github.anlcnydn.utils;

import com.github.anlcnydn.logger.Log;
import org.json.JSONException;
import org.json.JSONObject;

public class Convert {
  private static final String LOG_TAG = Convert.class.getSimpleName();

  /**
   *
   * @param str
   * @return
   */
  public static JSONObject toJson(String str) {
    try {
      return new JSONObject(str);
    } catch (JSONException e) {
      Log.error(LOG_TAG, "String could not be converted to JSON, thrown JSONException", e);
    }
    return null;
  }
}
