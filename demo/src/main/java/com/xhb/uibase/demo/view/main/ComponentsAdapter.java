package com.xhb.uibase.demo.view.main;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.niuedu.ListTree;
import com.niuedu.ListTreeAdapter;
import com.xhb.uibase.demo.R;
import com.xhb.uibase.demo.core.ComponentInfo;

/**
 * 为RecyclerView提供数据
 */
public class ComponentsAdapter extends
        ListTreeAdapter<ComponentsAdapter.BaseViewHolder> {

    @FunctionalInterface
    interface OnItemClickListener<T> {
        void onClick(RecyclerView recyclerView, View view, T data);
    }

    OnItemClickListener<ComponentInfo> itemClickListener;
    //行上弹出菜单的侦听器
    private PopupMenu.OnMenuItemClickListener itemMenuClickListener;
    //记录弹出菜单是在哪个行上出现的
    private ListTree.TreeNode currentNode;

    //构造方法
    public ComponentsAdapter(ListTree tree, OnItemClickListener<ComponentInfo> clickListener,
                             PopupMenu.OnMenuItemClickListener listener) {
        super(tree);
        this.itemClickListener = clickListener;
        this.itemMenuClickListener = listener;
    }

    public ListTree.TreeNode getCurrentNode() {
        return currentNode;
    }

    @Override
    protected BaseViewHolder onCreateNodeView(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        //创建不同的行View
        if (viewType == R.layout.component_group) {
            //注意！此处有一个不同！最后一个参数必须传true！
            View view = inflater.inflate(viewType, parent, true);
            //用不同的ViewHolder包装
            return new GroupViewHolder(view);
        } else if (viewType == R.layout.component_item) {
            //注意！此处有一个不同！最后一个参数必须传true！
            View view = inflater.inflate(viewType, parent, true);
            //用不同的ViewHolder包装
            return new ComponentViewHolder(view);
        } else {
            return null;
        }
    }

    @Override
    protected void onBindNodeViewHolder(BaseViewHolder holder, int position) {
        View view = holder.itemView;
        //get node at the position
        ListTree.TreeNode node = tree.getNodeByPlaneIndex(position);

        if (node.getLayoutResId() == R.layout.component_group) {
            //group node
            String title = (String) node.getData();

            GroupViewHolder gvh = (GroupViewHolder) holder;
            gvh.textViewTitle.setText(title);
        } else if (node.getLayoutResId() == R.layout.component_item) {
            //child node
            ComponentInfo info = (ComponentInfo) node.getData();

            ComponentViewHolder cvh = (ComponentViewHolder) holder;
            cvh.imageViewHead.setImageDrawable(info.getIcon());
            cvh.textViewTitle.setText(info.getTitle());
            cvh.textViewAuthor.setText("[" + info.getAuthor() + "]");
            cvh.textViewStars.setText(String.valueOf(info.getStars()));
            cvh.textViewDetail.setText(info.getDetail());
        }
    }

    //组行和联系人行的Holder基类
    class BaseViewHolder extends ListTreeAdapter.ListTreeViewHolder {
        public BaseViewHolder(View itemView) {
            super(itemView);
        }
    }

    //将ViewHolder声明为Adapter的内部类，反正外面也用不到
    class GroupViewHolder extends BaseViewHolder {

        TextView textViewTitle;

        public GroupViewHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.title);
        }
    }

    class ComponentViewHolder extends BaseViewHolder {
        ImageView imageViewHead;
        TextView textViewTitle;
        TextView textViewStars;
        TextView textViewAuthor;
        TextView textViewDetail;

        public ComponentViewHolder(View itemView) {
            super(itemView);

            imageViewHead = itemView.findViewById(R.id.icon);
            textViewTitle = itemView.findViewById(R.id.title);
            textViewStars = itemView.findViewById(R.id.stars);
            textViewAuthor = itemView.findViewById(R.id.author);
            textViewDetail = itemView.findViewById(R.id.detail);

            //点了PopMenu控件，弹出PopMenu
            textViewAuthor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int nodePlaneIndex = getAdapterPosition();
                    ListTree.TreeNode node = tree.getNodeByPlaneIndex(nodePlaneIndex);
                    currentNode = node;
                    PopupMenu popup = new PopupMenu(v.getContext(), v);
                    popup.setOnMenuItemClickListener(itemMenuClickListener);
                    MenuInflater inflater = popup.getMenuInflater();
                    //inflater.inflate(R.menu.menu_item, popup.getMenu());
                    popup.show();
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)  {
                    ListTree.TreeNode node = tree.getNodeByPlaneIndex(getAdapterPosition());
                    itemClickListener.onClick(null, v, (ComponentInfo) node.getData());
                }
            });

        }
    }
}