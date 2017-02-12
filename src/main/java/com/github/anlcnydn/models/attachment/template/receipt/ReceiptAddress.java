package com.github.anlcnydn.models.attachment.template.receipt;

import com.github.anlcnydn.Constants;
import com.github.anlcnydn.logger.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

public class ReceiptAddress {
  private static final String LOG_TAG = ReceiptAddress.class.getName();

  private static final String STREET_1 = "street_1";
  private static final String STREET_2 = "street_2";
  private static final String CITY = "city";
  private static final String POSTAL_CODE = "postal_code";
  private static final String STATE = "state";
  private static final String COUNTRY = "country";

  private String street1;
  private Optional<String> street2 = Optional.empty();
  private String city;
  private String postalCode;
  private String state;
  private String country;

  private ReceiptAddress(String street1, String city, String postalCode, String state,
      String country) {
    this.street1 = street1;
    this.city = city;
    this.postalCode = postalCode;
    this.state = state;
    this.country = country;
  }

  private ReceiptAddress(String street1, String street2, String city, String postalCode,
      String state, String country) {
    this.street1 = street1;
    this.street2 = Optional.of(street2);
    this.city = city;
    this.postalCode = postalCode;
    this.state = state;
    this.country = country;
  }

  public static ReceiptAddress create(String street1, String city, String postalCode, String state,
      String country) {
    return new ReceiptAddress(street1, city, postalCode, state, country);
  }

  public static ReceiptAddress create(String street1, String street2, String city,
      String postalCode, String state, String country) {
    return new ReceiptAddress(street1, street2, city, postalCode, state, country);
  }

  public JSONObject toJson() {
    JSONObject address = new JSONObject();
    try {
      address.put(STREET_1, street1);
      if (street2.isPresent()) {
        address.put(STREET_2, street2.get());
      }
      address.put(CITY, city);
      address.put(POSTAL_CODE, postalCode);
      address.put(STATE, state);
      address.put(COUNTRY, country);
    } catch (JSONException e) {
      Log.error(LOG_TAG + ".toJson()", Constants.JSON_EXCEPTION_ERROR_MESSAGE, e);
    }
    return address;
  }
}
