package com.github.anlcnydn.models.attachment.template.receipt;

import com.github.anlcnydn.Constants;
import com.github.anlcnydn.logger.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

public class ReceiptAdjustment {
  private static final String LOG_TAG = ReceiptAdjustment.class.getName();

  private static final String NAME = "name";
  private static final String AMOUNT = "amount";

  private Optional<String> name = Optional.empty();
  private Optional<Double> amount = Optional.empty();

  private ReceiptAdjustment() {}

  private ReceiptAdjustment(String name, Double amount) {
    this.name = Optional.of(name);
    this.amount = Optional.of(amount);
  }

  public ReceiptAdjustment create() {
    return new ReceiptAdjustment();
  }

  public ReceiptAdjustment create(String name, Double amount) {
    return new ReceiptAdjustment(name, amount);
  }

  public JSONObject toJson() {
    JSONObject adjustment = new JSONObject();
    try {
      if (name.isPresent()) {
        adjustment.put(NAME, name.get());
      }
      if (amount.isPresent()) {
        adjustment.put(AMOUNT, amount.get());
      }
    } catch (JSONException e) {
      Log.error(LOG_TAG + ".toJson()", Constants.JSON_EXCEPTION_ERROR_MESSAGE, e);
    }
    return adjustment;
  }
}
