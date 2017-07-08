package com.github.anlcnydn.bots;

import com.github.anlcnydn.BotHttpResult;
import com.github.anlcnydn.FacebookApiException;
import com.github.anlcnydn.models.Message;
import com.github.anlcnydn.models.attachment.upload.Image;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SenderTest {

  private Sender senderSpy = spy(Sender.class);

  /////////////////////////////////////
  //          sendMessage            //
  /////////////////////////////////////

  @Test(expected = IllegalArgumentException.class)
  public void sendMessage_with_null_Message() throws Exception {
    senderSpy.sendMessage(null);
  }

  /* TODO: figure out how to mock FunctionalInterface
  @Test
  public void sendMessage_with_uploadable_Message() throws Exception {
    Message messageMock = mock(Message.class);
    BotHttpSuccess expected = new BotHttpSuccess(200);
    doReturn(true).when(messageMock).hasUploadable();
    doReturn(expected).when(senderSpy).send(messageMock, any());
  
    BotHttpResult sendResult = senderSpy.sendMessage(messageMock);
    assertEquals(sendResult, expected);
    verify(senderSpy).send(messageMock, json -> json.has("attachment_id"));
  }
  
  @Test
  public void sendMessage_with_non_uploadable_Message() throws Exception {
    Message message = Message.create("someone", "Hey!");
    BotHttpSuccess expected = new BotHttpSuccess(200);
    doReturn(expected).when(senderSpy).send(message,
      any());
  
    BotHttpResult sendResult = senderSpy.sendMessage(message);
    assertEquals(sendResult, expected);
    verify(senderSpy).send(message,
      json -> json.has("message_id") && json.getString("recipient_id").equals(any(String.class)));
  }
  */

  /////////////////////////////////////
  //              send               //
  /////////////////////////////////////
  @Test
  public void send_with_happy_path() throws Exception {
    String response = "{\n" + "  \"recipient_id\": \"Anil\",\n"
        + "  \"message_id\": \"mid.1456970487936:c34767dfe57ee6e339\"\n" + "}\n";

    BotHttpResult result = mockForSendNonUploadable(response, false);
    assertTrue(result.isSuccess());
    assertEquals(200, result.getStatusCode());
  }

  @Test
  public void send_with_missing_recipient_id() throws Exception {
    String response =
        "{\n" + "  \"message_id\": \"mid.1456970487936:c34767dfe57ee6e339\"\n" + "}\n";

    BotHttpResult result = mockForSendNonUploadable(response, false);
    assertFalse(result.isSuccess());
    assertEquals(200, result.getStatusCode());
  }

  @Test(expected = FacebookApiException.class)
  public void send_with_malformed_response() throws Exception {
    String response = "{\n" + "  \"recipient_id\": \"Anil\"" + "}\n";

    mockForSendNonUploadable(response, false);
  }

  @Test(expected = FacebookApiException.class)
  public void send_with_wrong_recipient_id() throws Exception {
    String response = "{\n" + "  \"recipient_id\": \"Someone Else\",\n"
        + "  \"message_id\": \"mid.1456970487936:c34767dfe57ee6e339\"\n" + "}\n";

    BotHttpResult result = mockForSendNonUploadable(response, false);
    System.out.println(result);
  }

  @Test(expected = FacebookApiException.class)
  public void send_if_IOException_is_thrown() throws Exception {
    String response = "{\n" + "  \"recipient_id\": \"SomeoneElse\",\n"
        + "  \"message_id\": \"mid.1456970487936:c34767dfe57ee6e339\"\n" + "}\n";

    mockForSendNonUploadable(response, true);
  }

  /////////////////////////////////////
  //          send uploadable        //
  /////////////////////////////////////
  @Test
  public void send_uploadable_with_happy_path() throws Exception {
    String response = "{\n" + "  \"attachment_id\": \"image\",\n" + "}\n";

    BotHttpResult result = mockForSendUploadable(response, false);
    assertTrue(result.isSuccess());
    assertEquals(200, result.getStatusCode());
  }

  /////////////////////////////////////
  //     getVerificationToken        //
  /////////////////////////////////////
  @Test
  public void getVerificationToken() throws Exception {
    String result = senderSpy.getVerificationToken();
    assertEquals(result, "test-verification-token");
  }

  /////////////////////////////////////
  //          getBotToken            //
  /////////////////////////////////////
  @Test
  public void getBotToken() throws Exception {
    String result = senderSpy.getBotToken();
    assertEquals(result, "test-bot-token");
  }

  /////////////////////////////////////
  //          Boilerplate            //
  /////////////////////////////////////

  class TestSender extends Sender {

    TestSender(CloseableHttpClient httpclient) {
      super(httpclient);
    }
  }

  private BotHttpResult mockForSendNonUploadable(String responseJson, Boolean throwIOException)
      throws Exception {

    String recipientId = "Anil";
    String text = "Naber";
    Message message = Message.create(recipientId, text);

    HttpPost httpPostMock = mock(HttpPost.class);
    doNothing().when(httpPostMock).addHeader("charset", StandardCharsets.UTF_8.name());
    doNothing().when(httpPostMock).setURI(any(URI.class));
    doNothing().when(httpPostMock)
        .setEntity(new StringEntity(message.toJson().toString(), ContentType.APPLICATION_JSON));

    CloseableHttpClient httpClientMock = mock(CloseableHttpClient.class);
    CloseableHttpResponse httpResponseMock = mock(CloseableHttpResponse.class);
    StatusLine statusLineMock = mock(StatusLine.class);
    doReturn(200).when(statusLineMock).getStatusCode();
    doReturn(statusLineMock).when(httpResponseMock).getStatusLine();
    doNothing().when(httpClientMock).close();
    if (!throwIOException) {
      when(httpClientMock.execute(any(HttpUriRequest.class))).thenReturn(httpResponseMock);
    } else {
      when(httpClientMock.execute(any(HttpUriRequest.class))).thenThrow(new IOException());
    }
    Sender sender = spy(new TestSender(httpClientMock));
    doReturn(httpPostMock).when(sender).createHttpPost(message);

    doReturn(responseJson).when(sender).getResponseEntity(httpResponseMock);

    return sender.send(message,
        json -> json.has("message_id") && json.getString("recipient_id").equals(recipientId));
  }

  private BotHttpResult mockForSendUploadable(String responseJson, Boolean throwIOException)
      throws Exception {

    String recipientId = "Anil";
    Message message =
        Message.create(recipientId, Image.create("https://petersapparel.com/img/shirt.png"));

    HttpPost httpPostMock = mock(HttpPost.class);
    doNothing().when(httpPostMock).setURI(any(URI.class));
    doNothing().when(httpPostMock).setEntity(any(HttpEntity.class));

    CloseableHttpClient httpClientMock = mock(CloseableHttpClient.class);
    CloseableHttpResponse httpResponseMock = mock(CloseableHttpResponse.class);
    StatusLine statusLineMock = mock(StatusLine.class);
    doReturn(200).when(statusLineMock).getStatusCode();
    doReturn(statusLineMock).when(httpResponseMock).getStatusLine();
    doNothing().when(httpClientMock).close();
    if (!throwIOException) {
      when(httpClientMock.execute(any(HttpUriRequest.class))).thenReturn(httpResponseMock);
    } else {
      when(httpClientMock.execute(any(HttpUriRequest.class))).thenThrow(new IOException());
    }
    Sender sender = spy(new TestSender(httpClientMock));
    doReturn(httpPostMock).when(sender).createHttpPost(message);

    doReturn(responseJson).when(sender).getResponseEntity(httpResponseMock);

    return sender.send(message, json -> json.has("attachment_id"));
  }
}
