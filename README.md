![discord.jar banner](https://cdn.discordapp.com/attachments/1048910867128922172/1063823409382953042/Birthday__1_-removebg-preview_1.png)

[![seailz - discord.jar](https://img.shields.io/static/v1?label=seailz&message=discord.jar&color=blue&logo=github)](https://github.com/discord-jar/discord.jar "Go to GitHub repo") [![stars - discord.jar](https://img.shields.io/github/stars/discord-jar/discord.jar?style=social)](https://github.com/discord-jar/discord.jar) [![forks - discord.jar](https://img.shields.io/github/forks/discord-jar/discord.jar?style=social)](https://github.com/discord-jar/discord.jar) [![License](https://img.shields.io/badge/License-GNU_General_Public_License_v3.0-blue)](#license) [![issues - discord.jar](https://img.shields.io/github/issues/discord-jar/discord.jar)](https://github.com/discord-jar/discord.jar/issues)

# discord.jar - a clean Java wrapper for Discord

discord.jar [![loc - discord.jar](https://sloc.xyz/github/discord-jar/discord.jar)](https://github.com/discord-jar/discord.jar) is
a **work in progress** Java wrapper for the [Discord API](https://discord.com/developers/docs/intro).
Everything that needs doing can be found in the [Issues](https://github.com/discord-jar/discord.jar/issues) tab, so if you're
interested in helping out it would be greatly appreciated! Developed & maintained by @seailz

🧵Multi-Threaded<br>
🌐Supports interaction only/http-only bots!<br>
🔗Supports linked roles<br>
🏃Efficient!


Our official Discord server:
https://discord.gg/tmvS8A57J4

## Getting Started

### Prerequisites

<b>You'll need to add discord.jar to your project's dependencies. We are currently using
JitPack to host our builds. See tutorials for your dependency management
system [here](https://jitpack.io/#discord-jar/discord.jar/-SNAPSHOT).</b>

<details>
<summary>Above will change soon</summary>
<br>
When discord.jar officially releases, we'll be using our own repository. (Thanks @joeecodes). I'm also looking into getting djar on maven central.
</details>

A Discord bot token is required to use the API. You can get one by creating a bot
account [here](https://discord.com/developers/applications).

### Creating a Bot

To initialize a bot that uses the gateway (is able to receive events), you can use the following code:

```java
new DiscordJar("token");
```

You can specify intents to use with the gateway by using the following code:

```java
new DiscordJar("token", EnumSet.of(Intent.GUILDS, Intent.GUILD_MESSAGES));
```

Note: You can use the `Intent.ALL` constant to specify all intents. This does not include privileged intents.

### Creating an HTTP-Only bot

To make your bot an <a href="https://discord.com/developers/docs/topics/gateway#privileged-intents">HTTP only bot</a>,
you'll need to specify a couple more parameters.

```java
new DiscordJar("token", true,
        new HTTPOnlyInfo(
        "interactions",
        "EXAMPLE_APPLICATION_PUBLIC_KEY" // this cxan be found in your application's page in the dev panel
));
```

You should set `"interactions"` to whatever endpoint you want to use to receive post requests from Discord. This will be
the endpoint you set in the Discord Developer Portal.

This WILL break some methods and is only recommended to be used if you know what you are doing.
Otherwise, making a normal bot is recommended.
HTTP-only bots (or Interaction-only bots) are bots that do not connect to the gateway, and therefore cannot receive
events.
They receive interactions through POST requests to a specified endpoint of your bot.
This is useful if you want to make a bot that only uses slash commands.
<p>
Voice <b>will not work</b>, neither will setting your status & most gateway events.
<br>Interaction-based events will still be delivered as usual.
<p>
To change the port of your web server, create a new file in the running directory called `application.properties` and add the following line:

```
server.port=8081
```

#### Getting it set up in the Discord Developer Portal

1. Go to the [Discord Developer Portal](https://discord.com/developers/applications)
2. You'll want to start your bot if you haven't already.
3. Select your bot.
4. Go to the "General Information" tab.
5. Scroll down to "INTERACTIONS ENDPOINT URL" and set it to the URL of your bot's endpoint. For example, if my bot's IP
   was 123, the port was 321, and the endpoint was "interactions", I would set it to `http://123:321/interactions`.

If it fails to save after that, please contact `Seailz#0001` on Discord.

## Documentation

### Linked Roles
As far as I know, discord.jar is the only Java library to support linked roles, so here's a quick guide on how to use them:

1. First, lets get everything set up in the developer portal. Start by going to the OAuth2 > General tab.
2) You'll want to add a redirect URL, which should be the place where you are going to host your bot. For now, set it to `http://localhost:8080/verify`

![image](https://user-images.githubusercontent.com/81972974/214909363-f00f64b3-5dc0-4afd-b500-73ceb1cacfee.png)

3. Once you've done that, save your changes and go to the OAuth2 > URL Generator tab. On this page you'll see a big menu of scopes, and you need to select `identify` & `role_connections.write`.
4. After those are selected, a "SELECT REDIRECFT URL" drop down will appear - select the url you set earlier.

![image](https://user-images.githubusercontent.com/81972974/214909838-b07cb4a5-9f30-482c-bf29-d52ef96eab2d.png)

5. You'll end up with a "GENERATED URL" field. Note this down.
6. Head to the General Information tab, and scroll down until you find "LINKED ROLES VERIFICATION URL". Set this to the URL you noted in the last step.
![image](https://user-images.githubusercontent.com/81972974/214910245-98906c89-21e0-44a3-a922-45dbb2fbe9b8.png)
7. Now, invite the bot to your server as usual, and you should be able to make a Linked Role! (As of writing, linked roles are only 50% rolled out yet).
8. We actually need to implement this into our code now. Head to your code, and you'll want to add this:
```java
        discordJar.getSelfInfo().setRoleConnections(
                new ApplicationRoleConnectionMetadata(
                        ApplicationRoleConnectionMetadata.Type.BOOLEAN_EQUAL,
                        "isdeveloper",
                        "Developer",
                        "Is a Developer"
                )
        );
```
This code informs Discord of what role connection metadata you would like to have, so feel free to modify the metadata or add different ones.
9. We can now start getting set up with our webserver. You'll need a MySQL database for this. Here's an example of how to do that:
```java
        Database db = new Database(
                "DATABASE_IP", // set these to your database credentials
                3306,
                "DATABASE_USER",
                "DATABASE_PASS",
                "DATABASE_NAME"
        );

        LinkedRoles lr = new LinkedRoles(
                "CLIENT_ID", // can be found in the oauth2 > general tab
                "CLIENT_SECRET", // can be found in the oauth2 > general tab
                "http://localhost:8080/verify",
                "/verify",
                "APPLICATION_ID", // can be found in the GENERAL_INFORMATION tab
                true, // should discord.jar redirect back to https://discord.com/oauth2/authorized when code is completed?
                discordJar, // your discord.jar instance
                db
        );
```
10. Great! Now when you start your bot, it should be listenings to connections to the web server and will store data accordingly.
11. Finally, to set metadata for a user, you can use this method:
```java
        HashMap<String, Object> values = new HashMap<>();
        values.put("isdeveloper", 1); // discord requires boolean fields to use 1, or 0. (I don't know why)
        lr.updateRoles("USER_ID", "discord.jar", "Seailz", values); // you can adjust these values accordingly
```

Examples can be found [here](https://github.com/discord-jar/discord.jar/tree/main/examples)
<br>Javadocs can be found [here](https://discord-jar.github.io/discord.jar).

## Contributing

If you want to contribute to this project, feel free to do so! Everything that needs doing can be found in
the [Issues](https://github.com/discord-jar/discord.jar/issues) tab,
and if an issue there has the **available** tag you are free to make a PR fixing/adding it! :)

Make sure you first check the [active PRs](https://github.com/discord-jar/discord.jar/pulls) and branches for the feature/bug
you're fixing/adding.
If you make a PR for an issue with the **claimed** tag, and you are not the one who claimed it, (or in other words if
there is a PR open for the issue you're looking to fix/add), then your PR will be closed.

If you've opened a PR, but it's not yet finished, please mark it as a draft PR.
Branches should be named either:
`feature/[feature]`
or
`bug/[bugfix]`
or
`other[description]`.

To contribute to the `/examples` module, please see [here](https://github.com/discord-jar/discord.jar/tree/main/examples).
`/.github` is off limits for contirubtions, please make an issue if there is an error in that folder.

## License
License info can be found [here](https://github.com/discord-jar/discord.jar/blob/main/LICENSE). This project is licensed under GNU General Public License V3


## Contact
Our official Discord server:
https://discord.gg/tmvS8A57J4

## Support me :)
<a href="https://github.com/sponsors/seailz">
 <img alt="Ghsponsors Singular badge" height="56" href="https://github.com/seailz" src="https://cdn.jsdelivr.net/gh/intergrav/devins-badges/assets/cozy/donate/ghsponsors-singular_vector.svg">
</a>

<a href="https://ko-fi.com/discordjv" target="_blank">
 <img alt="Kofi Singular badge" height="56" src="https://cdn.jsdelivr.net/gh/intergrav/devins-badges/assets/cozy/donate/kofi-singular_vector.svg">
</a>
