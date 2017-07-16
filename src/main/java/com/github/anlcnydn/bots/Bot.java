package com.github.anlcnydn.bots;

import com.github.anlcnydn.models.Update;

public interface Bot {
  void onUpdateReceived(Update update);
}
