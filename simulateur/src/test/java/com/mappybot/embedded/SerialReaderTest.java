package com.mappybot.embedded;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class SerialReaderTest {
    private final SerialReader reader = new SerialReader();
    private final MessageListener listener = mock(MessageListener.class);

    @Before
    public void before() {
        reader.addMessageListener(listener);
    }

    @Test
    public void should_notify_for_full_message() throws Exception {
        reader.feedChars("sonar100.0_");

        verify(listener).newMessage("sonar100.0_");
    }

    @Test
    public void should_not_notify_incomplete_message() throws Exception {
        reader.feedChars("sonar100");

        verifyZeroInteractions(listener);
    }

    @Test
    public void should_reconstitute_message() throws Exception {
        reader.feedChars("sonar10");
        reader.feedChars("1.0_");

        verify(listener).newMessage("sonar101.0_");
    }

    @Test
    public void should_reconstitute_multiple_messages() throws Exception {
        reader.feedChars("sonar10");
        reader.feedChars("1.0_son");
        reader.feedChars("ar1");
        reader.feedChars("02.0_so");
        reader.feedChars("nar");
        reader.feedChars("103.0_s");

        verify(listener).newMessage("sonar101.0_");
        verify(listener).newMessage("sonar102.0_");
        verify(listener).newMessage("sonar103.0_");
        verifyNoMoreInteractions(listener);
    }
}
