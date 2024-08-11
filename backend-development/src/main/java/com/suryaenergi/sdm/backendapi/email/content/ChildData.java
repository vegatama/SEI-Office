package com.suryaenergi.sdm.backendapi.email.content;

import com.suryaenergi.sdm.backendapi.email.Alignment;
import com.suryaenergi.sdm.backendapi.email.EmailContent;
import com.suryaenergi.sdm.backendapi.email.SizeUnit;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class ChildData {
    @Nullable
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    @Nullable
    private SizeUnit width;
    @Nullable
    private SizeUnit height;
    @Nullable
    private SizeUnit paddingTop;
    @Nullable
    private SizeUnit paddingBottom;
    @Nullable
    private SizeUnit paddingLeft;
    @Nullable
    private SizeUnit paddingRight;
    @Nullable
    private Alignment alignment = Alignment.TOP_LEFT;
    @Nullable
    private Color backgroundColor;

    @Getter
    private final EmailContent content;


    public ChildData(EmailContent content) {
        this.content = content;
    }

    @Nullable
    public SizeUnit getWidth() {
        return width;
    }

    @Nullable
    public SizeUnit getHeight() {
        return height;
    }

    @Nullable
    public SizeUnit getPaddingTop() {
        return paddingTop;
    }

    @Nullable
    public SizeUnit getPaddingBottom() {
        return paddingBottom;
    }

    @Nullable
    public SizeUnit getPaddingLeft() {
        return paddingLeft;
    }

    @Nullable
    public SizeUnit getPaddingRight() {
        return paddingRight;
    }

    @Nullable
    public Alignment getAlignment() {
        return alignment;
    }

    public ChildData backgroundColor(Color color) {
        this.backgroundColor = color;
        return this;
    }

    public ChildData alignment(Alignment alignment) {
        this.alignment = alignment;
        return this;
    }

    public ChildData padding(SizeUnit padding) {
        this.paddingTop = padding;
        this.paddingBottom = padding;
        this.paddingLeft = padding;
        this.paddingRight = padding;
        return this;
    }

    public ChildData padding(SizeUnit top, SizeUnit right, SizeUnit bottom, SizeUnit left) {
        this.paddingTop = top;
        this.paddingRight = right;
        this.paddingBottom = bottom;
        this.paddingLeft = left;
        return this;
    }

    public ChildData padding(SizeUnit horizontal, SizeUnit vertical) {
        this.paddingTop = vertical;
        this.paddingBottom = vertical;
        this.paddingLeft = horizontal;
        this.paddingRight = horizontal;
        return this;
    }

    public ChildData width(SizeUnit width) {
        this.width = width;
        return this;
    }

    public ChildData height(SizeUnit height) {
        this.height = height;
        return this;
    }

}
