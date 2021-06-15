package com.eazy.uibase.widget.treelist;

import java.util.ArrayList;
import java.util.List;

public class Node<B> {

    // bean
    public B bean;
    private int level;
    private boolean isExpand = false;

    //如果是叶子节点用list展开时用
    private List<Node> children = new ArrayList<>();

    private Node parent;

    private boolean isChecked;

    private boolean isLeaf;

    public Node(B bean, Node parent, int level, boolean isLeaf) {
        this.bean = bean;
        this.parent = parent;
        this.level = level;
        this.isLeaf = isLeaf;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    // 是否是根节点
    public boolean isRoot() {
        return parent == null;
    }

    // 判断父节点是否展开
    public boolean isParentExpand() {
        if (parent == null)
            return false;
        return parent.isExpand();
    }

    public B getBean() {
        return bean;
    }

    public void setBean(B bean) {
        this.bean = bean;
    }

    public int getLevel() {
        return level;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }

}
