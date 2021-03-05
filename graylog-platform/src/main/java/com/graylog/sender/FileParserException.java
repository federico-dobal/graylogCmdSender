package com.graylog.sender;

import java.io.IOException;

public class FileParserException extends IOException {
    public FileParserException(String message) {
        super(message);
    }
}
