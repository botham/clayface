package com.github.anlcnydn.bots;

import com.github.anlcnydn.FacebookApiException;
import com.github.anlcnydn.models.Message;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class SenderTest {

  private Sender senderSpy = spy(Sender.class);

  /**
   * Unit tests for
   * @see Sender#sendMessage(Message)
   */
  @Test(expected = FacebookApiException.class)
  public void sendMessage_with_null_Message() throws Exception {
    senderSpy.sendMessage(null);
  }

  @Test
  public void sendMessage_with_uploadable_Message() throws Exception {
    Message messageMock = mock(Message.class);
    doReturn(true).when(messageMock).hasUploadable();
    doReturn(true).when(senderSpy).sendUploadableMessage(messageMock);

    boolean sendResult = senderSpy.sendMessage(messageMock);
    assertTrue(sendResult);
    verify(senderSpy).sendUploadableMessage(messageMock);
  }

  @Test
  public void sendMessage_with_non_uploadable_Message() throws Exception {
    Message messageMock = mock(Message.class);
    doReturn(false).when(messageMock).hasUploadable();
    doReturn(true).when(senderSpy).send(messageMock);

    boolean sendResult = senderSpy.sendMessage(messageMock);
    assertTrue(sendResult);
    verify(senderSpy).send(messageMock);
  }
}
