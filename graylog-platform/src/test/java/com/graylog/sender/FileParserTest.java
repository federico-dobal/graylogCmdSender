package com.graylog.sender;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Unit test for FileParserTest class.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class FileParserTest extends TestCase {

    @Autowired
    private FileParser fileParser;

    private static String MESSAGES_FILE = "sample-10-messages.txt";
    private static String MESSAGES_FILE_1_EVENT = "sample-1-messages.txt";
    private static String MESSAGES_EMPTY_FILE = "sample-messages-empty.txt";
    private static String MESSAGES_FILE_NOT_FOUND = "sample-messages-not-found.txt";

    @Test
    public void testGetAllEventsSuccessfulParse10Messages() throws FileParserException{
        assertEquals(10, fileParser.getAllEvents(MESSAGES_FILE).size());
    }

    @Test
    public void testGetAllEventsSuccessfulRetrieveDetails() throws FileParserException{
        List<Event> allEvents = fileParser.getAllEvents(MESSAGES_FILE_1_EVENT);
        assertEquals(1, allEvents.size());
        Event actualEvent = allEvents.get(0);

        assertEquals("desktop", actualEvent.getClientDeviceType());
        assertEquals("11.73.87.52", actualEvent.getClientIP());
        assertEquals("noRecord", actualEvent.getClientIPClass());
        assertEquals(403, actualEvent.getClientStatus().intValue());
        assertEquals(889, actualEvent.getClientRequestBytes().intValue());
        assertEquals("graylog.org", actualEvent.getClientRequestReferer());
        assertEquals("/search", actualEvent.getClientRequestURI());
        assertEquals("Mozilla/5.0 (compatible, MSIE 11, Windows NT 6.3; Trident/7.0; rv:11.0) like Gecko", actualEvent.getClientRequestUserAgent());
        assertEquals(122, actualEvent.getClientSrcPort().intValue());
        assertEquals("156.20.151.71", actualEvent.getEdgeServerIP());
        assertEquals(1576929197, actualEvent.getEdgeStartTimestamp().intValue());
        assertEquals("115.242.153.30", actualEvent.getDestinationIP());
        assertEquals(821, actualEvent.getOriginResponseBytes().intValue());
        assertEquals(337000000, actualEvent.getOriginResponseTime().intValue());
    }

    @Test
    public void testGetAllEventsSuccessfulEmptyFile() throws FileParserException{
        assertEquals(0, fileParser.getAllEvents(MESSAGES_EMPTY_FILE).size());
    }

    @Test(expected = FileParserException.class)
    public void testGetAllEventsFileNotFound() throws FileParserException{
        fileParser.getAllEvents(MESSAGES_FILE_NOT_FOUND);
    }
}
