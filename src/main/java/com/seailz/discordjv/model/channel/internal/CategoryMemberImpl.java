package com.seailz.discordjv.model.channel.internal;

import com.seailz.discordjv.model.channel.Category;
import com.seailz.discordjv.model.channel.CategoryMember;

public record CategoryMemberImpl(Category owner) implements CategoryMember {}