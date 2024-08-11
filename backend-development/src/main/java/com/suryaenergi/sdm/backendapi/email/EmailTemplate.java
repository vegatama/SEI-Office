package com.suryaenergi.sdm.backendapi.email;

import com.suryaenergi.sdm.backendapi.email.content.*;
import lombok.Data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class EmailTemplate {
    @Data
    public static class Entry {
        private final String key;
        private final Object value;

        public Entry(String key, Object value) {
            this.key = key;
            this.value = value;
        }
    }
    public abstract EmailContent build();

    protected SpanContent span(String text) {
        return new SpanContent(text);
    }

    protected FormFieldsContent formFields(String key, Object value) {
        return new FormFieldsContent(key, value);
    }

    protected FormFieldsContent formFields(Entry...entries) {
        Map<String, Object> map = new HashMap<>();
        for (Entry entry : entries) {
            map.put(entry.key, entry.value);
        }
        return new FormFieldsContent(map);
    }

    protected GroupContent group(EmailContent...children) {
        return new GroupContent(Arrays.asList(children));
    }

    protected EmailContent br() {
        return new EmailContent() {
            @Override
            public void build(StringBuilder builder) {
                builder.append("<br>");
            }
        };
    }

    protected Entry entry(String key, Object value) {
        return new Entry(key, value);
    }

    protected TableContent.Row row(Object...cells) {
        return new TableContent.Row(cells);
    }

    protected TableContent.Cell cell(Object content) {
        return new TableContent.Cell(content);
    }

    protected TableContent.Cell spacerCell() {
        return new TableContent.Cell(null) {
            @Override
            public void buildStyle(StringBuilder builder, TableContent tableContent) {
                super.buildStyle(builder, tableContent);
                builder.append("width:100%;");
            }
        };
    }

    protected TableContent.Cell spacerCell(SizeUnit width) {
        return new TableContent.Cell(null) {
            @Override
            public void buildStyle(StringBuilder builder, TableContent tableContent) {
                super.buildStyle(builder, tableContent);
                builder.append("width:");
                width.build(builder);
                builder.append(";");
            }
        };
    }

    protected TableContent.Header header(Object...cells) {
        return new TableContent.Header(cells);
    }

    protected TableContent table(TableContent.Row...rows) {
        return new TableContent(rows);
    }

    protected ButtonContent btnPrimary(String text, String url) {
        return new ButtonContent(text, url, ButtonContent.VARIANT_PRIMARY);
    }

    protected ButtonContent btnSecondary(String text, String url) {
        return new ButtonContent(text, url, ButtonContent.VARIANT_SECONDARY);
    }

    protected ButtonContent btnTertiary(String text, String url) {
        return new ButtonContent(text, url, ButtonContent.VARIANT_TERTIARY);
    }

    protected LinkContent link(String url, String text) {
        return new LinkContent(url, text);
    }

    protected ParagraphContent p(String text) {
        return new ParagraphContent(text);
    }

    protected HeadingContent h1(String text) {
        return new HeadingContent(text, 1);
    }

    protected HeadingContent h2(String text) {
        return new HeadingContent(text, 2);
    }

    protected HeadingContent h3(String text) {
        return new HeadingContent(text, 3);
    }

    protected HeadingContent h4(String text) {
        return new HeadingContent(text, 4);
    }

    protected ImageContent imgBase64(String base64, String alt) {
        return ImageContent.base64Image(base64, alt);
    }

    protected ImageContent imgNetwork(String url, String alt) {
        return ImageContent.networkImage(url, alt);
    }

    protected EmailContent spacer(SizeUnit size) {
        return new EmailContent() {
            @Override
            public void build(StringBuilder builder) {
                // do not use div here, it will break the email layout
                builder.append("<table width=\"100%\" height=\"");
                size.build(builder);
                builder.append("\"><tr><td></td></tr></table>");
            }
        };
    }

    protected EmailContent text(String text) {
        return new EmailContent() {
            @Override
            public void build(StringBuilder builder) {
                builder.append(text);
            }
        };
    }

    protected ParagraphContent p(EmailContent...content) {
        return new ParagraphContent(group(content));
    }

    protected SpanContent span(EmailContent...content) {
        return new SpanContent(group(content));
    }

    protected RowContent row(ChildData...data) {
        return new RowContent(Arrays.asList(data));
    }

    protected StyleContent style(EmailContent content) {
        return new StyleContent(content);
    }

    protected ColumnContent col(ChildData...children) {
        return new ColumnContent(Arrays.asList(children));
    }

    protected ContainerContent container(EmailContent content) {
        return new ContainerContent(content);
    }

    protected SizeUnit px(double value) {
        return SizeUnit.px(value);
    }

    protected SizeUnit percent(double value) {
        return SizeUnit.percent(value);
    }

    protected SizeUnit em(double value) {
        return SizeUnit.em(value);
    }

    protected ChildData child() {
        return new ChildData(null);
    }

    protected ChildData child(EmailContent content) {
        return new ChildData(content);
    }

    protected EmailContent mobileOnly(EmailContent content) {
        return new MobileContent(content);
    }

    protected EmailContent desktopOnly(EmailContent content) {
        return new DesktopContent(content);
    }

    protected static final SizeUnit zero = SizeUnit.px(0);
}
