package com.suryaenergi.sdm.backendapi.email.content;

import java.util.List;

public class RowContent extends AbstractTableContent {
    public RowContent(List<ChildData> data) {
        super(data);
    }
    @Override
    public void build(StringBuilder builder) {
        buildTableHeader(builder);
        builder.append("<tr>");
        for (ChildData child : children) {
            buildTableItem(builder, child);
        }
        builder.append("</tr>");
        builder.append("</table>");
    }


}
