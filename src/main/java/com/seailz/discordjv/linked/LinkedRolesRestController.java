package com.seailz.discordjv.linked;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.linked.response.Response;
import com.seailz.discordjv.linked.response.error.CodeNotPresentResponse;
import com.seailz.discordjv.linked.response.error.InvalidEndpointResponse;
import com.seailz.discordjv.utils.URLS;
import com.seailz.discordjv.utils.discordapi.DiscordRequest;
import com.seailz.discordjv.utils.discordapi.DiscordResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * This is an internal class used by discord.jv & spring to handle OAuth2 requests.
 *
 * @author Seailz
 * @since  1.0
 * @see    LinkedRoles
 */
@RestController
public class LinkedRolesRestController {

    private static String clientId;
    private static String clientSecret;
    private static String redirectEndpoint;
    private static String redirectUrl;
    private static DiscordJv discordJv;

    @RequestMapping(value="/*", method = {org.springframework.web.bind.annotation.RequestMethod.GET, org.springframework.web.bind.annotation.RequestMethod.POST})
    protected Response request(@RequestParam Map<String, String> reqParam, @NotNull HttpServletRequest request, @NotNull HttpServletResponse response) throws IOException, InterruptedException, URISyntaxException {
        String endpoint = request.getRequestURI();
        if (endpoint.equals(redirectEndpoint) && !endpoint.equals("")) {
            String code = reqParam.get("code");
            if (code == null) {
                Logger.getLogger("discord.jv").warning("Code was null, were you redirected by Discord?");
                return new CodeNotPresentResponse();
            }

            HashMap<String, String> params = new HashMap<>();
            params.put("client_id", clientId);
            params.put("client_secret", clientSecret);
            params.put("grant_type", "authorization_code");
            params.put("code", code);
            params.put("redirect_uri", LinkedRolesRestController.redirectUrl);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(new URI("https://discord.com/api/oauth2/token"))
                    .POST(getParamsUrlEncoded(params))
                    .headers("Content-Type", "application/x-www-form-urlencoded")
                    .build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            System.out.println(res.body());

            response.setHeader("Location", "https://discord.com/oauth2/authorized");
            response.setStatus(302);
        }

        return new InvalidEndpointResponse();
    }

    protected static void set(String clientId, String clientSecret, String redirectUrl, String redirectEndpoint, DiscordJv discordJv) {
        LinkedRolesRestController.clientId = clientId;
        LinkedRolesRestController.clientSecret = clientSecret;
        LinkedRolesRestController.redirectEndpoint = redirectEndpoint;
        LinkedRolesRestController.discordJv = discordJv;
        LinkedRolesRestController.redirectUrl = redirectUrl;
    }

    private HttpRequest.BodyPublisher getParamsUrlEncoded(Map<String, String> parameters) {
        String urlEncoded = parameters.entrySet()
                .stream()
                .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
        return HttpRequest.BodyPublishers.ofString(urlEncoded);
    }
}
