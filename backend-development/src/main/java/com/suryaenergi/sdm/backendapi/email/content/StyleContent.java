package com.suryaenergi.sdm.backendapi.email.content;

import com.suryaenergi.sdm.backendapi.email.Alignment;
import com.suryaenergi.sdm.backendapi.email.EmailContent;
import com.suryaenergi.sdm.backendapi.email.SizeUnit;

import java.awt.*;

public class StyleContent implements EmailContent {
    private String font;
    private Double fontSize;
    private Color color;
    private Boolean bold;
    private Boolean italic;
    private Boolean underline;
    private SizeUnit width;
    private SizeUnit height;
    private SizeUnit maxWidth;
    private SizeUnit maxHeight;
    private SizeUnit paddingTop;
    private SizeUnit paddingBottom;
    private SizeUnit paddingLeft;
    private SizeUnit paddingRight;
    private SizeUnit marginTop;
    private SizeUnit marginBottom;
    private SizeUnit marginLeft;
    private SizeUnit marginRight;
    private Alignment alignment;
    private EmailContent content;

    public StyleContent(EmailContent content) {
        this.content = content;
    }

    public StyleContent width(SizeUnit width) {
        this.width = width;
        return this;
    }

    public StyleContent height(SizeUnit height) {
        this.height = height;
        return this;
    }

    public StyleContent maxWidth(SizeUnit maxWidth) {
        this.maxWidth = maxWidth;
        return this;
    }

    public StyleContent maxHeight(SizeUnit maxHeight) {
        this.maxHeight = maxHeight;
        return this;
    }

    public StyleContent paddingTop(SizeUnit paddingTop) {
        this.paddingTop = paddingTop;
        return this;
    }

    public StyleContent paddingBottom(SizeUnit paddingBottom) {
        this.paddingBottom = paddingBottom;
        return this;
    }

    public StyleContent paddingLeft(SizeUnit paddingLeft) {
        this.paddingLeft = paddingLeft;
        return this;
    }

    public StyleContent paddingRight(SizeUnit paddingRight) {
        this.paddingRight = paddingRight;
        return this;
    }

    public StyleContent marginTop(SizeUnit marginTop) {
        this.marginTop = marginTop;
        return this;
    }

    public StyleContent marginBottom(SizeUnit marginBottom) {
        this.marginBottom = marginBottom;
        return this;
    }

    public StyleContent marginLeft(SizeUnit marginLeft) {
        this.marginLeft = marginLeft;
        return this;
    }

    public StyleContent marginRight(SizeUnit marginRight) {
        this.marginRight = marginRight;
        return this;
    }

    public StyleContent alignment(Alignment alignment) {
        this.alignment = alignment;
        return this;
    }

    public StyleContent font(String font) {
        this.font = font;
        return this;
    }

    public StyleContent fontSize(Double fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    public StyleContent color(Color color) {
        this.color = color;
        return this;
    }

    public StyleContent bold(Boolean bold) {
        this.bold = bold;
        return this;
    }

    public StyleContent italic(Boolean italic) {
        this.italic = italic;
        return this;
    }

    public StyleContent underline(Boolean underline) {
        this.underline = underline;
        return this;
    }

    @Override
    public void build(StringBuilder builder) {
        builder.append("<span style=\"display:inline-block; ");
        if (font != null) {
            builder.append("font-family: ").append(font).append("; ");
        }
        if (fontSize != null) {
            builder.append("font-size: ").append(fontSize).append("px; ");
        }
        if (color != null) {
            builder.append("color: rgba(")
                    .append(color.getRed()).append(", ")
                    .append(color.getGreen()).append(", ")
                    .append(color.getBlue()).append(", ")
                    .append(color.getAlpha()).append("); ");
        }
        if (bold != null && bold) {
            builder.append("font-weight: bold; ");
        }
        if (italic != null && italic) {
            builder.append("font-style: italic; ");
        }
        if (underline != null && underline) {
            builder.append("text-decoration: underline; ");
        }
        if (width != null) {
            builder.append("min-width:");
            width.build(builder);
            builder.append(";");
            builder.append("width:");
            width.build(builder);
            builder.append(";");
        }
        if (height != null) {
            builder.append("min-height:");
            height.build(builder);
            builder.append(";");
            builder.append("height:");
            height.build(builder);
            builder.append(";");
        }
        if (maxWidth != null) {
            builder.append("max-width:");
            maxWidth.build(builder);
            builder.append(";");
        }
        if (maxHeight != null) {
            builder.append("max-height:");
            maxHeight.build(builder);
            builder.append(";");
        }
        if (paddingTop != null) {
            builder.append("padding-top:");
            paddingTop.build(builder);
            builder.append(";");
        }
        if (paddingBottom != null) {
            builder.append("padding-bottom:");
            paddingBottom.build(builder);
            builder.append(";");
        }
        if (paddingLeft != null) {
            builder.append("padding-left:");
            paddingLeft.build(builder);
            builder.append(";");
        }
        if (paddingRight != null) {
            builder.append("padding-right:");
            paddingRight.build(builder);
            builder.append(";");
        }
        if (alignment != null) {
            switch (alignment) {
                case TOP_LEFT:
                    builder.append("vertical-align:top;text-align:left;");
                    break;
                case TOP_CENTER:
                    builder.append("vertical-align:top;text-align:center;");
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
        if (marginTop != null) {
            builder.append("margin-top:");
            marginTop.build(builder);
            builder.append(";");
        }
        if (marginBottom != null) {
            builder.append("margin-bottom:");
            marginBottom.build(builder);
            builder.append(";");
        }
        if (marginLeft != null) {
            builder.append("margin-left:");
            marginLeft.build(builder);
            builder.append(";");
        }
        if (marginRight != null) {
            builder.append("margin-right:");
            marginRight.build(builder);
            builder.append(";");
        }

        builder.append("\">");
        content.build(builder);
        builder.append("</span>");
    }
}
