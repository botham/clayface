package com.github.anlcnydn.models.attachment.template.receipt;

import com.github.anlcnydn.Constants;
import com.github.anlcnydn.logger.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

public class ReceiptSummary {
  private static final String LOG_TAG = ReceiptSummary.class.getName();

  private static final String SUBTOTAL = "subtotal";
  private static final String SHIPPING_COST = "shipping_cost";
  private static final String TOTAL_TAX = "total_tax";
  private static final String TOTAL_COST = "total_cost";

  private Optional<Double> subtotal = Optional.empty();
  private Optional<Double> shippingCost = Optional.empty();
  private Optional<Double> totalTax = Optional.empty();

  /**
   * Required.
   */
  private double totalCost;

  private ReceiptSummary(double totalCost) {
    this.totalCost = totalCost;
  }

  private ReceiptSummary(double totalCost, double subtotal, double shippingCost, double totalTax) {
    this.totalCost = totalCost;
    this.subtotal = Optional.of(subtotal);
    this.shippingCost = Optional.of(shippingCost);
    this.totalTax = Optional.of(totalTax);
  }

  public static ReceiptSummary create(double totalCost) {
    return new ReceiptSummary(totalCost);
  }

  public static ReceiptSummary create(double totalCost, double subtotal, double shippingCost,
      double totalTax) {
    return new ReceiptSummary(totalCost, subtotal, shippingCost, totalTax);
  }

  public JSONObject toJson() {
    JSONObject summary = new JSONObject();
    try {
      if (subtotal.isPresent()) {
        summary.put(SUBTOTAL, subtotal.get());
      }
      if (shippingCost.isPresent()) {
        summary.put(SHIPPING_COST, shippingCost.get());
      }
      if (totalTax.isPresent()) {
        summary.put(TOTAL_TAX, totalTax.get());
      }
      summary.put(TOTAL_COST, totalCost);
    } catch (JSONException e) {
      Log.error(LOG_TAG + ".toJson()", Constants.JSON_EXCEPTION_ERROR_MESSAGE, e);
    }
    return summary;
  }
}
