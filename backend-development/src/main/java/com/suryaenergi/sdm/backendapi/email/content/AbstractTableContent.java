package com.suryaenergi.sdm.backendapi.email.content;

import com.suryaenergi.sdm.backendapi.email.Alignment;
import com.suryaenergi.sdm.backendapi.email.SizeUnit;
import com.suryaenergi.sdm.backendapi.email.EmailContent;
import com.suryaenergi.sdm.backendapi.email.EmailUtil;

import java.awt.*;
import java.util.List;

public abstract class AbstractTableContent implements EmailContent {

    protected final List<ChildData> children;

    public AbstractTableContent(List<ChildData> children) {
        this.children = children;
    }

    protected void buildTableHeader(StringBuilder builder) {
        builder.append("<table style=\"border-collapse:collapse;width:100%;\">");
    }

    protected void buildTableItem(StringBuilder builder, ChildData child) {
        builder.append("<td style=\"");
        SizeUnit width = child.getWidth();
        if (width != null) {
            builder.append("min-width:");
            width.build(builder);
            builder.append(";");
            builder.append("width:");
            width.build(builder);
            builder.append(";");
        }
        SizeUnit height = child.getHeight();
        if (height != null) {
            builder.append("min-height:");
            height.build(builder);
            builder.append(";");
            builder.append("height:");
            height.build(builder);
            builder.append(";");
        }
        SizeUnit topPadding = child.getPaddingTop();
        if (topPadding != null) {
            builder.append("padding-top:");
            topPadding.build(builder);
            builder.append(";");
        } else {
            builder.append("padding-top:0;");
        }
        SizeUnit bottomPadding = child.getPaddingBottom();
        if (bottomPadding != null) {
            builder.append("padding-bottom:");
            bottomPadding.build(builder);
            builder.append(";");
        } else {
            builder.append("padding-bottom:0;");
        }
        SizeUnit leftPadding = child.getPaddingLeft();
        if (leftPadding != null) {
            builder.append("padding-left:");
            leftPadding.build(builder);
            builder.append(";");
        } else {
            builder.append("padding-left:0;");
        }
        SizeUnit rightPadding = child.getPaddingRight();
        if (rightPadding != null) {
            builder.append("padding-right:");
            rightPadding.build(builder);
            builder.append(";");
        } else {
            builder.append("padding-right:0;");
        }
        Color backgroundColor = child.getBackgroundColor();
        if (backgroundColor != null) {
            builder.append("background-color:");
            builder.append(EmailUtil.colorToHex(backgroundColor));
            builder.append(";");
        }
        Alignment alignment = child.getAlignment();
        if (alignment != null) {
            switch (alignment) {
                case TOP_LEFT:
                    builder.append("vertical-align:top;text-align:left;");
                    break;
                case TOP_CENTER:
                    builder.append("vertical-align:top;text-align: enter;");
                    break;
                case TOP_RIGHT:
                    builder.append("vertical-align:top;text-align:right;");
                    break;
                case CENTER_LEFT:
                    builder.append("vertical-align:middle;text-align:left;");
                    break;
                case CENTER:
                    builder.append("vertical-align:middle;text-align:center;");
                    break;
                case CENTER_RIGHT:
                    builder.append("vertical-align:middle;text-align:right;");
                    break;
                case BOTTOM_LEFT:
                    builder.append("vertical-align:bottom;text-align:left;");
                    break;
                case BOTTOM_CENTER:
                    builder.append("vertical-align:bottom;text-align:center;");
                    break;
                case BOTTOM_RIGHT:
                    builder.append("vertical-align:bottom;text-align:right;");
                    break;
            }
        }
        EmailContent content = child.getContent();
        if (content != null) {
            builder.append("\">");
            content.build(builder);
            builder.append("</td>");
        } else {
            builder.append("\"/>");
        }
    }

}
