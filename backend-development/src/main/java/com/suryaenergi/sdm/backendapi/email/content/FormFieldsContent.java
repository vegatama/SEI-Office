package com.suryaenergi.sdm.backendapi.email.content;

import com.suryaenergi.sdm.backendapi.email.EmailContent;

import java.util.HashMap;
import java.util.Map;

public class FormFieldsContent implements EmailContent {
    private final Map<String, Object> formFields;

    public FormFieldsContent(Map<String, Object> formFields) {
        this.formFields = formFields;
    }

    public FormFieldsContent(String key, Object value) {
        this.formFields = new HashMap<>();
        this.formFields.put(key, value);
    }

    public FormFieldsContent with(String key, Object value) {
        this.formFields.put(key, value);
        return this;
    }

    public FormFieldsContent with(Map<String, Object> formFields) {
        this.formFields.putAll(formFields);
        return this;
    }


    @Override
    public void build(StringBuilder builder) {
        builder.append("<table style=\"border-collapse: collapse;\">");
        for (Map.Entry<String, ?> entry : formFields.entrySet()) {
            builder.append("<tr>");
            builder.append("<td align=\"left\" valign=\"top\" style=\"padding: 0 20px 5px 5px;\"><b>").append(entry.getKey()).append("</b></td>");
            builder.append("<td align=\"left\" valign=\"top\" style=\"padding:0 0 5px 0\">")
                    .append("<b>:</b> ")
                    .append(entry.getValue()).append("</td>");
            builder.append("</tr>");
        }
        builder.append("</table>");
    }
}
