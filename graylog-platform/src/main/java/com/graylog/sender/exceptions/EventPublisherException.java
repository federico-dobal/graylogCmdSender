package com.graylog.sender.exceptions;

import java.io.IOException;

public class EventPublisherException extends IOException {
    public EventPublisherException(String message) {
        super(message);
    }
}
