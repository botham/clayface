package com.github.anlcnydn;

import com.github.anlcnydn.models.Update;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FacebookBotApiTest {

  /////////////////////////////////////
  //          verify                 //
  /////////////////////////////////////

  @Test
  public void verify_with_separate_params_success() throws Exception {
    FacebookBotApi bot = new FaceBookBot();
    String challenge = "I challenge you!";
    BotHttpSuccess result =
        (BotHttpSuccess) bot.verify("subscribe", "verification-token", challenge);
    assertTrue(result.isSuccess());
    assertEquals(200, result.getStatusCode());
    assertEquals(challenge, result.getValue().get());
  }

  @Test
  public void verify_with_separate_params_unequal_verification() throws Exception {
    FacebookBotApi bot = new FaceBookBot();
    String challenge = "I challenge you!";
    BotHttpResult result = bot.verify("subscribe", "wrong-token", challenge);
    assertFalse(result.isSuccess());
    assertEquals(401, result.getStatusCode());
  }

  @Test
  public void verify_with_separate_params_illegal_mode() throws Exception {
    FacebookBotApi bot = new FaceBookBot();
    String challenge = "I challenge you!";
    BotHttpResult result = bot.verify("hey ho", "verification-token", challenge);
    assertFalse(result.isSuccess());
    assertEquals(401, result.getStatusCode());
  }

  @Test
  public void verify_with_map_success() throws Exception {
    FacebookBotApi bot = new FaceBookBot();
    String challenge = "I challenge you!";
    String mode = "subscribe";
    String verificationToken = "verification-token";
    Map<String, String> urlFields = new HashMap<>();
    urlFields.put("hub.challenge", challenge);
    urlFields.put("hub.mode", mode);
    urlFields.put("hub.verify_token", verificationToken);

    BotHttpSuccess result = (BotHttpSuccess) bot.verify(urlFields);
    assertTrue(result.isSuccess());
    assertEquals(200, result.getStatusCode());
    assertEquals(challenge, result.getValue().get());
  }

  @Test
  public void verify_with_map_missing_element() throws Exception {
    FacebookBotApi bot = new FaceBookBot();
    String mode = "subscribe";
    String verificationToken = "verification-token";
    Map<String, String> urlFields = new HashMap<>();
    urlFields.put("hub.mode", mode);
    urlFields.put("hub.verify_token", verificationToken);

    BotHttpResult result = bot.verify(urlFields);
    assertFalse(result.isSuccess());
    assertEquals(400, result.getStatusCode());
  }

  @Test
  public void verify_with_map_null_parameter() throws Exception {
    FacebookBotApi bot = new FaceBookBot();
    BotHttpResult result = bot.verify(null);
    assertFalse(result.isSuccess());
    assertEquals(400, result.getStatusCode());
  }


  /////////////////////////////////////
  //            receive              //
  /////////////////////////////////////
  @Test
  public void receive_with_happy_path() throws Exception {
    FacebookBotApi bot = spy(FacebookBotApi.class);
    String requestBody = "{\n" + "  \"object\":\"page\",\n" + "  \"entry\":[\n" + "    {\n"
        + "      \"id\":\"PAGE_ID\",\n" + "      \"time\":1458692752478,\n"
        + "      \"messaging\":[\n" + "        {\n" + "          \"sender\":{\n"
        + "            \"id\":\"USER_ID\"\n" + "          },\n" + "          \"recipient\":{\n"
        + "            \"id\":\"PAGE_ID\"\n" + "          }\n" + "        }\n" + "      ]\n"
        + "    }\n" + "  ]\n" + "}";

    doNothing().when(bot).onUpdateReceived(any(Update.class));
    bot.receive(requestBody);
    verify(bot).onUpdateReceived(any(Update.class));
  }

  @Test(expected = IllegalArgumentException.class)
  public void receive_with_malformed_request_body() throws Exception {
    FacebookBotApi bot = new FaceBookBot();
    String requestBody = "message";

    bot.receive(requestBody);
  }

  public class FaceBookBot extends FacebookBotApi {
    @Override
    public void onUpdateReceived(Update update) {}

    @Override
    protected String getVerificationToken() {
      return "verification-token";
    }

    @Override
    protected String getBotToken() {
      return "bot-token";
    }
  }
}
