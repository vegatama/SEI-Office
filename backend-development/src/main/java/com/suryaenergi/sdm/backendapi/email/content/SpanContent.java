package com.suryaenergi.sdm.backendapi.email.content;

import com.suryaenergi.sdm.backendapi.email.EmailContent;

public class SpanContent implements EmailContent {

    private final Object content;

    public SpanContent(Object content) {
        this.content = content;
    }

    @Override
    public void build(StringBuilder builder) {
        if (content instanceof EmailContent) {
            builder.append("<span>");
            ((EmailContent) content).build(builder);
            builder.append("</span>");
            return;
        }
        builder.append("<span>").append(content).append("</span>");
    }

}
