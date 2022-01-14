package fr.pearl.core.common.configuration.comment;

public class CommentNode {
    private final CommentNode parent;
    private final int indent;
    private final String fullPath;
    private final String indentSeparator;

    public CommentNode(CommentNode parent, String path, int indent) {
        this.parent = parent;
        this.indent = indent;
        this.fullPath = parent == null ? path : parent.fullPath + "." + path;
        this.indentSeparator = parent == null ? "" : parent.indentSeparator + "  ";
    }

    public String getIndentSeparator() {
        return indentSeparator;
    }

    public int getIndent() {
        return indent;
    }

    public CommentNode getParent() {
        return parent;
    }

    public String getFullPath() {
        return fullPath;
    }
}
