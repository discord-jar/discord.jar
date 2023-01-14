![discord.jv banner](https://cdn.discordapp.com/attachments/1048910867128922172/1063823409382953042/Birthday__1_-removebg-preview_1.png)

[![seailz - discord.jv](https://img.shields.io/static/v1?label=seailz&message=discord.jv&color=blue&logo=github)](https://github.com/seailz/discord.jv "Go to GitHub repo") [![stars - discord.jv](https://img.shields.io/github/stars/seailz/discord.jv?style=social)](https://github.com/seailz/discord.jv) [![forks - discord.jv](https://img.shields.io/github/forks/seailz/discord.jv?style=social)](https://github.com/seailz/discord.jv) [![License](https://img.shields.io/badge/License-GNU_General_Public_License_v3.0-blue)](#license) [![issues - discord.jv](https://img.shields.io/github/issues/seailz/discord.jv)](https://github.com/seailz/discord.jv/issues)

# discord.jar - a clean Java wrapper for Discord

discord.jar [![loc - discord.jar](https://sloc.xyz/github/discord-jar/discord.jar)](https://github.com/discord-jar/discord.jar) is
a **work in progress** Java wrapper for the [Discord API](https://discord.com/developers/docs/intro).
Everything that needs doing can be found in the [Issues](https://github.com/seailz/discord.jv/issues) tab, so if you're
interested in helping out it would be greatly appreciated!

## Getting Started

### Prerequisites

You'll need to add discord.jar to your project's dependencies. We are currently using
jitpack to host our builds. See tutorials for your dependency management
system [here](https://jitpack.io/#discord-jar/discord.jar/-SNAPSHOT).

A Discord bot token is required to use the API. You can get one by creating a bot
account [here](https://discord.com/developers/applications).

### Creating a Bot

To initialize a bot that uses the gateway (is able to receive events), you can use the following code:

```java
new DiscordJv("token");
```

You can specify intents to use with the gateway by using the following code:

```java
new DiscordJv("token", EnumSet.of(Intent.GUILDS, Intent.GUILD_MESSAGES));
```

Note: You can use the `Intent.ALL` constant to specify all intents. This does not include privileged intents.

### Creating an HTTP-Only bot

To make your bot an <a href="https://discord.com/developers/docs/topics/gateway#privileged-intents">HTTP only bot</a>,
you'll need to specify a couple more parameters.

```java
new DiscordJv("token",EnumSet.of(Intents.GUILDS,Intents.GUILD_MESSAGES), APIVersion.getLatest(), true,
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

There is currently no documentation (excluding the tiny bit above), but it will be
available [here](https://discord-jv.gitbook.io/discord.jv-documentation/) when it's ready. Although there is no
documentation, there are still examples which are
avaliable [here](https://github.com/discord-jar/discord.jar/tree/main/examples)

Javadocs can be found [here](https://discord-jar.github.io/discord.jar).

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

## Dependencies
Springframework - web sockets - may be removed soon, <p>
org.json - json managing - may be removed soon, <p>
jetbrains annotations (should stay?)

## License
License info can be found [here](https://github.com/discord-jar/discord.jar/blob/main/LICENSE). This project is licensed under GNU General Public License V3


## Contact
[![](https://dcbadge.vercel.app/api/server/3cF5xeT3eV)]([https://discord.gg/INVITEID](https://discord.gg/3cF5xeT3eV))

## Donations
<a href="https://github.com/sponsors/seailz">
 <img alt="Ghsponsors Singular badge" height="56" href="https://github.com/seailz" src="https://cdn.jsdelivr.net/gh/intergrav/devins-badges/assets/cozy/donate/ghsponsors-singular_vector.svg">
</a>

<a href="https://ko-fi.com/discordjv" target="_blank">
 <img alt="Kofi Singular badge" height="56" src="https://cdn.jsdelivr.net/gh/intergrav/devins-badges/assets/cozy/donate/kofi-singular_vector.svg">
</a>
