# 列表视图

提供下列功能：
- 数据封装：RecyclerList，树形数据：RecyclerTreeList，支持数据编辑、变化通知
- 数据视图绑定机制 ItemBinding；单一视图类型：UnitItemBinding，多样视图类型 BaseItemBinding
- 通用列表适配器：支持托管点击回调、空视图、数据更新
- 各种装饰器：填充，分割线，支持快速扩展，支持树形数据
- 简单的 RecylcerView 封装： ZListView，支持图标、标题、详细模式，扩展操作交互

# 数据封装 RecyclerList
``` kotlin
val recyclerList (recylerView.adpater as RecylerViewAdpater).getItems()
// 操作数据，列表会自动更新
recyclerList.clear()
recyclerList.add(Item())
recyclerList.move(1, 0) // 移动 Item 1 到位置 0
```

# 树形数据 RecyclerTreeList
``` kotlin
val treeList = object : RecyclerTreeList<T>() {
    fun getChildren(T item) : Iterator<T> {
        return item.children.iterator()
    }
}
// ...
// 操作数据，列表会自动更新
val item: T
treeList.add(item)
treeList.remove(0) // 删除第一的子树
treeList.get(1).move(1, 0) // 移动第二个子树的子节点，第二个子节点移动到所在树的最前面
```

# 单一视图类型 UnitItemBinding
``` xml
<?xml version="1.0" encoding="utf-8"?>
<layout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">
    <data>
        <variable
            name="data"
            type="String" />
        <variable
            name="viewModel"
            type="Object" />
    </data>
    <TextView
        android:id="@+id/title"
        android:paddingStart="5dp"
        android:textSize="20dp"
        android:textFontWeight="2"
        android:layout_width="200dp"
        android:layout_height="wrap_content"
        android:text="@{data}"/>

</layout>
```

```
// 动态绑定，数据绑定到 data 变量，交互逻辑绑定到 viewModel 变量
val itemBinding = object : UnitItemBinding(R.layout.item) {
    @Override
    public Object createViewModel(Object item) {
        return ViewModel(item);
    }
}
// 自定义动态绑定 Id
itemBinding.setBindVariable(BR.item, BR.vm)
```

# 多样视图类型 BaseItemBinding

```
val itemBinding = object : BaseItemBinding() {
    @Override
    public int getItemViewType(Object item) {
        if (item is A) {
            return R.layout.item_a
        } else {
            return R.layout.item_b
        }
    }
    @Override
    public void bindView(ViewDataBinding binding, Object item, int position) {
        super.bindView(binding, item, position);
        // 自定义绑定行为
        binding.setVariable(BR.styles, styles);
    }
}
```
* 退化为 ViewHolder
```
// item2.xml 不是 layout 动态绑定
val itemBinding = object : UnitItemBinding(R.layout.item2) {
    @Override
    protected void bindView(View view, Object item, int position) {
        val textView = view.findItemById(R.id.textView) as TextView
        textView.text = item.toString()
    }
}
```

