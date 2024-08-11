package com.suryaenergi.sdm.backendapi.email;

public class EdgeInsets {
    private SizeUnit top;
    private SizeUnit right;
    private SizeUnit bottom;
    private SizeUnit left;

    public static EdgeInsets padAll(SizeUnit all) {
        return new EdgeInsets().thenAll(all);
    }

    public static EdgeInsets padHorizontal(SizeUnit horizontal) {
        return new EdgeInsets().thenHorizontal(horizontal);
    }

    public static EdgeInsets padVertical(SizeUnit vertical) {
        return new EdgeInsets().thenVertical(vertical);
    }

    public static EdgeInsets padTop(SizeUnit top) {
        return new EdgeInsets().thenTop(top);
    }

    public static EdgeInsets padRight(SizeUnit right) {
        return new EdgeInsets().thenRight(right);
    }

    public static EdgeInsets padBottom(SizeUnit bottom) {
        return new EdgeInsets().thenBottom(bottom);
    }

    public static EdgeInsets padLeft(SizeUnit left) {
        return new EdgeInsets().thenLeft(left);
    }

    public EdgeInsets() {}

    public EdgeInsets thenAll(SizeUnit all) {
        this.top = all;
        this.right = all;
        this.bottom = all;
        this.left = all;
        return this;
    }

    public EdgeInsets thenHorizontal(SizeUnit horizontal) {
        this.right = horizontal;
        this.left = horizontal;
        return this;
    }

    public EdgeInsets thenVertical(SizeUnit vertical) {
        this.top = vertical;
        this.bottom = vertical;
        return this;
    }

    public EdgeInsets thenTop(SizeUnit top) {
        this.top = top;
        return this;
    }

    public EdgeInsets thenRight(SizeUnit right) {
        this.right = right;
        return this;
    }

    public EdgeInsets thenBottom(SizeUnit bottom) {
        this.bottom = bottom;
        return this;
    }

    public EdgeInsets thenLeft(SizeUnit left) {
        this.left = left;
        return this;
    }

    public void buildStyle(StringBuilder builder) {
        if (top != null) {
            builder.append("padding-top:");
            top.build(builder);
            builder.append(";");
        }
        if (right != null) {
            builder.append("padding-right:");
            right.build(builder);
            builder.append(";");
        }
        if (bottom != null) {
            builder.append("padding-bottom:");
            bottom.build(builder);
            builder.append(";");
        }
        if (left != null) {
            builder.append("padding-left:");
            left.build(builder);
            builder.append(";");
        }
    }
}
