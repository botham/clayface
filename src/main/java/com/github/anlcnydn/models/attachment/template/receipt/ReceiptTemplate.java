package com.github.anlcnydn.models.attachment.template.receipt;

import com.github.anlcnydn.Constants;
import com.github.anlcnydn.interfaces.AttachmentPayload;
import com.github.anlcnydn.logger.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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

  private String recipientName;
  private String orderNumber;
  private String currency;
  private String paymentMethod;
  private String orderUrl;
  private String timestamp;
  private ArrayList<ReceiptElement> elements;
  private Address address;
  private ReceiptSummary summary;
  private ArrayList<ReceiptAdjustment> adjustments;

  private ReceiptTemplate(String recipientName, String orderNumber, String currency, String paymentMethod, ReceiptSummary summary) {
    this.recipientName = recipientName;
    this.orderNumber = orderNumber;
    this.currency = currency;
    this.paymentMethod = paymentMethod;
    this.summary = summary;
  }

  public static ReceiptTemplate create(String recipientName, String orderNumber, String currency, String paymentMethod, ReceiptSummary summary) {
    return new ReceiptTemplate(recipientName, orderNumber, currency, paymentMethod, summary);
  }

  public ReceiptTemplate setOrderUrl(String orderUrl) {
    this.orderUrl = orderUrl;
    return this;
  }

  public ReceiptTemplate setTimestamp(String timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  public ReceiptTemplate addReceiptElement(ReceiptElement element) {
    if(elements == null) {
      elements = new ArrayList<>();
    }
    if(elements.size() < 100) {
      elements.add(element);
    }
    return this;
  }

  public ReceiptTemplate addReceiptAdjustment(ReceiptAdjustment adjustment) {
    if(adjustments == null) {
      adjustments = new ArrayList<>();
    }
    adjustments.add(adjustment);
    return this;
  }

  public ReceiptTemplate setAddress(Address address) {
    this.address = address;
    return this;
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
      if(orderUrl != null) {
        receipt.put(ORDER_URL, orderUrl);
      }
      if(timestamp != null) {
        receipt.put(TIMESTAMP, timestamp);
      }
      if(elements != null) {
        JSONArray elementsArr = new JSONArray();
        for(ReceiptElement e :elements) {
          elementsArr.put(e.toJson());
        }
        receipt.put(ELEMENTS, elementsArr);
      }
      if(address != null) {
        receipt.put(ADDRESS, address);
      }
      receipt.put(SUMMARY, summary.toJson());
      if(adjustments != null) {
        JSONArray adjustmentsArr = new JSONArray();
        for(ReceiptAdjustment a: adjustments) {
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
