package com.github.anlcnydn.models.attachment.template.receipt;

import com.github.anlcnydn.Constants;
import com.github.anlcnydn.logger.Log;
import org.json.JSONException;
import org.json.JSONObject;

public class ReceiptSummary {
  private static final String LOG_TAG = ReceiptSummary.class.getName();

  private static final String SUBTOTAL = "subtotal";
  private static final String SHIPPING_COST = "shipping_cost";
  private static final String TOTAL_TAX = "total_tax";
  private static final String TOTAL_COST = "total_cost";

  private double subtotal;
  private double shippingCost;
  private double totalTax;
  private double totalCost;

  private ReceiptSummary(double totalCost) {
    this.totalCost = totalCost;
    this.subtotal = 0;
    this.shippingCost = 0;
    this.totalTax = 0;
  }

  public static ReceiptSummary create(double totalCost) {
    return new ReceiptSummary(totalCost);
  }

  public ReceiptSummary setSubtotal(double subtotal) {
    this.subtotal = subtotal;
    return this;
  }

  public ReceiptSummary setShippingCost(double shippingCost) {
    this.shippingCost = shippingCost;
    return this;
  }

  public ReceiptSummary setTotalTax(double totalTax) {
    this.totalTax = totalTax;
    return this;
  }

  public JSONObject toJson() {
    JSONObject summary = new JSONObject();
    try {
      if(subtotal != 0) {
        summary.put(SUBTOTAL, subtotal);
      }
      if(shippingCost != 0) {
        summary.put(SHIPPING_COST, shippingCost);
      }
      if(totalTax != 0) {
        summary.put(TOTAL_TAX, totalTax);
      }
      summary.put(TOTAL_COST, totalCost+"");
    } catch (JSONException e) {
      Log.error(LOG_TAG + ".toJson()", Constants.JSON_EXCEPTION_ERROR_MESSAGE, e);
    }
    return summary;
  }
}
