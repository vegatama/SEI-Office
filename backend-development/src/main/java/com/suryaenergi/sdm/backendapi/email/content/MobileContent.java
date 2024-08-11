package com.suryaenergi.sdm.backendapi.email.content;

import com.suryaenergi.sdm.backendapi.email.EmailContent;

public class MobileContent implements EmailContent {
    private final EmailContent content;

    public MobileContent(EmailContent content) {
        this.content = content;
    }

    @Override
    public void build(StringBuilder builder) {
        // using table to make the content responsive
        builder.append("<table class=\"table_mobile_content\" style=\"width: 100%;height:100%;border-collapse: collapse;\">");
        builder.append("<tr><td style=\"padding:0;\">");
        content.build(builder);
        builder.append("</td></tr>");
        builder.append("</table>");
    }
}
