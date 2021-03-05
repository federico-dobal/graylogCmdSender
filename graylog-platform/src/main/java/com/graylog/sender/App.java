package com.graylog.sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class App implements CommandLineRunner {

    @Autowired
    FileParser fileParser;

    @Autowired
    EventPublisher eventPublisher;

    private static String MESSAGES_FILE = "sample-messages.txt";

    private static Logger LOG = LoggerFactory
            .getLogger(App.class);

    public static void main(String[] args) {
        LOG.info("STARTING THE APPLICATION");
        SpringApplication.run(App.class, args).close();
        LOG.info("APPLICATION FINISHED");
    }

    @Override
    public void run(String... args) {
        try {
            List<Event> allEventsFromFile = fileParser.getAllEvents(MESSAGES_FILE);
            for(Event event : allEventsFromFile) {
                try {
                    eventPublisher.publish(event);
                } catch (IOException e) {
                    // Log error and continue with other events
                    LOG.error("Error publishing event", e.getStackTrace());
                    continue;
                }
            }
        } catch (FileParserException e) {
            // Log exception and finish the application execution
            LOG.error("Application error while parsing the file", e.getStackTrace());
        }

    }
}