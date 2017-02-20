package com.github.anlcnydn.models;

import com.github.anlcnydn.Constants;
import com.github.anlcnydn.interfaces.BotApiObject;
import com.github.anlcnydn.logger.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class Update implements BotApiObject {

  private static final String LOG_TAG = Update.class.getName();

  private static final String ENTRY = "entry";
  private static final String ID = "id";
  private static final String TIME = "time";
  private static final String MESSAGING = "messaging";
  private static final String MESSAGE = "message";

  private String pageId;
  private ArrayList<Message> messages;
  private Date updateTime;

  public Update() {
    super();
  }

  public Update(JSONObject node) {
    try {

      this.pageId = node.getJSONArray(ENTRY).getJSONObject(0).getString(ID);
      this.updateTime = new Date(node.getJSONArray(ENTRY).getJSONObject(0).getLong(TIME));
      this.messages = new ArrayList<Message>();
      JSONArray messaging = node.getJSONArray(ENTRY).getJSONObject(0).getJSONArray(MESSAGING);
      for (int i = 0; i < messaging.length(); i++) {
        JSONObject message = messaging.getJSONObject(i);
        if (message.has(MESSAGE)) {
          messages.add(Message.create(message));
        }
      }
    } catch (JSONException e) {
      Log.error(LOG_TAG + ".constructor", Constants.JSON_EXCEPTION_ERROR_MESSAGE, e);
    }
  }

  public Message getMessage() {
    return messages.get(0);
  }

  public String getPageId() {
    return pageId;
  }

  public ArrayList<Message> getMessages() {
    return messages;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public boolean hasMessage() {
    return !messages.isEmpty();
  }

  public boolean hasMultipleMessages() {
    return messages.size() > 1;
  }

}
