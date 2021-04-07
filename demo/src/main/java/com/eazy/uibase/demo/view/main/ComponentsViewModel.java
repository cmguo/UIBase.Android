package com.eazy.uibase.demo.view.main;

import android.content.Context;

import androidx.lifecycle.ViewModel;

//import com.niuedu.ListTree;
import com.eazy.uibase.demo.R;
import com.eazy.uibase.demo.core.Component;
import com.eazy.uibase.demo.core.ComponentInfo;
import com.eazy.uibase.demo.core.Components;

import java.util.List;
import java.util.Map;

public class ComponentsViewModel extends ViewModel {

    private static final String TAG = "ComponentsViewModel";

    // TODO: Implement the ViewModel
    private Map<Integer, List<Component>> components_ =
            Components.collectComponents();

//    ListTree buildTree(Context context) {
//        ListTree tree = new ListTree();
//        for (Map.Entry<Integer, List<Component>> e : components_.entrySet()) {
//            ListTree.TreeNode group = tree.addNode(null,
//                    ComponentInfo.getText(context, e.getKey()), R.layout.component_group);
//            for (Component c : e.getValue()) {
//                tree.addNode(group, new ComponentInfo(context, c), R.layout.component_item);
//            }
//        }
//        return tree;
//    }
}