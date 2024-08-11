package com.suryaenergi.sdm.backendapi.email.content;


import com.suryaenergi.sdm.backendapi.email.EmailContent;

public class ParagraphContent implements TextContent {
    private final Object content;

    public ParagraphContent(Object content) {
        this.content = content;
    }

    @Override
    public void build(StringBuilder builder) {
        if (content instanceof EmailContent) {
            builder.append("<p>");
            ((EmailContent) content).build(builder);
            builder.append("</p>");
            return;
        }
        builder.append("<p>")
                .append(content).append("</p>");
    }
}
