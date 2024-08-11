package com.suryaenergi.sdm.backendapi.email;

public class EmailBuilder {
    private final EmailContent content;
    private final boolean useMeta;
    private final boolean useMediaQuery;

    public EmailBuilder(EmailContent content, boolean useMeta, boolean useMediaQuery) {
        this.content = content;
        this.useMeta = useMeta;
        this.useMediaQuery = useMediaQuery;
    }

    public String build() {
        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html>");
        builder.append("<html>");
        builder.append("<head>");
        //<meta http-equiv="Content-Type" content="text/html charset=UTF-8" />
        if (useMeta) {
            builder.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
        }
        if (useMediaQuery) {
            builder.append("<style>@media only screen and (max-width:768px) {.table_desktop_content {display: none !important;}} @media only screen and (min-width:769px) {.table_mobile_content {display: none !important;}}</style>");
        }
        // no need to add title
        builder.append("</head>");
        // using display: none; to hide the desktop content on mobile and vice versa
        // ENFORCES TO HAVE 2 STYLE TAGS IN CASE THE FIRST ONE GOT STRIPPED
        if (useMediaQuery) {
            builder.append("<body><style>@media only screen and (max-width:768px) {.table_desktop_content {display: none !important;}} @media only screen and (min-width:769px) {.table_mobile_content {display: none !important;}}</style>");
        }
        content.build(builder);
        builder.append("</body>");
        builder.append("</html>");
        return builder.toString();
    }
}
