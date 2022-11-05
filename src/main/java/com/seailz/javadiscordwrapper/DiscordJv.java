package com.seailz.javadiscordwrapper;

import com.seailz.javadiscordwrapper.model.Application;
import com.seailz.javadiscordwrapper.model.User;
import com.seailz.javadiscordwrapper.utils.Requester;
import com.seailz.javadiscordwrapper.utils.URLS;
import com.seailz.javadiscordwrapper.utils.version.APIVersion;
import org.json.JSONObject;

import java.io.*;

/**
 * The main class of the Discord.jv wrapper
 * @author Seailz
 * @since 1.0
 */
public class DiscordJv {

    /**
     * The token of the bot
     */
    private String token;

    public DiscordJv(String token) {
        this.token = token;
    }

    public static void main(String[] args) {
        String token = "";
        File file = new File("token.txt");
        // get first line of that file
        try (FileReader reader = new FileReader(file)) {
            token = new BufferedReader(reader).readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(new DiscordJv(token).getSelfInfo().owner().username());
    }

    /**
     * Returns the bot's token inputted in the constructor
     */
    public String getToken() {
        return token;
    }

    public Application getSelfInfo() {
        return Application.decompile(Requester.get(URLS.GET.APPLICATION.APPLICATION_INFORMATION, this));
    }

}
