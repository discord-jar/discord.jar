package com.seailz.discordjv.model.channel.internal;

import com.seailz.discordjv.model.channel.Category;
import com.seailz.discordjv.model.channel.CategoryMember;

public class CategoryMemberImpl implements CategoryMember {
    private final Category owner;

    public CategoryMemberImpl(Category owner) {
        this.owner = owner;
    }

    @Override
    public Category owner() {
        return this.owner;
    }
}