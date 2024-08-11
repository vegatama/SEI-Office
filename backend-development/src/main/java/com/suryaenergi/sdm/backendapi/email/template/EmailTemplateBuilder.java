package com.suryaenergi.sdm.backendapi.email.template;

import com.suryaenergi.sdm.backendapi.email.*;
import com.suryaenergi.sdm.backendapi.email.content.ChildData;
import com.suryaenergi.sdm.backendapi.email.content.TableContent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EmailTemplateBuilder extends AbstractEmailTemplate {
    public static EmailTemplateBuilder create(String subject) {
        return new EmailTemplateBuilder(subject, false);
    }
    public static EmailTemplateBuilder createPDFCompatible(String subject) {
        return new EmailTemplateBuilder(subject, true);
    }
    private final List<TableContent.Row> rows = new ArrayList<>();
    private final List<EmailContent> contents = new ArrayList<>();
    private final List<EmailContent> buttons = new ArrayList<>();
    private final List<String> texts = new ArrayList<>();
    private final List<Entry> entries = new ArrayList<>();
    private boolean fullWidthTable = false;
    private boolean borderlessTable = false;
    private EdgeInsets tablePadding;

    public EmailTemplateBuilder(String subject, boolean pdfCompatible) {
        super(subject, pdfCompatible);
    }

    public EmailTemplateBuilder append(String content) {
        finalizeFormFields();
        finalizeButtons();
        finalizeRow();
        texts.add(content);
        return this;
    }

    protected void finalizeRow() {
        if (rows.isEmpty()) {
            return;
        }
        TableContent table = table(rows.toArray(new TableContent.Row[0]));
        table.setFullWidth(fullWidthTable);
        table.setBorderless(borderlessTable);
        table.setPadding(tablePadding);
        contents.add(table);
        fullWidthTable = false;
        borderlessTable = false;
        rows.clear();
    }

    public EmailTemplateBuilder appendRow(Object...cells) {
        finalizeTexts();
        finalizeFormFields();
        rows.add(row(cells));
        return this;
    }

    public EmailTemplateBuilder newTable() {
        finalizeRow();
        finalizeTexts();
        finalizeFormFields();
        return this;
    }

    public EmailTemplateBuilder newFullWidthTable() {
        finalizeRow();
        finalizeTexts();
        finalizeFormFields();
        fullWidthTable = true;
        return this;
    }

    public EmailTemplateBuilder newFullWidthTable(EdgeInsets padding) {
        finalizeRow();
        finalizeTexts();
        finalizeFormFields();
        fullWidthTable = true;
        tablePadding = padding;
        return this;
    }

    public EmailTemplateBuilder newBorderlessTable() {
        finalizeRow();
        finalizeTexts();
        finalizeFormFields();
        borderlessTable = true;
        return this;
    }

    public EmailTemplateBuilder newBorderlessTable(EdgeInsets padding) {
        finalizeRow();
        finalizeTexts();
        finalizeFormFields();
        borderlessTable = true;
        tablePadding = padding;
        return this;
    }

    public EmailTemplateBuilder newBorderlessFullWidthTable() {
        finalizeRow();
        finalizeTexts();
        finalizeFormFields();
        fullWidthTable = true;
        borderlessTable = true;
        return this;
    }

    public EmailTemplateBuilder newBorderlessFullWidthTable(EdgeInsets padding) {
        finalizeRow();
        finalizeTexts();
        finalizeFormFields();
        fullWidthTable = true;
        borderlessTable = true;
        tablePadding = padding;
        return this;
    }

    public EmailTemplateBuilder appendCell(Object content) {
        // find the last row and append the cell to the last row
        if (rows.isEmpty()) {
            throw new IllegalStateException("No row to append cell");
        }
        TableContent.Row row = rows.get(rows.size() - 1);
        row.getCells().add(cell(content));
        return this;
    }

    public EmailTemplateBuilder appendSpacerCell() {
        // find the last row and append the spacer cell to the last row
        if (rows.isEmpty()) {
            throw new IllegalStateException("No row to append spacer cell");
        }
        TableContent.Row row = rows.get(rows.size() - 1);
        row.getCells().add(spacerCell());
        return this;
    }

    public EmailTemplateBuilder appendSpacerCell(SizeUnit width) {
        // find the last row and append the spacer cell to the last row
        if (rows.isEmpty()) {
            throw new IllegalStateException("No row to append spacer cell");
        }
        TableContent.Row row = rows.get(rows.size() - 1);
        row.getCells().add(spacerCell(width));
        return this;
    }

    public EmailTemplateBuilder withPadding(EdgeInsets padding) {
        // find the last row and set the padding on the last cell
        if (rows.isEmpty()) {
            throw new IllegalStateException("No row to set padding");
        }
        TableContent.Row row = rows.get(rows.size() - 1);
        List<Object> cells = row.getCells();
        if (cells.isEmpty()) {
            throw new IllegalStateException("No cell to set padding");
        }
        Object cell = cells.get(cells.size() - 1);
        if (!(cell instanceof TableContent.Cell)) {
            cell = new TableContent.Cell(cell);
            cells.set(cells.size() - 1, cell);
        }
        TableContent.Cell c = (TableContent.Cell) cell;
        c.setPadding(padding);
        return this;
    }

    public EmailTemplateBuilder withBorderSides(BorderSide...borderSides) {
        // find the last row and set the border sides on the last cell
        if (rows.isEmpty()) {
            throw new IllegalStateException("No row to set border sides");
        }
        TableContent.Row row = rows.get(rows.size() - 1);
        List<Object> cells = row.getCells();
        if (cells.isEmpty()) {
            throw new IllegalStateException("No cell to set border sides");
        }
        Object cell = cells.get(cells.size() - 1);
        if (!(cell instanceof TableContent.Cell)) {
            cell = new TableContent.Cell(cell);
            cells.set(cells.size() - 1, cell);
        }
        TableContent.Cell c = (TableContent.Cell) cell;
        c.setBorderSides(borderSides);
        return this;
    }

    public EmailTemplateBuilder withRowspan(int rowspan) {
        // find the last row and set the rowspan on the last cell
        if (rows.isEmpty()) {
            throw new IllegalStateException("No row to set rowspan");
        }
        TableContent.Row row = rows.get(rows.size() - 1);
        List<Object> cells = row.getCells();
        if (cells.isEmpty()) {
            throw new IllegalStateException("No cell to set rowspan");
        }
        Object cell = cells.get(cells.size() - 1);
        if (!(cell instanceof TableContent.Cell)) {
            cell = new TableContent.Cell(cell);
            cells.set(cells.size() - 1, cell);
        }
        TableContent.Cell c = (TableContent.Cell) cell;
        c.setRowspan(rowspan);
        return this;
    }

    public EmailTemplateBuilder withColspan(int colspan) {
        // find the last row and set the colspan on the last cell
        if (rows.isEmpty()) {
            throw new IllegalStateException("No row to set colspan");
        }
        TableContent.Row row = rows.get(rows.size() - 1);
        List<Object> cells = row.getCells();
        if (cells.isEmpty()) {
            throw new IllegalStateException("No cell to set colspan");
        }
        Object cell = cells.get(cells.size() - 1);
        if (!(cell instanceof TableContent.Cell)) {
            cell = new TableContent.Cell(cell);
            cells.set(cells.size() - 1, cell);
        }
        TableContent.Cell c = (TableContent.Cell) cell;
        c.setColspan(colspan);
        return this;
    }

    public EmailTemplateBuilder withCellAlignment(Alignment alignment) {
        // find the last row and set the alignment on the last cell
        if (rows.isEmpty()) {
            throw new IllegalStateException("No row to set alignment");
        }
        TableContent.Row row = rows.get(rows.size() - 1);
        List<Object> cells = row.getCells();
        if (cells.isEmpty()) {
            throw new IllegalStateException("No cell to set alignment");
        }
        Object cell = cells.get(cells.size() - 1);
        if (!(cell instanceof TableContent.Cell)) {
            cell = new TableContent.Cell(cell);
            cells.set(cells.size() - 1, cell);
        }
        TableContent.Cell c = (TableContent.Cell) cell;
        c.setAlignment(alignment);
        return this;
    }

    public EmailTemplateBuilder withBackgroundColor(Color color) {
        // find the last row and set the background color on the last cell
        if (rows.isEmpty()) {
            throw new IllegalStateException("No row to set background color");
        }
        TableContent.Row row = rows.get(rows.size() - 1);
        List<Object> cells = row.getCells();
        if (cells.isEmpty()) {
            throw new IllegalStateException("No cell to set background color");
        }
        Object cell = cells.get(cells.size() - 1);
        if (!(cell instanceof TableContent.Cell)) {
            cell = new TableContent.Cell(cell);
            cells.set(cells.size() - 1, cell);
        }
        TableContent.Cell c = (TableContent.Cell) cell;
        c.setBackgroundColor(color);
        return this;
    }

    public EmailTemplateBuilder withMinColWidth(SizeUnit width) {
        // find the last row and set the min width on the last cell
        if (rows.isEmpty()) {
            throw new IllegalStateException("No row to set min width");
        }
        TableContent.Row row = rows.get(rows.size() - 1);
        List<Object> cells = row.getCells();
        if (cells.isEmpty()) {
            throw new IllegalStateException("No cell to set min width");
        }
        Object cell = cells.get(cells.size() - 1);
        if (!(cell instanceof TableContent.Cell)) {
            cell = new TableContent.Cell(cell);
            cells.set(cells.size() - 1, cell);
        }
        TableContent.Cell c = (TableContent.Cell) cell;
        c.setMinWidth(width);
        return this;
    }

    public EmailTemplateBuilder withMinRowHeight(SizeUnit height) {
        // find the last row and set the min height on the last cell
        if (rows.isEmpty()) {
            throw new IllegalStateException("No row to set min height");
        }
        TableContent.Row row = rows.get(rows.size() - 1);
        List<Object> cells = row.getCells();
        if (cells.isEmpty()) {
            throw new IllegalStateException("No cell to set min height");
        }
        Object cell = cells.get(cells.size() - 1);
        if (!(cell instanceof TableContent.Cell)) {
            cell = new TableContent.Cell(cell);
            cells.set(cells.size() - 1, cell);
        }
        TableContent.Cell c = (TableContent.Cell) cell;
        c.setMinHeight(height);
        return this;
    }

    public EmailTemplateBuilder appendHeader(Object...cells) {
        finalizeTexts();
        finalizeFormFields();
        finalizeButtons();
        rows.add(header(cells));
        return this;
    }

    protected void finalizeTexts() {
        if (texts.isEmpty()) {
            return;
        }
        List<EmailContent> textContents = new ArrayList<>();
        for (int i = 0; i < texts.size(); i++) {
            String text = texts.get(i);
            if (i != 0) {
                textContents.add(br());
            }
            textContents.add(text(text));
        }
        contents.add(p(group(textContents.toArray(new EmailContent[0]))));
        texts.clear();
    }

    protected void finalizeFormFields() {
        if (entries.isEmpty()) {
            return;
        }
        contents.add(formFields(entries.toArray(new Entry[0])));
        entries.clear();
    }

    public EmailTemplateBuilder appendBtnPrimary(String text, String url) {
        finalizeRow();
        finalizeTexts();
        finalizeFormFields();
        buttons.add(btnPrimary(text, url));
        return this;
    }

    public EmailTemplateBuilder appendBtnSecondary(String text, String url) {
        finalizeRow();
        finalizeTexts();
        finalizeFormFields();
        buttons.add(btnSecondary(text, url));
        return this;
    }

    public EmailTemplateBuilder appendBtnTertiary(String text, String url) {
        finalizeRow();
        finalizeTexts();
        finalizeFormFields();
        buttons.add(btnTertiary(text, url));
        return this;
    }

    public EmailTemplateBuilder appendEntry(String key, Object value) {
        finalizeRow();
        finalizeTexts();
        finalizeButtons();
        entries.add(new Entry(key, value));
        return this;
    }

    public EmailTemplateBuilder appendNetworkImage(String url, String alt) {
        finalizeRow();
        finalizeTexts();
        finalizeFormFields();
        finalizeButtons();
        contents.add(imgNetwork(url, alt));
        return this;
    }

    public EmailTemplateBuilder appendSpacer(SizeUnit size) {
        finalizeRow();
        finalizeTexts();
        finalizeFormFields();
        finalizeButtons();
        contents.add(spacer(size));
        return this;
    }

    protected void finalizeButtons() {
        if (buttons.isEmpty()) {
            return;
        }
        List<ChildData> buttonData = new ArrayList<>(buttons.size());
        for (EmailContent button : buttons) {
            buttonData.add(child(button.style().marginRight(px(10)).marginBottom(px(10))));
        }
        buttons.clear();
        contents.add(row(buttonData.toArray(new ChildData[0])));
    }

    @Override
    public EmailContent buildContent() {
        finalizeRow();
        finalizeTexts();
        finalizeFormFields();
        finalizeButtons();
        List<ChildData> childData = new ArrayList<>();
        for (EmailContent content : contents) {
            childData.add(new ChildData(content));
        }
        return col(childData.toArray(new ChildData[0]));
    }

    public String generate() {
        if (isPdfCompatible()) {
            return generateForPdf();
        }
        EmailBuilder emailBuilder = new EmailBuilder(build(), true, true);
        return emailBuilder.build();
    }


    private String generateForPdf() {
        EmailBuilder emailBuilder = new EmailBuilder(build(), false, false);
        return emailBuilder.build();
    }

}
