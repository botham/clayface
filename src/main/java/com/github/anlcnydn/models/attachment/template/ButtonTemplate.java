package com.github.anlcnydn.models.attachment.template;

import com.github.anlcnydn.Constants;
import com.github.anlcnydn.interfaces.AttachmentPayload;
import com.github.anlcnydn.logger.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ButtonTemplate implements AttachmentPayload {
  private static final String LOG_TAG = ButtonTemplate.class.getName();

  private static final String TEMPLATE_TYPE = "template_type";
  private static final String TEXT = "text";
  private static final String BUTTONS = "buttons";

  /**
   * UTF-8 encoded text of up to 640 characters that appears the in main body
   */
  private String text;

  /**
   * List of, one to three, buttons that appear as call-to-actions
   */
  private List<Button> buttons;

  /**
   * Private constructor of ButtonTemplate
   * @param text
   * @param buttons
   */
  private ButtonTemplate(String text, ArrayList<Button> buttons) {
    this.text = text.length() > 640 ? text.substring(0, 640) : text;
    this.buttons = buttons.size() > 3 ? buttons.subList(0, 3) : buttons;
  }

  /**
   * Public create method of ButtonTemplate
   * @param text
   * @param buttons
   * @return
   */
  public static ButtonTemplate create(String text, ArrayList<Button> buttons) {
    return new ButtonTemplate(text, buttons);
  }

  /**
   * Value must be "button"
   * @return "button"
   */
  public String getTemplateType() {
    return "button";
  }

  @Override
  public JSONObject toJson() {
    JSONObject payload = new JSONObject();
    JSONArray buttonsArr = new JSONArray();
    try {
      payload.put(TEMPLATE_TYPE, getTemplateType());
      payload.put(TEXT, text);
      for (Button b : buttons) {
        buttonsArr.put(b.toJson());
      }
      payload.put(BUTTONS, buttonsArr);
    } catch (JSONException e) {
      Log.error(LOG_TAG + ".toJson()", Constants.JSON_EXCEPTION_ERROR_MESSAGE, e);
    }
    return payload;
  }
}
