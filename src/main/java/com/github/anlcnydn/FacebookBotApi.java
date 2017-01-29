package com.github.anlcnydn;

import com.github.anlcnydn.bots.Bot;
import com.github.anlcnydn.models.BotHttpResult;
import com.github.anlcnydn.models.Update;
import com.github.anlcnydn.utils.Convert;
import org.json.JSONObject;

import java.util.Map;

public class FacebookBotApi {
  private static final String LOG_TAG = FacebookBotApi.class.getSimpleName();

  private Bot bot;

  /**
   *
   * @param bot
   */
  public FacebookBotApi(Bot bot) {
    this.bot = bot;
  }

  public BotHttpResult verify(Map<String, String> urlFields) {
    if (urlFields != null && urlFields.containsKey("hub.mode")
        && urlFields.containsKey("hub.verify_token") && urlFields.containsKey("hub.challenge")) {
      String verifyToken = urlFields.get("hub.verify_token");
      String challenge = urlFields.get("hub.challenge");
      if (verifyToken.equals(bot.getVerificationToken())) {
        return new BotHttpResult(Constants.OK, challenge);
      }
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
    if (entireRequest != null && bot.onUpdateReceived(new Update(entireRequest))) {
      return new BotHttpResult(Constants.OK);
    }
    return new BotHttpResult(Constants.INTERNAL_SERVER_ERROR);
  }
}
