package com.suryaenergi.sdm.backendapi.email.content;

import com.suryaenergi.sdm.backendapi.email.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TableContent implements EmailContent {
    private final Row[] rows;
    private boolean fullWidth;
    private boolean borderless;
    private EdgeInsets padding;
    public TableContent(Row... rows) {
        this.rows = rows;
    }

    public void setPadding(EdgeInsets padding) {
        this.padding = padding;
    }

    public void setFullWidth(boolean fullWidth) {
        this.fullWidth = fullWidth;
    }

    public void setBorderless(boolean borderless) {
        this.borderless = borderless;
    }

    @Override
    public void build(StringBuilder builder) {
        if (fullWidth) {
            if (borderless) {
                builder.append("<table style=\"border-collapse:collapse;width:100%;white-space:nowrap;\">");
            } else {
                builder.append("<table style=\"border-collapse:collapse;width:100%;border:1px solid black; white-space:nowrap;\">");
            }
        } else {
            if (borderless) {
                builder.append("<table style=\"border-collapse:collapse;white-space:nowrap;\">");
            } else {
                builder.append("<table style=\"border-collapse:collapse;border:1px solid black;white-space:nowrap;\">");
            }
        }
        for (Row row : rows) {
            row.build(builder, this);
        }
        builder.append("</table>");
    }

    public static class Row {
        protected final List<Object> cells;

        public Row(Object... cells) {
            this.cells = new ArrayList<>(Arrays.asList(cells));
        }

        public List<Object> getCells() {
            return cells;
        }

        protected String cellToString(Object cell) {
            if (cell instanceof Cell) {
                cell = ((Cell) cell).content;
            }
            if (cell == null) {
                return "";
            }
            if (cell instanceof EmailContent) {
                StringBuilder builder = new StringBuilder();
                ((EmailContent) cell).build(builder);
                return builder.toString();
            }
            return cell.toString();
        }

        protected Alignment determineCellAlignment(String stringifiedCell) {
            // if number, right align
            // if text, left align
            // if date, center align
            try {
                Double.parseDouble(stringifiedCell);
                return Alignment.TOP_RIGHT;
            } catch (NumberFormatException e) {
                if (stringifiedCell.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    return Alignment.TOP_CENTER;
                }
                return Alignment.TOP_LEFT;
            }
        }

        public void build(StringBuilder builder, TableContent tableContent) {
            builder.append("<tr>");
            for (Object cell : cells) {
                if (!(cell instanceof Cell)) {
                    cell = new Cell(cell);
                }
                Cell c = (Cell) cell;
                String stringifiedCell = cellToString(cell);
                if (c.alignment == null) {
                    c.alignment = determineCellAlignment(stringifiedCell);
                }
                builder.append("<td");
                if (c.colspan > 1) {
                    builder.append(" colspan=\"").append(c.colspan).append("\"");
                }
                if (c.rowspan > 1) {
                    builder.append(" rowspan=\"").append(c.rowspan).append("\"");
                }
                builder.append(" style=\"");
                c.buildStyle(builder, tableContent);
                builder.append("\"");
                builder.append(">");
                builder.append(stringifiedCell);
                builder.append("</td>");
            }
            builder.append("</tr>");
        }

    }

    public static class Header extends Row {
        public static final Color DEFAULT_BACKGROUND_COLOR = Color.ORANGE;
        public Header(Object... cells) {
            super(cells);
        }

        @Override
        public void build(StringBuilder builder, TableContent tableContent) {
            builder.append("<tr>");
            for (Object cell : cells) {
                if (!(cell instanceof Cell)) {
                    cell = new Cell(cell);
                }
                Cell c = (Cell) cell;
                String stringifiedCell = cellToString(cell);
                if (c.alignment == null) {
                    c.alignment = Alignment.CENTER; // center align header
                }
                if (c.backgroundColor == null) {
                    c.backgroundColor = DEFAULT_BACKGROUND_COLOR;
                }
                builder.append("<th");
                if (c.colspan > 1) {
                    builder.append(" colspan=\"").append(c.colspan).append("\"");
                }
                if (c.rowspan > 1) {
                    builder.append(" rowspan=\"").append(c.rowspan).append("\"");
                }
                builder.append(" style=\"");
                c.buildStyle(builder, tableContent);
                builder.append("\"");
                builder.append(">");
                builder.append(stringifiedCell);
                builder.append("</th>");
            }
            builder.append("</tr>");
        }
    }

    public static class Cell {
        private final Object content;
        private int colspan;
        private int rowspan;
        private SizeUnit minWidth;
        private SizeUnit minHeight;
        private Alignment alignment;
        private Color backgroundColor;
        private BorderSide[] borderSides;
        private EdgeInsets padding;

        public Cell(Object content) {
            this(content, 1, 1);
        }

        public void setBackgroundColor(Color backgroundColor) {
            this.backgroundColor = backgroundColor;
        }

        public void setBorderSides(BorderSide[] borderSides) {
            this.borderSides = borderSides;
        }

        public void setMinWidth(SizeUnit minWidth) {
            this.minWidth = minWidth;
        }

        public void setMinHeight(SizeUnit minHeight) {
            this.minHeight = minHeight;
        }

        public void setColspan(int colspan) {
            this.colspan = colspan;
        }

        public void setRowspan(int rowspan) {
            this.rowspan = rowspan;
        }

        public void setAlignment(Alignment alignment) {
            this.alignment = alignment;
        }

        public void setPadding(EdgeInsets padding) {
            this.padding = padding;
        }

        public Cell(Object content, int colspan, int rowspan) {
            this.content = content;
            this.colspan = colspan;
            this.rowspan = rowspan;
        }

        public void buildStyle(StringBuilder builder, TableContent tableContent) {
            if (minWidth == null && minHeight == null && alignment == null && backgroundColor == null) {
                // border
                if (!tableContent.borderless) {
                    if (borderSides != null) {
                        for (BorderSide side : borderSides) {
                            builder.append("border-").append(side.name().toLowerCase()).append(":1px solid black;");
                        }
                    } else {
                        builder.append("border:1px solid black;");
                    }
                    if (padding != null) {
                        padding.buildStyle(builder);
                    } else if (tableContent.padding != null) {
//                        builder.append("padding:5px;");
                        tableContent.padding.buildStyle(builder);
                    }
                } else {
                    if (padding != null) {
                        padding.buildStyle(builder);
                    } else if (tableContent.padding != null) {
                        tableContent.padding.buildStyle(builder);
                    }
                    if (borderSides != null) {
                        for (BorderSide side : borderSides) {
                            builder.append("border-").append(side.name().toLowerCase()).append(":1px solid black;");
                        }
                    }
                }
                // padding
                return;
            }
            if (backgroundColor != null) {
                builder.append("background-color:").append(EmailUtil.colorToHex(backgroundColor)).append(";");
            }
            // border
//            builder.append("border:1px solid black;");
//            builder.append("padding:5px;");
            if (!tableContent.borderless) {
//                builder.append("border:1px solid black;");
//                builder.append("padding:5px;");
                if (borderSides != null) {
                    for (BorderSide side : borderSides) {
                        builder.append("border-").append(side.name().toLowerCase()).append(":1px solid black;");
                    }
                } else {
                    builder.append("border:1px solid black;");
                }
                if (padding != null) {
                    padding.buildStyle(builder);
                } else {
//                    builder.append("padding:5px;");
                    if (tableContent.padding != null) {
                        tableContent.padding.buildStyle(builder);
                    }
                }
            } else {
                if (padding != null) {
                    padding.buildStyle(builder);
                } else {
//                    builder.append("padding:5px;");
                    if (tableContent.padding != null) {
                        tableContent.padding.buildStyle(builder);
                    }
                }
                if (borderSides != null) {
                    for (BorderSide side : borderSides) {
                        builder.append("border-").append(side.name().toLowerCase()).append(":1px solid black;");
                    }
                }
            }
            if (minWidth != null) {
                builder.append("min-width:");
                minWidth.build(builder);
                builder.append(";");
            }
            if (minHeight != null) {
                builder.append("min-height:");
                minHeight.build(builder);
                builder.append(";");
            }
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
        }
    }
}
