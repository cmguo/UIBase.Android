package com.xhb.uibase.app.ui.main;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.niuedu.ListTree;
import com.xhb.uibase.app.R;
import com.xhb.uibase.demo.core.FragmentComponent;
import com.xhb.uibase.demo.core.Component;

import java.util.List;
import java.util.Map;

public class ComponentsViewModel extends ViewModel {

    private static final String TAG = "ComponentsViewModel";

    // TODO: Implement the ViewModel
    private Map<Integer, List<FragmentComponent>> controllers_ =
            FragmentComponent.collectComponents();

    ListTree buildTree(Context context) {
        ListTree tree = new ListTree();
        for (Map.Entry<Integer, List<FragmentComponent>> e : controllers_.entrySet()) {
            ListTree.TreeNode group = tree.addNode(null, getText(context, e.getKey()), R.layout.component_group);
            for (FragmentComponent c : e.getValue()) {
                tree.addNode(group, new ComponentInfo(context, c), R.layout.component_item);
            }
        }
        return tree;
    }

    protected static CharSequence getText(Context context, int id) {
        if (id == 0)
            return "";
        return context.getText(id);
    }
}