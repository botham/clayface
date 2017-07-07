package com.github.anlcnydn;

import com.github.anlcnydn.bots.ClayFaceBot;
import com.github.anlcnydn.models.BotHttpResult;
import com.github.anlcnydn.models.Update;
import com.github.anlcnydn.utils.Convert;
import org.json.JSONObject;

import java.util.Map;

import static com.github.anlcnydn.Constants.ValidationConstants.*;

public abstract class FacebookBotApi extends ClayFaceBot {
  private static final String LOG_TAG = FacebookBotApi.class.getSimpleName();

  public BotHttpResult verify(Map<String, String> urlFields) {
    if (urlFields != null && urlFields.containsKey(ModeField)
        && urlFields.containsKey(VerifyTokenField) && urlFields.containsKey(ChallengeField)) {
      String mode = urlFields.get(ModeField);
      String verifyToken = urlFields.get(VerifyTokenField);
      String challenge = urlFields.get(ChallengeField);
      return verify(mode, verifyToken, challenge);
    }
    return new BotHttpResult(Constants.BAD_REQUEST);
  }

  public BotHttpResult verify(String mode, String verifyToken, String challenge) {
    if ("subscribe".equals(mode) && verifyToken.equals(getVerificationToken())) {
      return new BotHttpResult(Constants.OK, challenge);
    }
    return new BotHttpResult(Constants.UNAUTHORIZED);
  }

  /**
   *
   * @param request
   * @return
   */
  public BotHttpResult receive(String request) {

    JSONObject entireRequest = Convert.toJson(request);
    if (entireRequest != null && onUpdateReceived(new Update(entireRequest))) {
      return new BotHttpResult(Constants.OK);
    }
    return new BotHttpResult(Constants.INTERNAL_SERVER_ERROR);
  }
}
