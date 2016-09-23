package com.github.anlcnydn.interfaces;

import org.json.JSONObject;

public interface AttachmentPayload extends BotApiObject {
  JSONObject toJson();
}
