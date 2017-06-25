## clayface 

A ready-to-use library to create Facebook Messenger bots easily.

#### Usage

[Find](https://mvnrepository.com/artifact/com.anilcanaydin/clayface) 
the latest version and import it.

Details for the Facebook Bot API please visit [here](https://developers.facebook.com/docs/messenger-platform/reference).

Basically a facebook bot needs to get and send messages. 

##### Creating Bots

Just create your own bot class and extend ClayFaceBot. 

``` java
import com.github.anlcnydn.FacebookApiException;
import com.github.anlcnydn.bots.ClayFaceBot;
import com.github.anlcnydn.models.Message;
import com.github.anlcnydn.models.Update;

public class FacebookBot extends ClayFaceBot {
  
  //This not a good practice to keep tokens here. This just for documentation purposes.
  private static final String VERIFICATION_TOKEN = "YOUR_CUSTOM_VERIFICATION_TOKEN";
  private static final String BOT_TOKEN = "YOUR_PAGE_TOKEN_CREATED_BY_FACEBOOK";

  @Override
  public boolean onUpdateReceived(Update update) {
    //When message arrived.
  }

  @Override
  public String getVerificationToken() {
    return VERIFICATION_TOKEN;
  }

  @Override
  public String getBotToken() {
    return BOT_TOKEN;
  }
}
```

##### Verification

First, we need to verify the facebook webhook connection. It means that facebook will send a get request to your 
host's "/webhook" url(For ex: https://example.com/webhook?hub.mode=subscribe&hub.challenge=SOME_CHALLENGE_SENT_BY_FACEBOOK&hub.verify_token=YOUR_CUSTOM_VERIFICATION_TOKEN).

In where you get the GET request, you need to put the parameters in a Map<String, String[]>. 

``` java
    //Lets create a dummy map.
    Map<String, String[]> map = new HashMap<>();
    map.put("hub.mode", "subscribe");
    map.put("hub.challenge", "SOME_CHALLENGE_SENT_BY_FACEBOOK");
    map.put("hub.verify_token", "YOUR_CUSTOM_VERIFICATION_TOKEN");
    FacebookBotApi api = new FacebookBotApi(new FacebookBot);
    
    //This result can be unauthorized or ok
    BotHttpResult result = api.verify(map);
    
    if(result.getCode().equals("ok")) {
        //the value is hub.challenge from facebook 
        return ok(result.getValue());
    }
    
    return unauthorized();

```

##### Getting Messages

Messages are sent /webhook url as POST request with a JSON body. You need to pass 
body as string to api.receive() method.

``` java
    String request = request().body().asJson().toString();
    FacebookBotApi api = new FacebookBotApi(new FacebookBot());
    
    //This result can be ok or internal_server_error
    BotHttpResult result = api.receive(request);
    if(result.getCode().equals("ok")) {
      return ok();
    }
    return internalServerError();
```

Messages arrive our bots onUpdateReceived() method.

``` java
  @Override
  public boolean onUpdateReceived(Update update) {
    if(update.hasMessage()) {
      Message m = Message.create(update.getMessage().getSenderId(), update.getMessage().getText());
      try {
        sendMessage(m);
      } catch (FacebookApiException e) {
        //TODO
      }
      return true;
    }
    return false;
  }
```

The above code snippet sends the same message to its sender. 

#### Facebook Bot API 

This library uses [Facebook Messenger Bot API](https://developers.facebook.com/docs/messenger-platform).
For further information please visit API's documentation.

#### Examples 

Examples will be shared under this title.

#### Questions & Suggestions 

Feel free to open an issue.



