package com.graylog.sender;

import com.graylog.sender.exceptions.EventPublisherException;
import com.graylog.sender.model.Event;
import com.graylog.sender.utilities.EventPublisher;
import com.squareup.okhttp.*;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit test for FileParserTest class.
 */
@SpringBootTest(properties = { "graylog.url.host=127.0.0.1", "graylog.url.port=1234" },
args = "path")
@TestPropertySource(properties={ "graylog.url.host=127.0.0.1", "graylog.url.port=1234" })
@RunWith(SpringRunner.class)
public class EventPublisherTest extends TestCase {

    @Mock
    OkHttpClient okHttpClient;

    @Mock
    Call call;

    private EventPublisher eventPublisher;

    @Before
    public void setUp() {
        eventPublisher = new EventPublisher(okHttpClient, "localhost", "123");
        ReflectionTestUtils.setField(eventPublisher, "client", okHttpClient);
    }

    /**
     * Tests an event can be successfully published on Graylog
     * @throws IOException
     */
    @Test
    public void testPublishAnEventSuccessfully() throws IOException {
         when(call.execute()).thenReturn(new Response.Builder()
                 .code(200)
                 .protocol(Protocol.HTTP_2)
                 .request(new Request.Builder()
                         .url(String.format("http://%s:%s/gelf", "GRAYLOG_URL_HOST", "1234"))
                         .build())
                 .build());

        when(okHttpClient.newCall(any())).thenReturn(call);

        eventPublisher.publish(new Event());
    }

    /**
     * Test the publish method convert properly IOException into EventPublisherException
     * @throws IOException
     */
    @Test(expected = EventPublisherException.class)
    public void testPublishAnEventError() throws IOException {
        when(call.execute()).thenThrow(IOException.class);
        when(okHttpClient.newCall(any())).thenReturn(call);

        eventPublisher.publish(new Event());
    }

}
