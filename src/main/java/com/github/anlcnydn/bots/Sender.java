package com.github.anlcnydn.bots;

import com.github.anlcnydn.Constants;
import com.github.anlcnydn.FacebookApiException;
import com.github.anlcnydn.interfaces.Uploadable;
import com.github.anlcnydn.logger.Log;
import com.github.anlcnydn.models.Message;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public abstract class Sender {
  private static final String LOG_TAG = Sender.class.getName();

  private static final String MESSAGE_ID = "message_id";
  private static final String RECIPIENT_ID = "recipient_id";
  private static final String RECIPIENT = "recipient";
  private static final String MESSAGE = "message";
  private static final String IMAGE = "image";
  private static final String AUDIO = "audio";
  private static final String VIDEO = "video";
  private static final String FILE = "file";
  private static final String FILEDATA = "filedata";

  private volatile CloseableHttpClient httpclient;
  private volatile RequestConfig requestConfig;

  Sender() {
    this.httpclient = HttpClientBuilder.create().setSSLHostnameVerifier(new NoopHostnameVerifier())
        .setConnectionTimeToLive(70, TimeUnit.SECONDS).setMaxConnTotal(100).build();

    RequestConfig.Builder configBuilder = RequestConfig.custom();

    this.requestConfig = configBuilder.setSocketTimeout(75000).setConnectTimeout(75000)
        .setConnectionRequestTimeout(75000).build();
  }

  public abstract String getBotToken();

  public boolean sendMessage(Message message) throws FacebookApiException {
    if (message == null) {
      throw new FacebookApiException();
    }
    if (message.hasUploadable()) {
      Log.debug(LOG_TAG, "Uploadable");
      return sendUploadableMessage(message);
    }
    return send(message);
  }

  boolean send(Message message) {
    String responseContent;
    try {
      String url = Constants.URL + getBotToken();
      HttpPost httpPost = new HttpPost(url);
      httpPost.setConfig(requestConfig);
      httpPost.addHeader("charset", StandardCharsets.UTF_8.name());
      httpPost
          .setEntity(new StringEntity(message.toJson().toString(), ContentType.APPLICATION_JSON));
      try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
        HttpEntity ht = response.getEntity();
        BufferedHttpEntity buf = new BufferedHttpEntity(ht);
        // TODO: catch ParseException and ClientProtocolException
        responseContent = EntityUtils.toString(buf, StandardCharsets.UTF_8);
      }
    } catch (IOException e) {
      Log.error(LOG_TAG + ".send()", "Something went wrong while trying to send a post request.",
          e);
      return false;
    }

    try {
      JSONObject jsonObject = new JSONObject(responseContent);
      if (jsonObject.has(MESSAGE_ID)
          && jsonObject.getString(RECIPIENT_ID).equals(message.getRecipientId())) {
        Log.debug(LOG_TAG, "Message successfully sent");
      }
    } catch (JSONException e) {
      Log.error(LOG_TAG + ".send()",
          "Something went wrong while trying to convert response to json", e);
      return false;
    }
    return true;
  }

  /**
   *
   * @param message
   * @return
   */
  boolean sendUploadableMessage(Message message) {
    String responseContent;
    try {
      String url = Constants.URL + getBotToken();
      HttpPost httppost = new HttpPost(url);
      httppost.setConfig(requestConfig);
      MultipartEntityBuilder builder = MultipartEntityBuilder.create();
      builder.addTextBody(RECIPIENT, message.getRecipientFieldAsJson().toString());
      builder.addTextBody(MESSAGE, message.getMessageFieldAsJson().toString());
      Uploadable uploadable = message.getUploadable().get();
      switch (uploadable.getType()) {
        case IMAGE:
          builder.addBinaryBody(FILEDATA, uploadable.asFile(),
              ContentType.create("image/png"), uploadable.asFile().getName());
          break;
        case AUDIO:
          builder.addBinaryBody(FILEDATA, uploadable.asFile(),
              ContentType.create("audio/mp3"), uploadable.asFile().getName());
          break;
        case VIDEO:
          builder.addBinaryBody(FILEDATA, uploadable.asFile(),
              ContentType.create("video/mp4"), uploadable.asFile().getName());
          break;
        case FILE:
          builder.addBinaryBody(FILEDATA, uploadable.asFile());
          break;
        default:
          return false;
      }

      HttpEntity multipart = builder.build();
      httppost.setEntity(multipart);
      try (CloseableHttpResponse response = httpclient.execute(httppost)) {
        HttpEntity ht = response.getEntity();
        BufferedHttpEntity buf = new BufferedHttpEntity(ht);
        responseContent = EntityUtils.toString(buf, StandardCharsets.UTF_8);
        Log.debug(LOG_TAG, responseContent);
      }
    } catch (IOException e) {
      Log.error(LOG_TAG + ".sendUploadableMessage()",
          "Something went wrong while trying to send a post request with multipart upload.", e);
      return false;
    }

    try {
      JSONObject jsonObject = new JSONObject(responseContent);
      if (jsonObject.has(MESSAGE_ID)
          && jsonObject.getString(RECIPIENT_ID).equals(message.getRecipientId())) {
        Log.debug(LOG_TAG, "JSON: " + jsonObject.toString(2));
        Log.debug(LOG_TAG, "Message successfully sent ");
      }
    } catch (JSONException e) {
      Log.error(LOG_TAG + ".sendUploadableMessage()",
          "Something went wrong while trying to convert response to json", e);
      return false;
    }

    return true;
  }
}
