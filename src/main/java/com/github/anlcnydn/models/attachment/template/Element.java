package com.github.anlcnydn.models.attachment.template;

import com.github.anlcnydn.Constants;
import com.github.anlcnydn.logger.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Element {
  private static final String LOG_TAG = Element.class.getName();

  private static final String TITLE = "title";
  private static final String DEFAULT_ACTION = "default_action";
  private static final String IMAGE_URL = "image_url";
  private static final String SUBTITLE = "subtitle";
  private static final String BUTTONS = "buttons";

  /**
   * title has a 80 character limit and required
   */
  private String title;

  /**
   * Image ratio is 1.91:1
   */
  private Optional<String> imageUrl = Optional.empty();

  /**
   * subtitle has a 80 character limit
   */
  private Optional<String> subtitle = Optional.empty();

  /**
   * buttons is limited to 3
   */
  private Optional<List<Button>> buttons = Optional.empty();

  /**
   * The default_action behaves like a URL Button and contains the same fields except that the title field is not needed.
   */
  private Optional<Action> defaultAction = Optional.empty();

  // Constructors.

  /**
   *
   * @param title
   */
  private Element(String title) {
    this.title = title.length() > 80 ? title.substring(0, 80) : title;
  }

  private Element(String title, String imageUrl) {
    this.title = title.length() > 80 ? title.substring(0, 80) : title;
    this.imageUrl = Optional.of(imageUrl);
  }

  private Element(String title, ArrayList<Button> buttons) {
    this.title = title;
    this.buttons = Optional.of(buttons.size() > 3 ? buttons.subList(0, 3) : buttons);
  }

  private Element(String title, Action defaultAction) {
    this.title = title;
    this.defaultAction = Optional.of(defaultAction);
  }

  private Element(String title, String imageUrl, String subtitle) {
    this.title = title.length() > 80 ? title.substring(0, 80) : title;
    this.imageUrl = Optional.of(imageUrl);
    this.subtitle = Optional.of(subtitle.length() > 80 ? subtitle.substring(0, 80) : subtitle);
  }

  private Element(String title, String imageUrl, ArrayList<Button> buttons) {
    this.title = title;
    this.imageUrl = Optional.of(imageUrl);
    this.buttons = Optional.of(buttons.size() > 3 ? buttons.subList(0, 3) : buttons);
  }

  private Element(String title, String imageUrl, Action defaultAction) {
    this.title = title;
    this.imageUrl = Optional.of(imageUrl);
    this.defaultAction = Optional.of(defaultAction);
  }

  private Element(String title, ArrayList<Button> buttons, Action defaultAction) {
    this.title = title;
    this.buttons = Optional.of(buttons.size() > 3 ? buttons.subList(0, 3) : buttons);
    this.defaultAction = Optional.of(defaultAction);
  }

  private Element(String title, String imageUrl, String subtitle, ArrayList<Button> buttons) {
    this.title = title;
    this.imageUrl = Optional.of(imageUrl);
    this.subtitle = Optional.of(subtitle.length() > 80 ? subtitle.substring(0, 80) : subtitle);
    this.buttons = Optional.of(buttons.size() > 3 ? buttons.subList(0, 3) : buttons);
  }

  private Element(String title, String imageUrl, String subtitle, Action defaultAction) {
    this.title = title;
    this.imageUrl = Optional.of(imageUrl);
    this.subtitle = Optional.of(subtitle.length() > 80 ? subtitle.substring(0, 80) : subtitle);
    this.defaultAction = Optional.of(defaultAction);
  }

  private Element(String title, String imageUrl, ArrayList<Button> buttons, Action defaultAction) {
    this.title = title;
    this.imageUrl = Optional.of(imageUrl);
    this.buttons = Optional.of(buttons.size() > 3 ? buttons.subList(0, 3) : buttons);
    this.defaultAction = Optional.of(defaultAction);
  }

  private Element(String title, String imageUrl, String subtitle, ArrayList<Button> buttons,
      Action defaultAction) {
    this.title = title;
    this.imageUrl = Optional.of(imageUrl);
    this.subtitle = Optional.of(subtitle.length() > 80 ? subtitle.substring(0, 80) : subtitle);
    this.buttons = Optional.of(buttons.size() > 3 ? buttons.subList(0, 3) : buttons);
    this.defaultAction = Optional.of(defaultAction);
  }

  //Static creators.

  /**
   *
   * @param title
   * @return
   */
  public static Element create(String title) {
    return new Element(title);
  }

  public static Element create(String title, String imageUrl) {
    return new Element(title, imageUrl);
  }

  public static Element create(String title, ArrayList<Button> buttons) {
    return new Element(title, buttons);
  }

  public static Element create(String title, Action defaultAction) {
    return new Element(title, defaultAction);
  }

  public static Element create(String title, String imageUrl, String subtitle) {
    return new Element(title, imageUrl, subtitle);
  }

  public static Element create(String title, String imageUrl, ArrayList<Button> buttons) {
    return new Element(title, imageUrl, buttons);
  }

  public static Element create(String title, String imageUrl, Action defaultAction) {
    return new Element(title, imageUrl, defaultAction);
  }

  public static Element create(String title, ArrayList<Button> buttons, Action defaultAction) {
    return new Element(title, buttons, defaultAction);
  }

  public static Element create(String title, String imageUrl, String subtitle,
      ArrayList<Button> buttons) {
    return new Element(title, imageUrl, subtitle, buttons);
  }

  public static Element create(String title, String imageUrl, String subtitle,
      Action defaultAction) {
    return new Element(title, imageUrl, subtitle, defaultAction);
  }

  public static Element create(String title, String imageUrl, ArrayList<Button> buttons,
      Action defaultAction) {
    return new Element(title, imageUrl, buttons, defaultAction);
  }

  public static Element create(String title, String imageUrl, String subtitle,
      ArrayList<Button> buttons, Action defaultAction) {
    return new Element(title, imageUrl, subtitle, buttons, defaultAction);
  }

  public JSONObject toJson() {
    JSONObject element = new JSONObject();
    try {
      element.put(TITLE, title);
      if (imageUrl.isPresent()) {
        element.put(IMAGE_URL, imageUrl.get());
      }
      if (subtitle.isPresent()) {
        element.put(SUBTITLE, subtitle.get());
      }
      if (buttons.isPresent()) {
        JSONArray buttonsArr = new JSONArray();
        for (Button b : buttons.get()) {
          buttonsArr.put(b.toJson());
        }
        element.put(BUTTONS, buttonsArr);
      }
      if (defaultAction.isPresent()) {
        element.put(DEFAULT_ACTION, defaultAction.get().toJson());
      }
    } catch (JSONException e) {
      Log.error(LOG_TAG + ".toJson()", Constants.JSON_EXCEPTION_ERROR_MESSAGE, e);
    }
    return element;
  }
}
