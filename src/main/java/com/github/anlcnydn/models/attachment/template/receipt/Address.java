package com.github.anlcnydn.models.attachment.template.receipt;

import com.github.anlcnydn.Constants;
import com.github.anlcnydn.logger.Log;
import org.json.JSONException;
import org.json.JSONObject;

public class Address {
  private static final String LOG_TAG = Address.class.getName();

  private static final String STREET_1 = "street_1";
  private static final String STREET_2 = "street_2";
  private static final String CITY = "city";
  private static final String POSTAL_CODE = "postal_code";
  private static final String STATE = "state";
  private static final String COUNTRY = "country";

  private String street1;
  private String street2;
  private String city;
  private String postalCode;
  private String state;
  private String country;

  private Address(String street1, String city, String postalCode, String state, String country) {
    this.street1 = street1;
    this.city = city;
    this.postalCode = postalCode;
    this.state = state;
    this.country = country;
  }

  public static Address create(String street1, String city, String postalCode, String state,
      String country) {
    return new Address(street1, city, postalCode, state, country);
  }

  public Address setStreetTwo(String street2) {
    this.street2 = street2;
    return this;
  }

  public JSONObject toJson() {
    JSONObject address = new JSONObject();
    try {
      address.put(STREET_1, street1);
      if (street2 != null) {
        address.put(STREET_2, street2);
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
