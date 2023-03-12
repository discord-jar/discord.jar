package com.seailz.discordjar.model.channel.interfaces;

import com.seailz.discordjar.model.channel.Channel;
import com.seailz.discordjar.model.channel.MessagingChannel;
import com.seailz.discordjar.model.channel.transcript.TranscriptFormatter;
import com.seailz.discordjar.model.embed.Embed;
import com.seailz.discordjar.model.embed.EmbedField;
import com.seailz.discordjar.model.guild.Member;
import com.seailz.discordjar.model.message.Attachment;
import com.seailz.discordjar.model.message.Message;
import com.seailz.discordjar.model.user.User;
import com.seailz.discordjar.utils.Snowflake;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public interface Transcriptable extends MessageRetrievable {

    /**
     * Creates an HTML transcript of 500 of the last messages in the channel.
     * <b>To use this method, you MUST add the template.html file that can be found
     * in the recourses project of this folder, to the root directory of your project.</b>
     *
     * @return The HTML transcript.
     * @throws IOException If the file cannot be created.
     * <p><p>
     * Original author: https://github.com/Ryzeon
     * <br>Modified for use in discord.jar by Seailz
     */
    default InputStream transcript() throws IOException, DiscordRequest.UnhandledDiscordAPIErrorException {
        List<String>
                imageFormats = Arrays.asList("png", "jpg", "jpeg", "gif"),
                videoFormats = Arrays.asList("mp4", "webm", "mkv", "avi", "mov", "flv", "wmv", "mpg", "mpeg"),
                audioFormats = Arrays.asList("mp3", "wav", "ogg", "flac");

        List<Message> messages = get500Messages();
        File htmlTemplate = new File("template.html");
        if (messages.isEmpty()) {
            throw new IllegalArgumentException("No messages to generate a transcript from");
        }
        MessagingChannel channel = djv().getTextChannelById(messages.iterator().next().channelId());
        Document document = Jsoup.parse(htmlTemplate, "UTF-8");
        document.outputSettings().indentAmount(0).prettyPrint(true);
        document.getElementsByClass("preamble__guild-icon")
                .first().attr("src", "https://cdn.discordapp.com/icons/" + channel.guild().id() + "/" + channel.guild().iconHash() + ".png"); // set guild icon

        document.getElementById("transcriptTitle").text(channel.name()); // set title
        document.getElementById("guildname").text(channel.guild().name()); // set guild name
        document.getElementById("ticketname").text(channel.name()); // set channel name

        Element chatLog = document.getElementById("chatlog"); // chat log
        for (Message message : messages.stream()
                .sorted(Comparator.comparing(Snowflake::timestampRaw))
                .collect(Collectors.toList())) {
            // create message group
            Element messageGroup = document.createElement("div");
            messageGroup.addClass("chatlog__message-group");

            // message reference
            if (message.referencedMessage() != null) { // preguntar si es eso
                // message.reference?.messageId
                // create symbol
                Element referenceSymbol = document.createElement("div");
                referenceSymbol.addClass("chatlog__reference-symbol");

                // create reference
                Element reference = document.createElement("div");
                reference.addClass("chatlog__reference");

                Message referenceMessage = message.referencedMessage();
                User author = referenceMessage.author();
                Member member = channel.guild().getMemberById(author.id());
                String color = "#ffffff";

                if (member != null)
                    TranscriptFormatter.toHex(Objects.requireNonNull(new Color(member.roles()[0].color())));

                reference.html("<img class=\"chatlog__reference-avatar\" src=\""
                        + author.imageUrl() + "\" alt=\"Avatar\" loading=\"lazy\">" +
                        "<span class=\"chatlog__reference-name\" title=\"" + author.username()
                        + "\" style=\"color: " + color + "\">" + author.username() + "\"</span>" +
                        "<div class=\"chatlog__reference-content\">" +
                        " <span class=\"chatlog__reference-link\" onclick=\"scrollToMessage(event, '"
                        + referenceMessage.id() + "')\">" +
                        "<em>" +
                        referenceMessage.getFormattedText() != null
                        ? referenceMessage.getFormattedText().length() > 42
                        ? referenceMessage.getFormattedText().substring(0, 42)
                        + "..."
                        : referenceMessage.getFormattedText()
                        : "Click to see attachment" +
                        "</em>" +
                        "</span>" +
                        "</div>");

                messageGroup.appendChild(referenceSymbol);
                messageGroup.appendChild(reference);
            }

            User author = message.author();

            Element authorElement = document.createElement("div");
            authorElement.addClass("chatlog__author-avatar-container");

            Element authorAvatar = document.createElement("img");
            authorAvatar.addClass("chatlog__author-avatar");
            authorAvatar.attr("src", author.imageUrl());
            authorAvatar.attr("alt", "Avatar");
            authorAvatar.attr("loading", "lazy");

            authorElement.appendChild(authorAvatar);
            messageGroup.appendChild(authorElement);

            // message content
            Element content = document.createElement("div");
            content.addClass("chatlog__messages");
            // message author name
            Element authorName = document.createElement("span");
            authorName.addClass("chatlog__author-name");
            // authorName.attr("title", author.getName()); // author.name
            authorName.attr("title", author.getAsMention());
            authorName.text(author.username());
            authorName.attr("data-user-id", author.id());
            content.appendChild(authorName);

            if (author.bot()) {
                Element botTag = document.createElement("span");
                botTag.addClass("chatlog__bot-tag").text("BOT");
                content.appendChild(botTag);
            }

            // timestamp
            Element timestamp = document.createElement("span");
            timestamp.addClass("chatlog__timestamp");
            timestamp
                    .text(message.timestampAsOffsetDateTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

            content.appendChild(timestamp);

            Element messageContent = document.createElement("div");
            messageContent.addClass("chatlog__message");
            messageContent.attr("data-message-id", message.id());
            messageContent.attr("id", "message-" + message.id());
            messageContent.attr("title", "Message sent: "
                    + message.timestampAsOffsetDateTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

            if (message.getFormattedText().length() > 0) {
                Element messageContentContent = document.createElement("div");
                messageContentContent.addClass("chatlog__content");

                Element messageContentContentMarkdown = document.createElement("div");
                messageContentContentMarkdown.addClass("markdown");

                Element messageContentContentMarkdownSpan = document.createElement("span");
                messageContentContentMarkdownSpan.addClass("preserve-whitespace");
                messageContentContentMarkdownSpan
                        .html(TranscriptFormatter.format(message.getFormattedText()));

                messageContentContentMarkdown.appendChild(messageContentContentMarkdownSpan);
                messageContentContent.appendChild(messageContentContentMarkdown);
                messageContent.appendChild(messageContentContent);
            }

            // messsage attachments
            if (!(message.attachments() == null) && message.attachments().length > 0) {
                for (Attachment attach : message.attachments()) {
                    Element attachmentsDiv = document.createElement("div");
                    attachmentsDiv.addClass("chatlog__attachment");

                    String attachmentType = attach.fileName().substring(attach.fileName().lastIndexOf(".") + 1);
                    if (imageFormats.contains(attachmentType)) {
                        Element attachmentLink = document.createElement("a");

                        Element attachmentImage = document.createElement("img");
                        attachmentImage.addClass("chatlog__attachment-media");
                        attachmentImage.attr("src", attach.url());
                        attachmentImage.attr("alt", "Image attachment");
                        attachmentImage.attr("loading", "lazy");
                        attachmentImage.attr("title",
                                "Image: " + attach.fileName() + TranscriptFormatter.formatBytes(attach.size()));

                        attachmentLink.appendChild(attachmentImage);
                        attachmentsDiv.appendChild(attachmentLink);
                    } else if (videoFormats.contains(attachmentType)) {
                        Element attachmentVideo = document.createElement("video");
                        attachmentVideo.addClass("chatlog__attachment-media");
                        attachmentVideo.attr("src", attach.url());
                        attachmentVideo.attr("alt", "Video attachment");
                        attachmentVideo.attr("controls", true);
                        attachmentVideo.attr("title",
                                "Video: " + attach.fileName() + TranscriptFormatter.formatBytes(attach.size()));

                        attachmentsDiv.appendChild(attachmentVideo);
                    } else if (audioFormats.contains(attachmentType)) {
                        Element attachmentAudio = document.createElement("audio");
                        attachmentAudio.addClass("chatlog__attachment-media");
                        attachmentAudio.attr("src", attach.url());
                        attachmentAudio.attr("alt", "Audio attachment");
                        attachmentAudio.attr("controls", true);
                        attachmentAudio.attr("title",
                                "Audio: " + attach.size() + TranscriptFormatter.formatBytes(attach.size()));

                        attachmentsDiv.appendChild(attachmentAudio);
                    } else {
                        Element attachmentGeneric = document.createElement("div");
                        attachmentGeneric.addClass("chatlog__attachment-generic");

                        Element attachmentGenericIcon = document.createElement("svg");
                        attachmentGenericIcon.addClass("chatlog__attachment-generic-icon");

                        Element attachmentGenericIconUse = document.createElement("use");
                        attachmentGenericIconUse.attr("xlink:href", "#icon-attachment");

                        attachmentGenericIcon.appendChild(attachmentGenericIconUse);
                        attachmentGeneric.appendChild(attachmentGenericIcon);

                        Element attachmentGenericName = document.createElement("div");
                        attachmentGenericName.addClass("chatlog__attachment-generic-name");

                        Element attachmentGenericNameLink = document.createElement("a");
                        attachmentGenericNameLink.attr("href", attach.url());
                        attachmentGenericNameLink.text(attach.fileName());

                        attachmentGenericName.appendChild(attachmentGenericNameLink);
                        attachmentGeneric.appendChild(attachmentGenericName);

                        Element attachmentGenericSize = document.createElement("div");
                        attachmentGenericSize.addClass("chatlog__attachment-generic-size");

                        attachmentGenericSize.text(TranscriptFormatter.formatBytes(attach.size()));
                        attachmentGeneric.appendChild(attachmentGenericSize);

                        attachmentsDiv.appendChild(attachmentGeneric);
                    }

                    messageContent.appendChild(attachmentsDiv);
                }
            }

            content.appendChild(messageContent);

            if (message.embeds() != null && !Arrays.stream(message.embeds()).collect(Collectors.toCollection(ArrayList::new)).isEmpty()) {
                for (Embed embed : message.embeds()) {
                    if (embed == null) {
                        continue;
                    }
                    Element embedDiv = document.createElement("div");
                    embedDiv.addClass("chatlog__embed");

                    // embed color
                    if (new Color(embed.color()) != null) {
                        Element embedColorPill = document.createElement("div");
                        embedColorPill.addClass("chatlog__embed-color-pill");
                        embedColorPill.attr("style",
                                "background-color: #" + TranscriptFormatter.toHex(new Color(embed.color())));

                        embedDiv.appendChild(embedColorPill);
                    }

                    Element embedContentContainer = document.createElement("div");
                    embedContentContainer.addClass("chatlog__embed-content-container");

                    Element embedContent = document.createElement("div");
                    embedContent.addClass("chatlog__embed-content");

                    Element embedText = document.createElement("div");
                    embedText.addClass("chatlog__embed-text");

                    // embed author
                    if (embed.author() != null && embed.author().name() != null) {
                        Element embedAuthor = document.createElement("div");
                        embedAuthor.addClass("chatlog__embed-author");

                        if (embed.author().iconUrl() != null) {
                            Element embedAuthorIcon = document.createElement("img");
                            embedAuthorIcon.addClass("chatlog__embed-author-icon");
                            embedAuthorIcon.attr("src", embed.author().iconUrl());
                            embedAuthorIcon.attr("alt", "Author icon");
                            embedAuthorIcon.attr("loading", "lazy");

                            embedAuthor.appendChild(embedAuthorIcon);
                        }

                        Element embedAuthorName = document.createElement("span");
                        embedAuthorName.addClass("chatlog__embed-author-name");

                        if (embed.author().url() != null) {
                            Element embedAuthorNameLink = document.createElement("a");
                            embedAuthorNameLink.addClass("chatlog__embed-author-name-link");
                            embedAuthorNameLink.attr("href", embed.author().url());
                            embedAuthorNameLink.text(embed.author().url());

                            embedAuthorName.appendChild(embedAuthorNameLink);
                        } else {
                            embedAuthorName.text(embed.author().url());
                        }

                        embedAuthor.appendChild(embedAuthorName);
                        embedText.appendChild(embedAuthor);
                    }

                    // embed title
                    if (embed.title() != null) {
                        Element embedTitle = document.createElement("div");
                        embedTitle.addClass("chatlog__embed-title");

                        if (embed.url() != null) {
                            Element embedTitleLink = document.createElement("a");
                            embedTitleLink.addClass("chatlog__embed-title-link");
                            embedTitleLink.attr("href", embed.url());

                            Element embedTitleMarkdown = document.createElement("div");
                            embedTitleMarkdown.addClass("markdown preserve-whitespace")
                                    .html(TranscriptFormatter.format(embed.title()));

                            embedTitleLink.appendChild(embedTitleMarkdown);
                            embedTitle.appendChild(embedTitleLink);
                        } else {
                            Element embedTitleMarkdown = document.createElement("div");
                            embedTitleMarkdown.addClass("markdown preserve-whitespace")
                                    .html(TranscriptFormatter.format(embed.title()));

                            embedTitle.appendChild(embedTitleMarkdown);
                        }
                        embedText.appendChild(embedTitle);
                    }

                    // embed description
                    if (embed.description() != null) {
                        Element embedDescription = document.createElement("div");
                        embedDescription.addClass("chatlog__embed-description");

                        Element embedDescriptionMarkdown = document.createElement("div");
                        embedDescriptionMarkdown.addClass("markdown preserve-whitespace");
                        embedDescriptionMarkdown
                                .html(TranscriptFormatter.format(embed.description()));

                        embedDescription.appendChild(embedDescriptionMarkdown);
                        embedText.appendChild(embedDescription);
                    }

                    // embed fields
                    if (embed.fields() != null && !(embed.fields().length == 0)) {
                        Element embedFields = document.createElement("div");
                        embedFields.addClass("chatlog__embed-fields");

                        for (EmbedField field : embed.fields()) {
                            Element embedField = document.createElement("div");
                            embedField.addClass(field.inline() ? "chatlog__embed-field-inline"
                                    : "chatlog__embed-field");

                            // Field nmae
                            Element embedFieldName = document.createElement("div");
                            embedFieldName.addClass("chatlog__embed-field-name");

                            Element embedFieldNameMarkdown = document.createElement("div");
                            embedFieldNameMarkdown.addClass("markdown preserve-whitespace");
                            embedFieldNameMarkdown.html(field.name());

                            embedFieldName.appendChild(embedFieldNameMarkdown);
                            embedField.appendChild(embedFieldName);


                            // Field value
                            Element embedFieldValue = document.createElement("div");
                            embedFieldValue.addClass("chatlog__embed-field-value");

                            Element embedFieldValueMarkdown = document.createElement("div");
                            embedFieldValueMarkdown.addClass("markdown preserve-whitespace");
                            embedFieldValueMarkdown
                                    .html(TranscriptFormatter.format(field.value()));

                            embedFieldValue.appendChild(embedFieldValueMarkdown);
                            embedField.appendChild(embedFieldValue);

                            embedFields.appendChild(embedField);
                        }

                        embedText.appendChild(embedFields);
                    }

                    embedContent.appendChild(embedText);

                    // embed thumbnail
                    if (embed.thumbnail() != null) {
                        Element embedThumbnail = document.createElement("div");
                        embedThumbnail.addClass("chatlog__embed-thumbnail-container");

                        Element embedThumbnailLink = document.createElement("a");
                        embedThumbnailLink.addClass("chatlog__embed-thumbnail-link");
                        embedThumbnailLink.attr("href", embed.thumbnail().url());

                        Element embedThumbnailImage = document.createElement("img");
                        embedThumbnailImage.addClass("chatlog__embed-thumbnail");
                        embedThumbnailImage.attr("src", embed.thumbnail().url());
                        embedThumbnailImage.attr("alt", "Thumbnail");
                        embedThumbnailImage.attr("loading", "lazy");

                        embedThumbnailLink.appendChild(embedThumbnailImage);
                        embedThumbnail.appendChild(embedThumbnailLink);

                        embedContent.appendChild(embedThumbnail);
                    }

                    embedContentContainer.appendChild(embedContent);

                    // embed image
                    if (embed.image() != null) {
                        Element embedImage = document.createElement("div");
                        embedImage.addClass("chatlog__embed-image-container");

                        Element embedImageLink = document.createElement("a");
                        embedImageLink.addClass("chatlog__embed-image-link");
                        embedImageLink.attr("href", embed.image().url());

                        Element embedImageImage = document.createElement("img");
                        embedImageImage.addClass("chatlog__embed-image");
                        embedImageImage.attr("src", embed.image().url());
                        embedImageImage.attr("alt", "Image");
                        embedImageImage.attr("loading", "lazy");

                        embedImageLink.appendChild(embedImageImage);
                        embedImage.appendChild(embedImageLink);

                        embedContentContainer.appendChild(embedImage);
                    }

                    // embed footer
                    if (embed.footer() != null) {
                        Element embedFooter = document.createElement("div");
                        embedFooter.addClass("chatlog__embed-footer");

                        if (embed.footer().iconUrl() != null) {
                            Element embedFooterIcon = document.createElement("img");
                            embedFooterIcon.addClass("chatlog__embed-footer-icon");
                            embedFooterIcon.attr("src", embed.footer().iconUrl());
                            embedFooterIcon.attr("alt", "Footer icon");
                            embedFooterIcon.attr("loading", "lazy");

                            embedFooter.appendChild(embedFooterIcon);
                        }

                        Element embedFooterText = document.createElement("span");
                        embedFooterText.addClass("chatlog__embed-footer-text");
                        embedFooterText.text(embed.timestamp() != null
                                ? embed.footer().text() + " • " + OffsetDateTime.parse(embed.timestamp())
                                .format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                                : embed.footer().text());

                        embedFooter.appendChild(embedFooterText);

                        embedContentContainer.appendChild(embedFooter);
                    }

                    embedDiv.appendChild(embedContentContainer);
                    content.appendChild(embedDiv);
                }
            }

            messageGroup.appendChild(content);
            chatLog.appendChild(messageGroup);
        }
        return new ByteArrayInputStream(document.outerHtml().getBytes());
    }

}
