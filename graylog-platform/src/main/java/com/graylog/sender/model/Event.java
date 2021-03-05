package com.graylog.sender.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Event {

    private String clientDeviceType;
    private String clientIP;
    private String clientIPClass;
    private Integer clientStatus;
    private Integer clientRequestBytes;
    private String clientRequestReferer;
    private String clientRequestURI;
    private String clientRequestUserAgent;
    private Integer clientSrcPort;
    private String edgeServerIP;
    private Integer edgeStartTimestamp;
    private String destinationIP;
    private Integer originResponseBytes;
    private Integer originResponseTime;

}
