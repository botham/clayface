package com.github.anlcnydn.models;

import com.github.anlcnydn.Constants;
import com.github.anlcnydn.FacebookApiException;
import com.github.anlcnydn.interfaces.BotApiObject;
import com.github.anlcnydn.interfaces.Uploadable;
import com.github.anlcnydn.logger.Log;
import com.github.anlcnydn.models.attachment.Attachment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

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


  private String recipientId;

  private Optional<Date> timestamp = Optional.empty();
  private Optional<String> messageId = Optional.empty();
  private Optional<Integer> seqNumber = Optional.empty();
  private Optional<String> senderId = Optional.empty();
  private Optional<String> text = Optional.empty();
  private Optional<ArrayList<Attachment>> attachments = Optional.empty();
  private Optional<Uploadable> uploadable = Optional.empty();

  private Message(String recipientId, String text) {
    this.recipientId = recipientId;
    this.text = Optional.of(text);
  }

  private Message(String recipientId, Attachment attachment) {
    this.recipientId = recipientId;
    ArrayList<Attachment> atts = new ArrayList<>();
    atts.add(attachment);
    this.attachments = Optional.of(atts);
  }

  private Message(String recipientId, Uploadable uploadable) {
    this.recipientId = recipientId;
    this.uploadable = Optional.of(uploadable);
  }

  private Message(JSONObject node) {
    try {


      this.senderId = Optional.of(node.getJSONObject(SENDER).getString(ID));
      this.recipientId = node.getJSONObject(RECIPIENT).getString(ID);
      this.timestamp = Optional.of(new Date(node.getLong(TIMESTAMP)));

      if (node.has(MESSAGE)) {
        JSONObject message = node.getJSONObject(MESSAGE);
        this.messageId = Optional.of(message.getString(MID));
        this.seqNumber = Optional.of(message.getInt(SEQ));

        if (message.has(TEXT)) {
          this.text = Optional.of(message.getString(TEXT));
        }
        if (message.has(ATTACHMENT)) {
          ArrayList<Attachment> atts = new ArrayList<>();
          JSONArray attachmentsArray = message.getJSONArray(ATTACHMENT);
          for (int i = 0; i < attachmentsArray.length(); i++) {
            atts.add(Attachment.create(attachmentsArray.getJSONObject(i)));
          }
          this.attachments = Optional.of(atts);
        }
      }
    } catch (JSONException e) {
      Log.error(LOG_TAG + ".constructor", Constants.JSON_EXCEPTION_ERROR_MESSAGE, e);
      //TODO: Throw facebookapiexc. with a meaningful message
    }
  }

  public static Message create(String recipientId, String text) {
    return new Message(recipientId, text);
  }

  public static Message create(String recipientId, Attachment attachment) {
    return new Message(recipientId, attachment);
  }

  public static Message create(JSONObject node) {
    return new Message(node);
  }

  public static Message create(String recipientId, Uploadable uploadable) {
    return new Message(recipientId, uploadable);
  }

  public boolean hasText() {
    return text.isPresent();
  }

  public boolean hasAttachment() {
    return attachments.isPresent();
  }

  public boolean hasMultipleAttachments() {
    return hasAttachment() && attachments.map(a -> a.size() > 1).get();
  }

  public boolean hasUploadable() {
    return uploadable.isPresent();
  }

  public Optional<String> getSenderId() {
    return senderId;
  }

  public String getRecipientId() {
    return recipientId;
  }

  public Optional<Date> getTimestamp() {
    return timestamp;
  }

  public Optional<String> getMessageId() {
    return messageId;
  }

  public Optional<Integer> getSeqNumber() {
    return seqNumber;
  }

  public Optional<String> getText() {
    return text;
  }

  public Optional<ArrayList<Attachment>> getAttachments() {
    return attachments;
  }

  public Optional<Uploadable> getUploadable() {
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
    } catch (JSONException e) {
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

    try {
      if (hasText()) {
        messageContent.put(TEXT, text);
      }

      Optional<JSONArray> attachmentsArray = attachments.map(atts -> {
        JSONArray jsArray = new JSONArray();
        atts.forEach(a -> jsArray.put(a.toJson()));
        return jsArray;
      });

      if (attachmentsArray.isPresent()) {
        messageContent.put(ATTACHMENT, attachmentsArray.get());
      }
      if (hasUploadable()) {
        JSONObject uploadableObj = new JSONObject();
        uploadableObj.put(TYPE, uploadable.get().getType());
        uploadableObj.put(PAYLOAD, new JSONObject());
        messageContent.put(ATTACHMENT, uploadableObj);
      }
    } catch (JSONException e) {
      Log.error(LOG_TAG + ".getMessageFieldAsJson()", Constants.JSON_EXCEPTION_ERROR_MESSAGE, e);
    }
    return messageContent;
  }



}
