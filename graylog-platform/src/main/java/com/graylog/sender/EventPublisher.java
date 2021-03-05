package com.graylog.sender;

import com.squareup.okhttp.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EventPublisher {

    @Value("${graylog.url.host}")
    private String GRAYLOG_URL_HOST;

    @Value("${graylog.url.port}")
    private String GRAYLOG_URL_PORT;

    private OkHttpClient client;

    @Autowired
    public EventPublisher(OkHttpClient client) {
        this.client = client;
    }

    /**
     * Publishes an event on Graylog
     * @param event event to publish
     * @throws IOException
     */
    public void publish(Event event) throws IOException {
        JSONObject obj = buildGelfMessageFromEvent(event);
        publishObject(obj);
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
