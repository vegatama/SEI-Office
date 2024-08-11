package com.suryaenergi.sdm.backendapi.email.content;

import com.suryaenergi.sdm.backendapi.email.EmailContent;

public class ContainerContent implements EmailContent {
    private final EmailContent content;

    public ContainerContent(EmailContent content) {
        this.content = content;
    }

    @Override
    public void build(StringBuilder builder) {
        // Container, with align center, shadow, and padding
        // Using a table to center the content
        builder.append("<table class=\"table_container\" style=\"width: 100%; text-align: center; box-shadow: 0 0 10px 0 rgba(0, 0, 0, 0.1); border-collapse: collapse;\">");
        builder.append("<tr><td style=\"padding:0;\">");
        content.build(builder);
        builder.append("</td></tr>");
        builder.append("</table>");
    }
}
