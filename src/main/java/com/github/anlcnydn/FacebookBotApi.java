package com.github.anlcnydn;

import com.github.anlcnydn.bots.ClayFaceBot;
import com.github.anlcnydn.models.Update;
import com.github.anlcnydn.utils.Convert;
import org.json.JSONObject;

import java.util.Map;

import static com.github.anlcnydn.Constants.ValidationConstants.*;

public abstract class FacebookBotApi extends ClayFaceBot {
  private static final String LOG_TAG = FacebookBotApi.class.getSimpleName();

  public BotHttpResult verify(Map<String, String> urlFields) {
    if (urlFields != null && urlFields.containsKey(MODE_FIELD)
        && urlFields.containsKey(VERIFY_TOKEN_FIELD) && urlFields.containsKey(CHALLENGE_FIELD)) {
      String mode = urlFields.get(MODE_FIELD);
      String verifyToken = urlFields.get(VERIFY_TOKEN_FIELD);
      String challenge = urlFields.get(CHALLENGE_FIELD);
      return verify(mode, verifyToken, challenge);
    }
    return new BotHttpFailure(Constants.BAD_REQUEST);
  }

  public BotHttpResult verify(String mode, String verifyToken, String challenge) {
    if ("subscribe".equals(mode) && verifyToken.equals(getVerificationToken())) {
      return new BotHttpSuccess(Constants.OK, challenge);
    }
    return new BotHttpFailure(Constants.UNAUTHORIZED);
  }

  /**
   * Gets request body from POST endpoint and provides an {@link Update} to {@code onUpdateReceived}.
   *
   * @param requestBody message from Facebook
   * @throws IllegalArgumentException If {@link Update} creation fails
   */
  public void receive(String requestBody) throws IllegalArgumentException {
    JSONObject requestJson = Convert.toJson(requestBody);
    Update update = Update.create(requestJson);

    if (requestJson != null && update != null) {
      onUpdateReceived(update);
    } else {
      throw new IllegalArgumentException(
          "An Update cannot be instantiated from request:\n" + requestBody);
    }
  }
}
