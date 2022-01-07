package fr.pearl.core.common.configuration.comment;

public class CommentTree {

    public static CommentNode getNode(CommentNode node, String path, int indent) {
        CommentNode parent = node == null ? null : node.getParent();
        int lastIndent = node == null ? 0 : node.getIndent();
        if (indent > lastIndent) {
            return new CommentNode(node, path, indent);
        } else if (indent < lastIndent) {
            if (parent == null) return new CommentNode(null, path, indent);
            int loops = (lastIndent - indent) / 2;
            CommentNode nearestParent = parent;
            while (loops != 0) {
                if (nearestParent == null) {
                    break;
                }
                nearestParent = nearestParent.getParent();
                loops--;
            }
            return new CommentNode(nearestParent, path, indent);
        }

        return new CommentNode(parent, path, indent);
    }
}
