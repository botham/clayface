package com.github.anlcnydn.models;

import com.github.anlcnydn.Constants;
import com.github.anlcnydn.interfaces.BotApiObject;
import com.github.anlcnydn.interfaces.Uploadable;
import com.github.anlcnydn.logger.Log;
import com.github.anlcnydn.models.attachment.Attachment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class Message implements BotApiObject {

  private static final String LOG_TAG = Message.class.getName();

  private static final String SENDER = "sender";
  private static final String RECIPIENT = "recipient";
  private static final String ID = "id";
  private static final String TIMESTAMP = "timestamp";
  private static final String MESSAGE = "message";
  private static final String MID = "mid";
  private static final String SEQ = "seq";
  private static final String TEXT = "text";
  private static final String QUICK_REPLY = "quick_reply";
  private static final String PAYLOAD = "payload";
  private static final String ATTACHMENT = "attachment";
  private static final String TYPE = "type";

  private String senderId;
  private String recipientId;
  private Date timestamp;
  private String messageId;
  private int seqNumber;
  private String text;
  private ArrayList<Attachment> attachments;
  private Uploadable uploadable;

  private Message(String recipientId, String text) {
    this.recipientId = recipientId;
    this.text = text;
  }

  private Message(String recipientId, Attachment attachment) {
    this.recipientId = recipientId;
    if(attachments == null) {
      attachments = new ArrayList<>();
    }
    attachments.add(attachment);
  }

  private Message(String recipientId, Uploadable uploadable) {
    this.recipientId = recipientId;
    this.uploadable = uploadable;
  }


  public Message(JSONObject node) {
    try {
      this.senderId = node.getJSONObject(SENDER).getString(ID);
      this.recipientId = node.getJSONObject(RECIPIENT).getString(ID);
      this.timestamp = new Date(node.getLong(TIMESTAMP));
      this.messageId = node.getJSONObject(MESSAGE).getString(MID);
      this.seqNumber = node.getJSONObject(MESSAGE).getInt(SEQ);
      if (node.getJSONObject(MESSAGE).has(TEXT)) {
        this.text = node.getJSONObject(MESSAGE).getString(TEXT);
      }
      if (node.getJSONObject(MESSAGE).has(ATTACHMENT)) {
        JSONArray attachmentsArray = node.getJSONObject(MESSAGE).getJSONArray(ATTACHMENT);
        for (int i = 0; i < attachmentsArray.length(); i++) {
          if(attachments == null) {
            attachments = new ArrayList<>();
          }
          attachments.add(new Attachment(attachmentsArray.getJSONObject(i)));
        }
      }
    } catch (JSONException e) {
      Log.error(LOG_TAG + ".constructor", Constants.JSON_EXCEPTION_ERROR_MESSAGE, e);
    }
  }

  public static Message create(String recipientId, String text) {
    return new Message(recipientId, text);
  }

  public static Message create(String recipientId, Attachment attachment) {
    return new Message(recipientId, attachment);
  }

  public static Message create(String recipientId, Uploadable uploadable) {
    return new Message(recipientId, uploadable);
  }

  public boolean hasText() {
    return text != null;
  }

  public boolean hasAttachment() {
    return attachments != null;
  }

  public boolean hasMultipleAttachments() {
    return hasAttachment() && attachments.size() > 1;
  }

  public boolean hasUploadable() {
    return uploadable != null;
  }

  public String getSenderId() {
    return senderId;
  }

  public String getRecipientId() {
    return recipientId;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public String getMessageId() {
    return messageId;
  }

  public int getSeqNumber() {
    return seqNumber;
  }

  public String getText() {
    return text;
  }

  public ArrayList<Attachment> getAttachments() {
    return attachments;
  }

  public Uploadable getUploadable() {
    return uploadable;
  }

  public JSONObject toJson() {
    try {
      JSONObject whole = new JSONObject();
      JSONObject recipientIdObj = getRecipientFieldAsJson();
      JSONObject messageContent = getMessageFieldAsJson();
      whole.put(RECIPIENT, recipientIdObj);
      whole.put(MESSAGE, messageContent);
      return whole;
    } catch(JSONException e) {
      Log.error(LOG_TAG + ".toJson()", Constants.JSON_EXCEPTION_ERROR_MESSAGE, e);
      return null;
    }
  }

  public JSONObject getRecipientFieldAsJson() {
    JSONObject object = new JSONObject();
    try {
      object.put(ID, recipientId);
    } catch (JSONException e) {
      Log.error(LOG_TAG + ".getRecipientFieldAsJson()", Constants.JSON_EXCEPTION_ERROR_MESSAGE, e);
    }
    return object;
  }

  public JSONObject getMessageFieldAsJson() {
    JSONObject messageContent = new JSONObject();
    JSONArray attachmentsArray = new JSONArray();
    try {
      if(hasText()) {
        messageContent.put(TEXT, text);
      }
      if(hasAttachment()) {
        for (Attachment a : attachments) {
          attachmentsArray.put(a.toJson());
        }
        messageContent.put(ATTACHMENT, attachmentsArray.getJSONObject(0));
      }
      if(hasUploadable()) {
        JSONObject uploadableObj = new JSONObject();
        uploadableObj.put(TYPE, uploadable.getType());
        uploadableObj.put(PAYLOAD, new JSONObject());
        messageContent.put(ATTACHMENT,uploadableObj);
      }
    } catch(JSONException e) {
      Log.error(LOG_TAG + ".getMessageFieldAsJson()", Constants.JSON_EXCEPTION_ERROR_MESSAGE, e);
    }
    return messageContent;
  }




}
