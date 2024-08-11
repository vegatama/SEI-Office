package com.suryaenergi.sdm.backendapi.email.content;

import java.util.List;

public class ColumnContent extends AbstractTableContent {
    public ColumnContent(List<ChildData> children) {
        super(children);
    }

    @Override
    public void build(StringBuilder builder) {
        buildTableHeader(builder);
        for (ChildData child : children) {
            builder.append("<tr>");
            buildTableItem(builder, child);
            builder.append("</tr>");
        }
        builder.append("</table>");
    }
}
