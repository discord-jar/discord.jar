package com.seailz.discordjar.http;

import com.seailz.discordjar.DiscordJar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HttpOnlyApplication {

    public HttpOnlyApplication() {
    }

    public static void init(DiscordJar discordJar, String endpoint, String applicationPublicKey) {
        HttpOnlyManager.init(discordJar, endpoint, applicationPublicKey);
        SpringApplication.run(HttpOnlyApplication.class);
    }

}
