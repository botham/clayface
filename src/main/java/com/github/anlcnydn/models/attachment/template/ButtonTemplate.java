package com.github.anlcnydn.models.attachment.template;

import com.github.anlcnydn.Constants;
import com.github.anlcnydn.interfaces.AttachmentPayload;
import com.github.anlcnydn.logger.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ButtonTemplate implements AttachmentPayload {
  private static final String LOG_TAG = ButtonTemplate.class.getName();

  private static final String TEMPLATE_TYPE = "template_type";
  private static final String TEXT = "text";
  private static final String BUTTONS = "buttons";

  private String text;
  private ArrayList<Button> buttons;

  private ButtonTemplate() {}

  public static ButtonTemplate create() {
    return new ButtonTemplate();
  }

  public ButtonTemplate addButton(Button button) {
    if (buttons == null) {
      buttons = new ArrayList<>();
    }
    if (buttons.size() < 3) {
      buttons.add(button);
    }
    return this;
  }

  public ButtonTemplate setText(String text) {
    this.text = text;
    return this;
  }

  public String getTemplateType() {
    return "button";
  }

  @Override
  public JSONObject toJson() {
    JSONObject button = new JSONObject();
    JSONArray buttonsArr = new JSONArray();
    try {
      button.put(TEMPLATE_TYPE, getTemplateType());
      button.put(TEXT, text);
      for (Button b : buttons) {
        buttonsArr.put(b.toJson());
      }
      button.put(BUTTONS, buttonsArr);
    } catch (JSONException e) {
      Log.error(LOG_TAG + ".toJson()", Constants.JSON_EXCEPTION_ERROR_MESSAGE, e);
    }
    return button;
  }
}
