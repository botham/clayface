package com.github.anlcnydn.utils;

import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConvertTest {
  @Test
  public void toJson() throws Exception {
    String str = "{\"key\":\"value\"}";
    String str1 = "{asd";
    JSONObject object = new JSONObject(str);
    assertEquals(object.toString(), Convert.toJson(str).toString());
    assertEquals(null, Convert.toJson(str1));
  }

}