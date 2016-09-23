package com.github.anlcnydn.models.attachment.template.receipt;

import com.github.anlcnydn.Constants;
import com.github.anlcnydn.logger.Log;
import org.json.JSONException;
import org.json.JSONObject;

public class ReceiptElement {
  private static final String LOG_TAG = ReceiptElement.class.getName();

  private static final String TITLE = "title";
  private static final String SUBTITLE = "subtitle";
  private static final String QUANTITY = "quantity";
  private static final String PRICE = "price";
  private static final String CURRENCY = "currency";
  private static final String IMAGE_URL = "image_url";

  private String title;
  private String subtitle;
  private int quantity;
  private double price;
  private String currency;
  private String imageUrl;

  private ReceiptElement(String title, double price) {
    this.title = title;
    this.price = price;
    this.quantity = 0;
  }

  public ReceiptElement create(String title, double price) {
    return new ReceiptElement(title, price);
  }

  public ReceiptElement setSubtitle(String subtitle) {
    this.subtitle = subtitle;
    return this;
  }

  public ReceiptElement setQuantity(int quantity) {
    this.quantity = quantity;
    return this;
  }

  public ReceiptElement setCurrency(String currency) {
    this.currency = currency;
    return this;
  }

  public ReceiptElement setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
    return this;
  }

  public JSONObject toJson() {
    JSONObject element = new JSONObject();
    try {
      element.put(TITLE, title);
      if(subtitle != null) {
        element.put(SUBTITLE, subtitle);
      }
      if(quantity > 0) {
        element.put(QUANTITY, quantity);
      }
      element.put(PRICE, price);
      if(currency != null) {
        element.put(CURRENCY, currency);
      }
      if(imageUrl != null) {
        element.put(IMAGE_URL, imageUrl);
      }

    } catch (JSONException e) {
      Log.error(LOG_TAG + ".toJson()", Constants.JSON_EXCEPTION_ERROR_MESSAGE, e);
    }
    return element;
  }



}
