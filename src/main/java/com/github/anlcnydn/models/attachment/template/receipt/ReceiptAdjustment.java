package com.github.anlcnydn.models.attachment.template.receipt;

import com.github.anlcnydn.Constants;
import com.github.anlcnydn.logger.Log;
import org.json.JSONException;
import org.json.JSONObject;

public class ReceiptAdjustment {
  private static final String LOG_TAG = ReceiptAdjustment.class.getName();

  private static final String NAME = "name";
  private static final String AMOUNT = "amount";

  private String name;
  private double amount;

  private ReceiptAdjustment() {
    this.amount = 0;
  }

  public ReceiptAdjustment create() {
    return new ReceiptAdjustment();
  }

  public ReceiptAdjustment setName(String name) {
    this.name = name;
    return this;
  }

  public ReceiptAdjustment setAmount(double amount) {
    this.amount = amount;
    return this;
  }

  public JSONObject toJson() {
    JSONObject adjustment = new JSONObject();
    try {
      if(name != null) {
        adjustment.put(NAME, name);
      }
      if(amount != 0) {
        adjustment.put(AMOUNT, amount);
      }
    } catch (JSONException e) {
      Log.error(LOG_TAG + ".toJson()", Constants.JSON_EXCEPTION_ERROR_MESSAGE, e);
    }
    return adjustment;
  }
}
