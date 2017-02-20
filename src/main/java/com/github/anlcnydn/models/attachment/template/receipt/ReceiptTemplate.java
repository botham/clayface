package com.github.anlcnydn.models.attachment.template.receipt;

import com.github.anlcnydn.Constants;
import com.github.anlcnydn.interfaces.AttachmentPayload;
import com.github.anlcnydn.logger.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Optional;

public class ReceiptTemplate implements AttachmentPayload {
  private static final String LOG_TAG = ReceiptTemplate.class.getName();

  private static final String TEMPLATE_TYPE = "template_type";
  private static final String RECIPIENT_NAME = "recipient_name";
  private static final String ORDER_NUMBER = "order_number";
  private static final String CURRENCY = "currency";
  private static final String PAYMENT_METHOD = "payment_method";
  private static final String ORDER_URL = "order_url";
  private static final String TIMESTAMP = "timestamp";
  private static final String ELEMENTS = "elements";
  private static final String ADDRESS = "address";
  private static final String SUMMARY = "summary";
  private static final String ADJUSTMENTS = "adjustments";

  /**
   * Recipient's name.
   * Required.
   */
  private String recipientName;

  /**
   * Order number.
   * Required, must be unique.
   */
  private String orderNumber;

  /**
   * Currency for order.
   * Required.
   */
  private String currency;

  /**
   * Payment method details. This can be a custom string. ex: "Visa 1234".
   * Required.
   */
  private String paymentMethod;

  /**
   * Payment summary.
   * Required.
   */
  private ReceiptSummary summary;

  /**
   * URL of order.
   */
  private Optional<String> orderUrl = Optional.empty();

  /**
   * Timestamp of the order, in seconds.
   */
  private Optional<String> timestamp = Optional.empty();

  /**
   * Items in order.
   */
  private Optional<ArrayList<ReceiptElement>> elements = Optional.empty();

  /**
   * Shipping address.
   */
  private Optional<ReceiptAddress> receiptAddress = Optional.empty();

  /**
   * Payment adjustments.
   */
  private Optional<ArrayList<ReceiptAdjustment>> adjustments = Optional.empty();

  private ReceiptTemplate(String recipientName, String orderNumber, String currency,
      String paymentMethod, ReceiptSummary summary) {
    this.recipientName = recipientName;
    this.orderNumber = orderNumber;
    this.currency = currency;
    this.paymentMethod = paymentMethod;
    this.summary = summary;
  }

  private ReceiptTemplate(String recipientName, String orderNumber, String currency,
      String paymentMethod, ReceiptSummary summary, String orderUrl, String timestamp,
      ArrayList<ReceiptElement> elements, ReceiptAddress receiptAddress,
      ArrayList<ReceiptAdjustment> adjustments) {
    this.recipientName = recipientName;
    this.orderNumber = orderNumber;
    this.currency = currency;
    this.paymentMethod = paymentMethod;
    this.summary = summary;
    this.orderUrl = Optional.of(orderUrl);
    this.timestamp = Optional.of(timestamp);
    this.elements = Optional.of(elements);
    this.receiptAddress = Optional.of(receiptAddress);
    this.adjustments = Optional.of(adjustments);
  }

  public static ReceiptTemplate create(String recipientName, String orderNumber, String currency,
      String paymentMethod, ReceiptSummary summary) {
    return new ReceiptTemplate(recipientName, orderNumber, currency, paymentMethod, summary);
  }

  public static ReceiptTemplate create(String recipientName, String orderNumber, String currency,
      String paymentMethod, ReceiptSummary summary, String orderUrl, String timestamp,
      ArrayList<ReceiptElement> elements, ReceiptAddress receiptAddress,
      ArrayList<ReceiptAdjustment> adjustments) {
    return new ReceiptTemplate(recipientName, orderNumber, currency, paymentMethod, summary,
        orderUrl, timestamp, elements, receiptAddress, adjustments);
  }

  public String getTemplateType() {
    return "receipt";
  }

  @Override
  public JSONObject toJson() {
    JSONObject receipt = new JSONObject();
    try {
      receipt.put(TEMPLATE_TYPE, getTemplateType());
      receipt.put(RECIPIENT_NAME, recipientName);
      receipt.put(ORDER_NUMBER, orderNumber);
      receipt.put(CURRENCY, currency);
      receipt.put(PAYMENT_METHOD, paymentMethod);
      if (orderUrl.isPresent()) {
        receipt.put(ORDER_URL, orderUrl.get());
      }
      if (timestamp.isPresent()) {
        receipt.put(TIMESTAMP, timestamp.get());
      }
      if (elements.isPresent()) {
        JSONArray elementsArr = new JSONArray();
        for (ReceiptElement e : elements.get()) {
          elementsArr.put(e.toJson());
        }
        receipt.put(ELEMENTS, elementsArr);
      }
      if (receiptAddress.isPresent()) {
        receipt.put(ADDRESS, receiptAddress.get().toJson());
      }
      receipt.put(SUMMARY, summary.toJson());
      if (adjustments.isPresent()) {
        JSONArray adjustmentsArr = new JSONArray();
        for (ReceiptAdjustment a : adjustments.get()) {
          adjustmentsArr.put(a.toJson());
        }
        receipt.put(ADJUSTMENTS, adjustmentsArr);
      }
    } catch (JSONException e) {
      Log.error(LOG_TAG + "toJson()", Constants.JSON_EXCEPTION_ERROR_MESSAGE, e);
    }
    return receipt;
  }
}
