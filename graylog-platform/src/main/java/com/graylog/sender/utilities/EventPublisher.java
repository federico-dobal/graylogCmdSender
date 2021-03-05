package com.graylog.sender.utilities;

import com.graylog.sender.exceptions.EventPublisherException;
import com.graylog.sender.model.Event;
import com.squareup.okhttp.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EventPublisher {

    private static Logger LOG = LoggerFactory.getLogger(FileParser.class);



    private String GRAYLOG_URL_HOST;


    private String GRAYLOG_URL_PORT;


    private OkHttpClient client;

    public EventPublisher(@Autowired OkHttpClient client,
                          @Value("${graylog.url.host:localhost}") String graylogHost,
                          @Value("${graylog.url.port:1234}") String graylogPort) {
        this.client = client;
        this.GRAYLOG_URL_HOST = graylogHost;
        this.GRAYLOG_URL_PORT = graylogPort;
    }

    /**
     * Publishes an event on Graylog
     * @param event event to publish
     * @throws IOException
     */
    public void publish(Event event) throws EventPublisherException {
        try {
            JSONObject obj = buildGelfMessageFromEvent(event);
            publishObject(obj);
        } catch (IOException e) {
            LOG.error("Error publishing event", e.getStackTrace());
            throw new EventPublisherException(String.format("Error publishing event", e.getMessage()));
        }
    }

    private void publishObject(JSONObject obj) throws IOException {

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, obj.toString());
        Request request = new Request.Builder()
                .url(String.format("http://%s:%s/gelf", GRAYLOG_URL_HOST, GRAYLOG_URL_PORT))
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        client.newCall(request).execute();

    }

    private JSONObject buildGelfMessageFromEvent(Event event) {
        JSONObject obj = new JSONObject();
        obj.put("version", "1.1");
        obj.put("host", event.getClientIP());
        obj.put("level", "1");
        obj.put("short_message", String.format("Execution identified from client %s:%d", event.getClientIP(), event.getClientSrcPort()));
        obj.put("message", String.format("Event on client IP %s", event.getClientIP()));
        obj.put("full_message", String.format("%s%d with status[%d]", event.getClientIP(), event.getClientSrcPort(), event.getClientStatus()));
        obj.put("client_device_type", event.getClientDeviceType());
        obj.put("client_ip", event.getClientIP());
        obj.put("client_IP_class", event.getClientIPClass());
        obj.put("client_status", event.getClientStatus());
        obj.put("client_request_bytes", event.getClientRequestBytes());
        obj.put("client_request_referer", event.getClientRequestReferer());
        obj.put("client_request_URI", event.getClientRequestURI());
        obj.put("client_request_user_agent", event.getClientRequestUserAgent());
        obj.put("client_src_port", event.getClientSrcPort());
        obj.put("edge_server_IP", event.getEdgeServerIP());
        obj.put("edge_start_timestamp", event.getEdgeStartTimestamp());
        obj.put("destination_IP", event.getDestinationIP());
        obj.put("origin_response_bytes", event.getOriginResponseBytes());
        obj.put("origin_response_time", event.getOriginResponseTime());
        return obj;
    }
}
