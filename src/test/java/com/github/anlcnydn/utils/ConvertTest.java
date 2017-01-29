package com.github.anlcnydn.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class ConvertTest {

  @Test
  public void toJson() throws Exception {
    String validJsonString = "{\"key\":\"value\"}";
    String invalidJsonString = "{asd";

    assertEquals(validJsonString, Convert.toJson(validJsonString).toString());
    assertNull(Convert.toJson(invalidJsonString));
  }

}
