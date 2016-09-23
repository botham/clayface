package com.github.anlcnydn.models.attachment.template;

import com.github.anlcnydn.Constants;
import com.github.anlcnydn.logger.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Element {
  private static final String LOG_TAG = Element.class.getName();

  private static final String TITLE = "title";
  private static final String ITEM_URL = "item_url";
  private static final String IMAGE_URL = "image_url";
  private static final String SUBTITLE = "subtitle";
  private static final String BUTTONS = "buttons";

  /**
   * title has a 80 character limit
   */
  private String title;

  private String itemUrl;

  /**
   * Image ratio is 1.91:1
   */
  private String imageUrl;

  /**
   * subtitle has a 80 character limit
   */
  private String subtitle;

  /**
   * buttons is limited to 3
   */
  private ArrayList<Button> buttons;

  public Element(String title) {
    this.title = title;
    this.buttons = new ArrayList<>();
  }

  public Element setItemUrl(String itemUrl) {
    this.itemUrl = itemUrl;
    return this;
  }

  public Element setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
    return this;
  }

  public Element setSubtitle(String subtitle) {
    this.subtitle = subtitle;
    return this;
  }

  public Element addButton(Button button) {
    if(buttons.size() < 3) {
      buttons.add(button);
    }
    return this;
  }

  public JSONObject toJson() {
    JSONObject element = new JSONObject();
    try {
      element.put(TITLE, title);
      if(itemUrl != null) {
        element.put(ITEM_URL, itemUrl);
      }
      if(imageUrl != null) {
        element.put(IMAGE_URL, imageUrl);
      }
      if(subtitle != null) {
        element.put(SUBTITLE, subtitle);
      }
      if(!buttons.isEmpty()) {
        JSONArray buttonsArr = new JSONArray();
        for(Button b : buttons) {
          buttonsArr.put(b.toJson());
        }
        element.put(BUTTONS, buttonsArr);
      }
    } catch (JSONException e) {
      Log.error(LOG_TAG + ".toJson()", Constants.JSON_EXCEPTION_ERROR_MESSAGE, e);
    }
    return element;
  }
}
