package com.suryaenergi.sdm.backendapi.email.content;

import com.suryaenergi.sdm.backendapi.email.EmailContent;

public class ButtonContent implements EmailContent {
    public static final String VARIANT_PRIMARY = "button_primary"; // Filled button
    public static final String VARIANT_SECONDARY = "button_secondary"; // Outlined button
    public static final String VARIANT_TERTIARY = "button_tertiary"; // Text button
    private final String text;
    private final String url;
    private final String variant;

    public ButtonContent(String text, String url, String variant) {
        this.text = text;
        this.url = url;
        this.variant = variant;
    }

    @Override
    public void build(StringBuilder builder) {
//        builder.append("<a href=\"").append(url).append("\" class=\"button ").append(variant).append("\">").append(text).append("</a>");
        // TODO inline style
        /*
         // primary button
          builder.append(".button {");
        builder.append("display: inline-block;");
        builder.append("padding: 10px 20px;");
        builder.append("border-radius: 5px;");
        builder.append("text-decoration: none;");
        builder.append("}");
        builder.append(".button_primary {");
        builder.append("background-color: #007bff;");
        builder.append("color: white;");
        builder.append("}");

        // secondary button
        builder.append(".button_secondary {");
        builder.append("border: 1px solid #007bff;");
        builder.append("color: #007bff;");
        builder.append("}");

        // tertiary button
        builder.append(".button_tertiary {");
        builder.append("color: #007bff;");
        builder.append("}");
         */
        switch (variant) {
            case VARIANT_PRIMARY:
                builder.append("<a href=\"").append(url).append("\" style=\"display: inline-block; padding: 10px 20px; border-radius: 5px; text-decoration: none; background-color: #007bff; color: white;\">").append(text).append("</a>");
                break;
            case VARIANT_SECONDARY:
                builder.append("<a href=\"").append(url).append("\" style=\"display: inline-block; padding: 10px 20px; border-radius: 5px; text-decoration: none; border: 1px solid #007bff; color: #007bff;\">").append(text).append("</a>");
                break;
            case VARIANT_TERTIARY:
                builder.append("<a href=\"").append(url).append("\" style=\"display: inline-block; padding: 10px 20px; border-radius: 5px; text-decoration: none; color: #007bff;\">").append(text).append("</a>");
                break;
        }
    }
}
