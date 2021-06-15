package com.eazy.uibase.widget.treelist;

import java.util.ArrayList;
import java.util.List;

class TreeNodeHelper {
    enum CascadeType {
        ParentChild, // 父子级联
        RootControlContent, //根节点选中控制数据内容
        ParentControlContent, //父节点选中控制数据内容
    }

    enum SelectedType {
        LeafSingleChoice,
        LeafMultipleChoice,
        SingleChoice,
        MultipleChoice,
        BrothersSingleChoice,//兄弟节点单选
        BrothersMultipleChoice // //兄弟节点多选
    }

    enum ExpandType {
        ChildNode,
        FirstChildNode,
        Descendant,//所有后代
        FirstDescendant
    }

    enum DefaultSelectedType {
        Normal,// 选中
        FirstSelected,//父子级联中用
    }

    private SelectedType selectedType = SelectedType.SingleChoice;
    private DefaultSelectedType defaultSelectedType = DefaultSelectedType.Normal;
    private ExpandType expandType = ExpandType.Descendant;

    public SelectedType getSelectedType() {
        return selectedType;
    }

    public TreeNodeHelper setSelectedType(SelectedType selectedType) {
        this.selectedType = selectedType;
        return this;
    }

    public DefaultSelectedType getDefaultSelectedType() {
        return defaultSelectedType;
    }

    public TreeNodeHelper setDefaultSelectedType(DefaultSelectedType defaultSelectedType) {
        this.defaultSelectedType = defaultSelectedType;
        return this;
    }

    public ExpandType getExpandType() {
        return expandType;
    }

    public TreeNodeHelper setExpandType(ExpandType expandType) {
        this.expandType = expandType;
        return this;
    }

    public CascadeType getCascadeType() {
        return cascadeType;
    }

    public TreeNodeHelper setCascadeType(CascadeType cascadeType) {
        this.cascadeType = cascadeType;
        return this;
    }

    private CascadeType cascadeType = CascadeType.ParentChild;

    private List<Node> treeNode = new ArrayList<>(); // 需要显示的数据结构
    private List<Node> originNode;// 保存原始数据

    /**
     * 当前只实现root级联
     **/
    public void setOriginNode(List<Node> originNode) {
        // 父子级联重构内容
        // 重构展开状态
        treeNode = new ArrayList<>();
        if (originNode == null || originNode.isEmpty())
            return;
        this.originNode = originNode;
        if (cascadeType == CascadeType.RootControlContent) {

        }
    }

    private void changeCascadeData(List<Node> nodes) {
        treeNode.clear();
        boolean findSelect = false;
        for (Node node : nodes) {


        }
    }


}
