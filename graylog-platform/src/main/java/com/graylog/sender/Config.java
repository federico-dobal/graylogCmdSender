package com.graylog.sender;

import com.squareup.okhttp.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.graylog.sender")
public class Config {

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient();
    }
}
