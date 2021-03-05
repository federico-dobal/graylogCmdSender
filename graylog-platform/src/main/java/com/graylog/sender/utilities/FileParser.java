package com.graylog.sender.utilities;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import com.graylog.sender.exceptions.FileParserException;
import com.graylog.sender.model.Event;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class FileParser {

    private static String CLIENT_DEVICE_TYPE = "ClientDeviceType";
    private static String CLIENT_IP = "ClientIP";
    private static String CLIENT_IP_CLASS = "ClientIPClass";
    private static String CLIENT_STATUS = "ClientStatus";
    private static String CLIENT_REQUEST_BYTES = "ClientRequestBytes";
    private static String CLIENT_REQUEST_REFERER = "ClientRequestReferer";
    private static String CLIENT_REQUEST_URI = "ClientRequestURI";
    private static String CLIENT_REQUEST_USER_AGENT = "ClientRequestUserAgent";
    private static String CLIENT_SRC_PORT = "ClientSrcPort";
    private static String EDGE_SERVER_IP = "EdgeServerIP";
    private static String EDGE_START_TIMESTAMP = "EdgeStartTimestamp";
    private static String DESTINATION_IP = "DestinationIP";
    private static String ORIGIN_RESPONSE_BYTES = "OriginResponseBytes";
    private static String ORIGIN_RESPONSE_TYME = "OriginResponseTime";

    private static Logger LOG = LoggerFactory.getLogger(FileParser.class);

    public FileParser() {
    }

    public List<Event> getAllEvents(String filename) throws FileParserException {
        try {
            return this.parseFile(filename);
        } catch (IOException e) {
            LOG.error("Error parsing the file", e.getStackTrace());
            throw new FileParserException(String.format("Error parsing the file: %s", e.getMessage()));
        }

    }

    private List<Event> parseFile(String filename) throws IOException{

        Resource resource = new ClassPathResource(filename);
        BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));

        return reader.lines().map(x -> {
            JSONObject obj = new JSONObject(x);
            return new Event(obj.get(CLIENT_DEVICE_TYPE).toString(),
                    obj.get(CLIENT_IP).toString(),
                    obj.get(CLIENT_IP_CLASS).toString(),
                    Integer.parseInt(obj.get(CLIENT_STATUS).toString()),
                    Integer.parseInt(obj.get(CLIENT_REQUEST_BYTES).toString()),
                    obj.get(CLIENT_REQUEST_REFERER).toString(),
                    obj.get(CLIENT_REQUEST_URI).toString(),
                    obj.get(CLIENT_REQUEST_USER_AGENT).toString(),
                    Integer.parseInt(obj.get(CLIENT_SRC_PORT).toString()),
                    obj.get(EDGE_SERVER_IP).toString(),
                    Integer.parseInt(obj.get(EDGE_START_TIMESTAMP).toString()),
                    obj.get(DESTINATION_IP).toString(),
                    Integer.parseInt(obj.get(ORIGIN_RESPONSE_BYTES).toString()),
                    Integer.parseInt(obj.get(ORIGIN_RESPONSE_TYME).toString()));
        }).collect(Collectors.toList());
    }
}
