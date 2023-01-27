package com.seailz.discordjv.http;

import com.seailz.discordjv.DiscordJv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HttpOnlyApplication {

    public HttpOnlyApplication() {
    }

    public static void init(DiscordJv discordJv, String endpoint, String applicationPublicKey) {
        HttpOnlyManager.init(discordJv, endpoint, applicationPublicKey);
        SpringApplication.run(HttpOnlyApplication.class);
    }

}
