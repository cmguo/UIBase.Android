# 列表视图

提供下列功能：
* 数据封装：RecyclerList，树形数据：RecyclerTreeList，支持数据编辑、变化通知
* 通用列表适配器：支持托管点击回调、空视图、数据更新
* 各种装饰器：填充，分割线，支持快速扩展，支持树形数据
* 简单的 RecylcerView 封装： ZListView，支持图标、标题、详细模式，扩展操作交互

* 数据封装 RecyclerList
``` kotlin
val recyclerList (recylerView.adpater as RecylerViewAdpater).getItems()
// 操作数据，列表会自动更新
recyclerList.clear()
recyclerList.add(Item())
recyclerList.move(1, 0) // 移动 Item 1 到位置 0
```

* 树形数据 RecyclerTreeList
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