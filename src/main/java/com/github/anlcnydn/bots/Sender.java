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
import java.util.Optional;
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
    String url = Constants.URL + getBotToken();
    HttpPost httppost = new HttpPost(url);
    httppost.setConfig(requestConfig);
    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
    builder.addTextBody(RECIPIENT, message.getRecipientFieldAsJson().toString());
    builder.addTextBody(MESSAGE, message.getMessageFieldAsJson().toString());

    Optional<HttpEntity> hasTypeOpt = message.getUploadable().map(uploadable -> {
      switch (uploadable.getType()) {
        case IMAGE:
          return buildHttpEntity(builder, uploadable, Optional.of(ContentType.create("image/png")));
        case AUDIO:
          return buildHttpEntity(builder, uploadable, Optional.of(ContentType.create("audio/mp3")));
        case VIDEO:
          return buildHttpEntity(builder, uploadable, Optional.of(ContentType.create("video/mp4")));
        case FILE:
          return buildHttpEntity(builder, uploadable, Optional.empty());
        default:
          return null;
      }
    });

    Optional<String> responseContentOpt = hasTypeOpt.map(multipart -> {
      httppost.setEntity(multipart);
      try (CloseableHttpResponse response = httpclient.execute(httppost)) {
        HttpEntity ht = response.getEntity();
        BufferedHttpEntity buf = new BufferedHttpEntity(ht);
        return EntityUtils.toString(buf, StandardCharsets.UTF_8);
      } catch (IOException e) {
        Log.error(LOG_TAG, e);
      }
      return null;
    });

    Optional<Boolean> resultOpt = responseContentOpt.map(responseContent -> {
      try {
        JSONObject jsonObject = new JSONObject(responseContent);
        if (jsonObject.has(MESSAGE_ID) && jsonObject.has(RECIPIENT_ID)
            && jsonObject.getString(RECIPIENT_ID).equals(message.getRecipientId())) {
          Log.debug(LOG_TAG, "JSON: " + jsonObject.toString(2));
          Log.debug(LOG_TAG, "Message successfully sent ");
          return true;
        }
      } catch (JSONException e) {
        Log.error(LOG_TAG + ".sendUploadableMessage()",
            "Something went wrong while trying to convert response to json", e);
      }
      return false;
    });

    return resultOpt.orElse(false);
  }

  private HttpEntity buildHttpEntity(MultipartEntityBuilder builder, Uploadable uploadable, Optional<ContentType> contentType) {
    Optional<HttpEntity> buildOpt = contentType.map(ct -> {
      builder.addBinaryBody(FILEDATA, uploadable.asFile(), ct, uploadable.asFile().getName());
      return builder.build();
    });
    return buildOpt.orElse(builder.addBinaryBody(FILEDATA, uploadable.asFile()).build());
  }
}
