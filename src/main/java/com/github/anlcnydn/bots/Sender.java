package com.github.anlcnydn.bots;

import com.github.anlcnydn.*;
import com.github.anlcnydn.interfaces.Uploadable;
import com.github.anlcnydn.logger.Log;
import com.github.anlcnydn.models.Message;
import com.github.anlcnydn.utils.CheckedFunction;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
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
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

public abstract class Sender {
  private static final String LOG_TAG = Sender.class.getName();

  private static final String MESSAGE_ID = "message_id";
  private static final String ATTACHMENT_ID = "attachment_id";
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

  private Config configuration;

  private BiFunction<Uploadable, MultipartEntityBuilder, HttpEntity> createHttpEntity =
      (uploadable, builder) -> {
        switch (uploadable.getType()) {
          case IMAGE:
            return buildHttpEntity(builder, uploadable,
                Optional.of(ContentType.create("image/png")));
          case AUDIO:
            return buildHttpEntity(builder, uploadable,
                Optional.of(ContentType.create("audio/mp3")));
          case VIDEO:
            return buildHttpEntity(builder, uploadable,
                Optional.of(ContentType.create("video/mp4")));
          case FILE:
            return buildHttpEntity(builder, uploadable, Optional.empty());
          default:
            return null;
        }
      };

  Sender() {
    this.httpclient = createHttpClient();

    RequestConfig.Builder configBuilder = RequestConfig.custom();

    this.requestConfig = configBuilder.setSocketTimeout(75000).setConnectTimeout(75000)
        .setConnectionRequestTimeout(75000).build();

    this.configuration = ConfigFactory.load();
  }

  Sender(CloseableHttpClient httpclient) {
    this.httpclient = httpclient;

    RequestConfig.Builder configBuilder = RequestConfig.custom();

    this.requestConfig = configBuilder.setSocketTimeout(75000).setConnectTimeout(75000)
        .setConnectionRequestTimeout(75000).build();

    this.configuration = ConfigFactory.load();
  }

  /**
   * Sends given {@link Message} to Facebook Bot.
   *
   * @param message {@link Message} to be sent
   * @return {@link BotHttpSuccess} if operation succeeds
   *         {@link BotHttpFailure} if there is a failure
   * @throws IllegalArgumentException If {@code message} is {@code null}
   * @throws FacebookApiException If there is a failure
   */
  public BotHttpResult sendMessage(Message message)
      throws IllegalArgumentException, FacebookApiException {
    if (message == null) {
      throw new IllegalArgumentException("Message cannot be null");
    }
    if (message.hasUploadable()) {
      Log.debug(LOG_TAG, "Uploadable");
      return send(message, json -> json.has(ATTACHMENT_ID));
    } else {
      return send(message, json -> json.has(MESSAGE_ID)
          && json.getString(RECIPIENT_ID).equals(message.getRecipientId()));
    }
  }

  // Sends  message
  BotHttpResult send(Message message,
      CheckedFunction<JSONObject, Boolean, JSONException> validationPredicate)
      throws FacebookApiException, IllegalArgumentException {

    HttpPost httpPost = createHttpPost(message);

    try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
      int statusCode = response.getStatusLine().getStatusCode();
      String responseContent = getResponseEntity(response);

      // if status code is 2xx we try to validate response
      if (200 <= statusCode || statusCode < 300) {
        boolean isSuccess = validateResponse(responseContent, validationPredicate);
        if (isSuccess) {
          return new BotHttpSuccess(statusCode);
        }
      }

      // return BotHttpFailure if status code is not 2xx or there is a problem with validation
      return new BotHttpFailure(statusCode, responseContent);
    } catch (IOException | ParseException e) {
      String errorMessage =
          "Something went wrong while trying to send a post request in " + LOG_TAG + ".send()";
      throw new FacebookApiException(errorMessage, e);
    }
  }

  protected String getVerificationToken() {
    return configuration.getString("clayface.credentials.verification-token");
  }

  protected String getBotToken() {
    return configuration.getString("clayface.credentials.bot-token");
  }

  private CloseableHttpClient createHttpClient() {
    return HttpClientBuilder.create().setSSLHostnameVerifier(new NoopHostnameVerifier())
        .setConnectionTimeToLive(70, TimeUnit.SECONDS).setMaxConnTotal(100).build();
  }

  HttpPost createHttpPost(Message message) throws IllegalArgumentException {
    HttpPost httpPost = new HttpPost();
    httpPost.setConfig(requestConfig);

    return message.getUploadable().map(uploadable -> {
      MultipartEntityBuilder builder = MultipartEntityBuilder.create();
      builder.addTextBody(RECIPIENT, message.getRecipientFieldAsJson().toString());
      builder.addTextBody(MESSAGE, message.getMessageFieldAsJson().toString());

      URI url = URI.create(Constants.UPLOAD_URL + getBotToken());

      httpPost.setURI(url);
      httpPost.setEntity(createHttpEntity.apply(uploadable, builder));
      return httpPost;
    }).orElseGet(() -> {
      URI url = URI.create(Constants.URL + getBotToken());

      httpPost.setURI(url);
      httpPost.addHeader("charset", StandardCharsets.UTF_8.name());
      httpPost
          .setEntity(new StringEntity(message.toJson().toString(), ContentType.APPLICATION_JSON));
      return httpPost;
    });
  }

  String getResponseEntity(CloseableHttpResponse response) throws IOException {
    HttpEntity ht = response.getEntity();
    BufferedHttpEntity buf = new BufferedHttpEntity(ht);
    return EntityUtils.toString(buf, StandardCharsets.UTF_8);
  }

  private boolean validateResponse(String responseText,
      CheckedFunction<JSONObject, Boolean, JSONException> predicate) throws FacebookApiException {
    try {
      JSONObject jsonObject = new JSONObject(responseText);
      if (predicate.apply(jsonObject)) {
        Log.debug(LOG_TAG, "Message successfully sent");
      } else {
        throw new FacebookApiException("Got inconsistent message: " + responseText);
      }
    } catch (JSONException e) {
      return false;
    }
    return true;
  }

  private HttpEntity buildHttpEntity(MultipartEntityBuilder builder, Uploadable uploadable,
      Optional<ContentType> contentType) {
    Optional<HttpEntity> buildOpt = contentType.map(ct -> {
      builder.addBinaryBody(FILEDATA, uploadable.asFile(), ct, uploadable.asFile().getName());
      return builder.build();
    });
    return buildOpt.orElse(builder.addBinaryBody(FILEDATA, uploadable.asFile()).build());
  }
}
