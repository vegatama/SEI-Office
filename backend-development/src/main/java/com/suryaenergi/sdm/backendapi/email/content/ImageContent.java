package com.suryaenergi.sdm.backendapi.email.content;

import com.suryaenergi.sdm.backendapi.email.EmailContent;

public abstract class ImageContent implements EmailContent {
    public static ImageContent networkImage(String url, String alt) {
        return new NetworkImage(url, alt);
    }

    public static ImageContent attachmentImage(String path, String alt) {
        return new AttachmentImage(path, alt);
    }

    public static ImageContent base64Image(String base64, String alt) {
        return new Base64Image(base64, alt);
    }

    private static class NetworkImage extends ImageContent {
        private final String url;
        private final String alt;

        public NetworkImage(String url, String alt) {
            this.url = url;
            this.alt = alt;
        }

        @Override
        public void build(StringBuilder builder) {
            builder.append("<img style=\"width:100%;height:100%\" src=\"").append(url).append("\" alt=\"").append(alt).append("\">");
        }
    }

    private static class AttachmentImage extends ImageContent {
        private final String path;
        private final String alt;

        public AttachmentImage(String path, String alt) {
            this.path = path;
            this.alt = alt;
        }

        @Override
        public void build(StringBuilder builder) {
            builder.append("<img style=\"width:100%;height:100%\" src=\"cid:").append(path).append("\" alt=\"").append(alt).append("\">");
        }
    }

    private static class Base64Image extends ImageContent {
        private final String base64;
        private final String alt;

        public Base64Image(String base64, String alt) {
            this.base64 = base64;
            this.alt = alt;
        }

        @Override
        public void build(StringBuilder builder) {
            builder.append("<img style=\"width:100%;\" src=\"data:image/png;base64,").append(base64).append("\" alt=\"").append(alt).append("\">");
        }
    }
}
