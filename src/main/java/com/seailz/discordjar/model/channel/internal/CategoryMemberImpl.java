package com.seailz.discordjar.model.channel.internal;

import com.seailz.discordjar.model.channel.Category;
import com.seailz.discordjar.model.channel.CategoryMember;

public record CategoryMemberImpl(Category owner) implements CategoryMember {}