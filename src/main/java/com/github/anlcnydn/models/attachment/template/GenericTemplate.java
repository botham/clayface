package com.github.anlcnydn.models.attachment.template;

import com.github.anlcnydn.Constants;
import com.github.anlcnydn.interfaces.AttachmentPayload;
import com.github.anlcnydn.logger.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GenericTemplate implements AttachmentPayload {
  private static final String LOG_TAG = GenericTemplate.class.getName();

  private static final String TEMPLATE_TYPE = "template_type";
  private static final String ELEMENTS = "elements";

  private ArrayList<Element> elements;

  private GenericTemplate(ArrayList<Element> elements) {
    this.elements = elements;
  }

  public static GenericTemplate create(ArrayList<Element> elements) {
    return new GenericTemplate(elements);
  }

  public GenericTemplate addElement(Element element) {
    if(elements == null) {
      elements = new ArrayList<>();
    }
    if(elements.size() < 10) {
      elements.add(element);
    }
    return this;
  }

  @Override
  public JSONObject toJson() {
    JSONObject generic = new JSONObject();
    try {
      generic.put(TEMPLATE_TYPE, getTemplateType());
      if(!elements.isEmpty()) {
        JSONArray elementsArr = new JSONArray();
        for(Element e: elements) {
          elementsArr.put(e.toJson());
        }
        generic.put(ELEMENTS, elementsArr);
      }
    } catch (JSONException e) {
      Log.error(LOG_TAG + ".toJson()", Constants.JSON_EXCEPTION_ERROR_MESSAGE, e);
    }
    return generic;
  }

  public String getTemplateType() {
    return "generic";
  }
}
