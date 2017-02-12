package com.github.anlcnydn.models.attachment.template.receipt;

import com.github.anlcnydn.Constants;
import com.github.anlcnydn.logger.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

public class ReceiptElement {
  private static final String LOG_TAG = ReceiptElement.class.getName();

  private static final String TITLE = "title";
  private static final String SUBTITLE = "subtitle";
  private static final String QUANTITY = "quantity";
  private static final String PRICE = "price";
  private static final String CURRENCY = "currency";
  private static final String IMAGE_URL = "image_url";

  /**
   * Title of item.
   * Required.
   */
  private String title;

  /**
   * Subtitle of item.
   */
  private Optional<String> subtitle = Optional.empty();

  /**
   * Quantity of item.
   */
  private Optional<Integer> quantity = Optional.empty();

  /**
   * Required, but 0 is allowed
   */
  private double price;

  /**
   * Currency of price.
   */
  private Optional<String> currency = Optional.empty();

  /**
   * Image URL of item.
   */
  private Optional<String> imageUrl = Optional.empty();

  private ReceiptElement(String title, double price) {
    this.title = title;
    this.price = price;
  }

  private ReceiptElement(String title, String subtitle, int quantity, double price, String currency,
      String imageUrl) {
    this.title = title;
    this.subtitle = Optional.of(subtitle);
    this.quantity = Optional.of(quantity);
    this.price = price;
    this.currency = Optional.of(currency);
    this.imageUrl = Optional.of(imageUrl);
  }

  public ReceiptElement create(String title, double price) {
    return new ReceiptElement(title, price);
  }

  public ReceiptElement create(String title, String subtitle, int quantity, double price,
      String currency, String imageUrl) {
    return new ReceiptElement(title, subtitle, quantity, price, currency, imageUrl);
  }

  public JSONObject toJson() {
    JSONObject element = new JSONObject();
    try {
      element.put(TITLE, title);
      if (subtitle.isPresent()) {
        element.put(SUBTITLE, subtitle.get());
      }
      if (quantity.isPresent()) {
        element.put(QUANTITY, quantity.get());
      }
      element.put(PRICE, price);
      if (currency.isPresent()) {
        element.put(CURRENCY, currency.get());
      }
      if (imageUrl.isPresent()) {
        element.put(IMAGE_URL, imageUrl.get());
      }
    } catch (JSONException e) {
      Log.error(LOG_TAG + ".toJson()", Constants.JSON_EXCEPTION_ERROR_MESSAGE, e);
    }
    return element;
  }
}
