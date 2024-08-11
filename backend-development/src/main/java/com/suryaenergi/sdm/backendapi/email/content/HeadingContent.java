package com.suryaenergi.sdm.backendapi.email.content;

public class HeadingContent implements TextContent {
    private final String heading;
    private final int level;

    public HeadingContent(String heading, int level) {
        this.heading = heading;
        this.level = level;
    }

    @Override
    public void build(StringBuilder builder) {
        builder.append("<h").append(level).append(">")
                .append(heading).append("</h").append(level).append(">");
    }
}
