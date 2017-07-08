package com.github.anlcnydn;

import com.github.anlcnydn.bots.SenderTest;
import com.github.anlcnydn.utils.ConvertTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ConvertTest.class, SenderTest.class, FacebookBotApiTest.class})
public class AllTests {
}
