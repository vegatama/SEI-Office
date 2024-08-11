package com.suryaenergi.sdm.backendapi.email.content;

import org.jetbrains.annotations.Nullable;

public class LinkContent implements TextContent {
    private String url;
    @Nullable
    private String text;

    public LinkContent(String url, @Nullable String text) {
        this.url = url;
        this.text = text;
    }

    @Override
    public void build(StringBuilder builder) {
        builder.append("<a href=\"").append(url).append("\">");
        if (text != null) {
            builder.append(text);
        } else {
            builder.append(url);
        }
        builder.append("</a>");
    }
}
