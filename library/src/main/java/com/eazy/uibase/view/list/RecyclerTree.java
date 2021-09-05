package com.eazy.uibase.view.list;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableList;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class RecyclerTree<T> extends AbstractList<T> {

    public final static int[] RootPosition = new int[0];

    protected final RecyclerTree<T> parentTree;
    private int firstPosition;
    private int itemCount = 1; // all items in sub tree, including self
    private ArrayList<RecyclerTree<T>> subTrees = new ArrayList<>();
    private List<T> children;
    private final OnListChangedCallback changedCallback = new OnListChangedCallback();

    public RecyclerTree(RecyclerTree<T> parent) {
        this(parent, new ArrayList<>());
    }

    public RecyclerTree(RecyclerTree<T> parent, Iterable<T> collection) {
        parentTree = parent == null ? this : parent;
        children = Items.toList(collection);
        subTrees.ensureCapacity(children.size());
        for (int i = 0; i < children.size(); ++i) {
            Iterable<T> children = getChildren(this.children.get(i));
            if (children != null) {
                RecyclerTree<T> subTree = new RecyclerTree<>(this, children);
                subTree.firstPosition = itemCount;
                itemCount += subTree.itemCount;
                subTrees.add(i, subTree);
            } else {
                itemCount += 1;
                subTrees.add(null);
            }
        }
        if (children instanceof ObservableList) {
            ((ObservableList<T>) children).addOnListChangedCallback(changedCallback);
        }
    }

    @Override
    public int size() {
        return itemCount;
    }

    @Override
    public T get(int index) {
        return getItem(index);
    }

    @NonNull
    @Override
    public Iterator<T> iterator() {
        return new TreeIterator<>(this);
    }

    /* Child methods */

    public boolean addChild(T object) {
        addChild(children.size(), object);
        return true;
    }

    public void addChild(int index, T object) {
        if (!changedCallback.lock) {
            try {
                changedCallback.lock = true;
                children.add(index, object);
            } finally {
                changedCallback.lock = false;
            }
        }
        Iterable<T> children = getChildren(object);
        int position = leafItemPosition(index);
        int count = 1;
        if (children != null) {
            RecyclerTree<T> subTree = new RecyclerTree<>(this, children);
            subTree.firstPosition = position;
            subTrees.add(index, subTree);
            count = subTree.itemCount;
        } else {
            subTrees.add(index, null);
        }
        updateSubTreesPosition(index + 1, count);
        parentTree.notifyInserted(this, position, count);
    }

    public boolean addChildren(@NonNull Collection<? extends T> collection) {
        return addChildren(children.size(), collection);
    }

    public boolean addChildren(int index, @NonNull Collection<? extends T> collection) {
        if (!changedCallback.lock) {
            try {
                changedCallback.lock = true;
                boolean added = children.addAll(index, collection);
                if (!added)
                    return false;
            } finally {
                changedCallback.lock = false;
            }
        }
        int position = leafItemPosition(index);
        int count = 0;
        subTrees.ensureCapacity(children.size());
        for (int i = index; subTrees.size() < children.size(); ++i) {
            Iterable<T> children = getChildren(this.children.get(i));
            if (children != null) {
                RecyclerTree<T> subTree = new RecyclerTree<>(this, children);
                subTree.firstPosition = position + count;
                subTrees.add(i, subTree);
                count += subTree.itemCount;
            } else {
                subTrees.add(i, null);
                count += 1;
            }
        }
        updateSubTreesPosition(index + collection.size(), count);
        parentTree.notifyInserted(this, position, count);
        return true;
    }

    public void clearChildren() {
        int oldSize = children.size();
        for (RecyclerTree<T> subTree : subTrees) {
            if (subTree != null)
                subTree.detach();
        }
        subTrees.clear();
        if (!changedCallback.lock) {
            try {
                changedCallback.lock = true;
                children.clear();
            } finally {
                changedCallback.lock = false;
            }
        }
        if (oldSize != 0) {
            itemCount = 1;
            parentTree.notifyRemoved(this, firstPosition, itemCount - 1);
        }
    }

    public T removeChild(int index) {
        T val = null;
        if (!changedCallback.lock) {
            try {
                changedCallback.lock = true;
                val = children.remove(index);
            } finally {
                changedCallback.lock = false;
            }
        }
        RecyclerTree<T> subTree = subTrees.remove(index);
        if (subTree == null) {
            int position = leafItemPosition(index);
            updateSubTreesPosition(index, -1);
            parentTree.notifyRemoved(this, position, 1);
        } else {
            subTree.detach();
            updateSubTreesPosition(index, -subTree.itemCount);
            parentTree.notifyRemoved(this, subTree.firstPosition, subTree.itemCount);
        }
        return val;
    }

    public boolean removeChild(T object) {
        int index = children.indexOf(object);
        if (index >= 0) {
            removeChild(index);
            return true;
        } else {
            return false;
        }
    }

    public T setChild(int index, T object) {
        T val = null;
        if (!changedCallback.lock) {
            try {
                changedCallback.lock = true;
                val = children.set(index, object);
            } finally {
                changedCallback.lock = false;
            }
        }
        if (val == object)
            return val;
        RecyclerTree<T> subTree1 = subTrees.get(index);
        Iterable<T> children = getChildren(object);
        RecyclerTree<T> subTree2 = children == null ? null : new RecyclerTree<>(this, children);
        if (subTree1 == null) {
            int position = leafItemPosition(index);
            if (subTree2 == null) {
                parentTree.notifyChanged(this, position, 1);
            } else {
                subTree2.firstPosition = position;
                subTrees.set(index, subTree2);
                updateSubTreesPosition(index + 1, subTree2.itemCount - 1);
                parentTree.notifyChanged(this, position, 1);
                parentTree.notifyInserted(this, position + 1, subTree2.itemCount - 1);
            }
        } else {
            subTree1.detach();
            subTrees.set(index, subTree2);
            if (subTree2 == null) {
                updateSubTreesPosition(index + 1, 1 - subTree1.itemCount);
                parentTree.notifyChanged(this, subTree1.firstPosition, 1);
                parentTree.notifyRemoved(this, subTree1.firstPosition + 1, subTree1.itemCount - 1);
            } else {
                subTree2.firstPosition = subTree1.firstPosition;
                updateSubTreesPosition(index + 1, subTree2.itemCount - subTree1.itemCount);
                parentTree.notifyChanged(this, subTree1.firstPosition, 1);
                parentTree.notifyRemoved(this, subTree1.firstPosition + 1, subTree1.itemCount - 1);
                parentTree.notifyInserted(this, subTree2.firstPosition + 1, subTree2.itemCount - 1);
            }
        }
        return val;
    }

    public void moveChildren(int fromPosition, int toPosition, int itemCount) {
        int position1 = itemPosition(fromPosition);
        int position2 = itemPosition(fromPosition + itemCount);
        boolean lock = changedCallback.lock;
        changedCallback.lock = true;
        if (fromPosition < toPosition) {
            int t = toPosition + itemCount - 1;
            for (int i = 0; i < itemCount; ++i) {
                if (!lock)
                    children.add(t, children.remove(fromPosition));
                subTrees.add(t, subTrees.remove(fromPosition));
            }
            toPosition += itemCount;
        } else {
            int f = fromPosition;
            int t = toPosition;
            for (int i = 0; i < itemCount; ++i) {
                if (!lock)
                    children.add(t, children.remove(f));
                subTrees.add(t, subTrees.remove(f));
                ++f;
                ++t;
            }
            fromPosition = toPosition;
            toPosition = f;
        }
        changedCallback.lock = false;
        int position3 = itemPosition(toPosition + itemCount);
        int position = Math.min(position1, position3);
        for (int i = fromPosition; i < toPosition; ++i) {
            RecyclerTree<T> subTree = subTrees.get(i);
            if (subTree == null) {
                ++position;
            } else {
                subTree.firstPosition = position;
                position += subTree.itemCount;
            }
        }
        parentTree.notifyMoved(this, position1, position3, position2 - position1);
    }

    public void removeChildren(int fromIndex, int toIndex) {
        if (!changedCallback.lock) {
            try {
                changedCallback.lock = true;
                children.subList(fromIndex, toIndex).clear();
            } finally {
                changedCallback.lock = false;
            }
        }
        int position1 = itemPosition(fromIndex);
        int position2 = itemPosition(toIndex);
        for (int i = fromIndex; i < toIndex; ++i) {
            RecyclerTree<T> subTree = subTrees.get(i);
            if (subTree != null)
                subTree.detach();
        }
        subTrees.subList(fromIndex, toIndex).clear();
        updateSubTreesPosition(toIndex, position1 - position2);
        parentTree.notifyRemoved(this, position1, position2 - position1);
    }

    /* Tree methods */

    public int getItemCount() {
        return itemCount;
    }

    public T getItem(int position) {
        if (position == 0) {
            return null; // should not got here
        }
        if (position >= itemCount)
            throw new IndexOutOfBoundsException("Failed to get " + position + "-th item from" + toString());
        --position;
        for (int i = 0; i < subTrees.size(); ++i) {
            if (position == 0)
                return children.get(i);
            RecyclerTree<T> subTree = subTrees.get(i);
            if (subTree == null) {
                --position;
            } else if (position < subTree.itemCount) {
                return subTree.getItem(position);
            } else {
                position -= subTree.itemCount;
            }
        }
        throw new IndexOutOfBoundsException("State error " + toString());
    }

    public T getItem(int[] position) {
        return getItem(position, position.length);
    }

    // maxLevel: tell position real length
    public T getItem(int[] positions, int maxLevel) {
        return getItem(positions, 0, maxLevel);
    }

    public int[] getTreePosition(int n) {
        return getTreePosition(n, 0);
    }

    public int getItemPosition(@NonNull int[] positions) {
        return getItemPosition(positions, positions.length);
    }

    // maxLevel: tell position real length
    public int getItemPosition(@NonNull int[] positions, int maxLevel) {
        return getItemPosition(positions, 0, maxLevel) - 1;
    }

    public int[] getNextPosition(@NonNull int[] positions) {
        return getNextPosition(positions, positions.length);
    }

    // maxLevel: tell position real length
    public int[] getNextPosition(@NonNull int[] positions, int maxLevel) {
        return getNextPosition(positions, 0, maxLevel);
    }

    /* Protected methods */

    // Should implement this to build tree structure
    protected Iterable<T> getChildren(T t) {
        return parentTree.getChildren(t);
    }

    @FunctionalInterface
    protected interface TreeVisitor {
        void visitTree(int[] firstPos, int firstLevel, int[] lastPos, int level);
    }

    protected void visitTrees(TreeVisitor visitor, int[] firstPos, int[] lastPos) {
        visitTrees(visitor, firstPos, firstPos.length, lastPos, 0);
    }

    protected void visitTrees(TreeVisitor visitor, int[] firstPos, int firstLevel, int[] lastPos) {
        visitTrees(visitor, firstPos, firstLevel, lastPos, 0);
    }

    protected void notifyInserted(RecyclerTree<T> subTree, int position, int count) {
        itemCount += count;
        parentTree.notifyInserted(this, firstPosition + position, count);
    }

    protected void notifyChanged(RecyclerTree<T> subTree, int position, int count) {
        parentTree.notifyChanged(this, firstPosition + position, count);
    }

    protected void notifyMoved(RecyclerTree<T> subTree, int position, int position2, int count) {
        parentTree.notifyMoved(this, firstPosition + position, firstPosition + position2, count);
    }

    protected void notifyRemoved(RecyclerTree<T> subTree, int position, int count) {
        itemCount -= count;
        parentTree.notifyRemoved(this, firstPosition + position, count);
    }

    /* Private methods */

    private static class TreeIterator<T> implements Iterator<T> {
        private final RecyclerTree<T> tree;
        private int[] nextPosition = RootPosition;
        TreeIterator(RecyclerTree<T> tree) {
            this.tree = tree;
        }
        @Override
        public boolean hasNext() {
            return nextPosition != null;
        }
        @Override
        public T next() {
            T t = tree.getItem(nextPosition);
            nextPosition = tree.getNextPosition(nextPosition);
            return t;
        }
    }

    private class OnListChangedCallback extends ObservableList.OnListChangedCallback<ObservableList<T>> {
        private boolean lock;

        @Override
        public void onChanged(ObservableList sender) {
        }
        @Override
        public void onItemRangeChanged(ObservableList<T> sender, int positionStart, int itemCount) {
            if (lock)
                return;
            lock = true;
            for (int i = positionStart; i < positionStart + itemCount; ++i) {
                setChild(i, sender.get(i));
            }
            lock = false;
        }
        @Override
        public void onItemRangeInserted(ObservableList<T> sender, int positionStart, int itemCount) {
            if (lock)
                return;
            lock = true;
            addChildren(positionStart,
                new ArrayList<>(children.subList(positionStart, positionStart + itemCount)));
            lock = false;
        }
        @Override
        public void onItemRangeMoved(ObservableList<T> sender, int fromPosition, int toPosition, int itemCount) {
            if (lock)
                return;
            lock = true;
            moveChildren(fromPosition, toPosition, itemCount);
            lock = false;
        }
        @Override
        public void onItemRangeRemoved(ObservableList<T> sender, int positionStart, int itemCount) {
            if (lock)
                return;
            lock = true;
            removeChildren(positionStart, positionStart + itemCount);
            lock = false;
        }
    }

    private void detach() {
        if (children instanceof ObservableList) {
            ((ObservableList<T>) children).removeOnListChangedCallback(changedCallback);
        }
    }

    // Get item in sub tree with tree position <n>
    //  l: sub tree level
    //  m: tell real length of <n>
    private T getItem(int[] positions, int level, int maxLevel) {
        if (level >= maxLevel) {
            return null; // should not got here
        }
        int index = positions[level];
        ++level;
        RecyclerTree<T> subTree = subTrees.get(index);
        if (subTree == null) {
            if (level < maxLevel) {
                throw new IndexOutOfBoundsException("Failed to get " + index + "-th sub tree from" + toString());
            }
            return children.get(index);
        } else {
            return subTree.getItem(positions, level, maxLevel);
        }
    }

    // Get n-th item position in sub tree
    //  l: sub tree level, result[j] == 0 if j < l, and is filled by outer stack
    private int[] getTreePosition(int position, int level) {
        if (position == 0) {
            return new int[level]; // all zero
        }
        if (position >= itemCount)
            throw new IndexOutOfBoundsException("Failed to get " + position + "-th item from" + toString());
        --position;
        ++level;
        for (int i = 0; i < subTrees.size(); ++i) {
            if (position == 0) {
                int[] positions = new int[level];
                positions[level - 1] = i;
                return positions;
            }
            RecyclerTree<T> subTree = subTrees.get(i);
            if (subTree == null) {
                --position;
            } else if (position < subTree.itemCount) {
                int[] positions = subTree.getTreePosition(position, level);
                positions[level] = i;
            } else {
                position -= subTree.itemCount;
            }
        }
        throw new IndexOutOfBoundsException("State error " + toString());
    }

    // Get linear index in sub tree with tree position <n>
    //  l: sub tree level
    //  m: tell real length of <n>
    private int getItemPosition(int[] positions, int level, int maxLevel) {
        if (level == maxLevel) {
            return 0;
        }
        int index = positions[level];
        ++level;
        RecyclerTree<T> subTree = subTrees.get(index);
        if (subTree == null) {
            if (level < maxLevel) {
                throw new IndexOutOfBoundsException("Failed to get " + index + "-th sub tree from" + toString());
            }
            return leafItemPosition(index);
        } else {
            return subTree.firstPosition + subTree.getItemPosition(positions, level, maxLevel);
        }
    }

    // Get next item in sub tree with tree position <n>
    //  l: sub tree level
    //  m: tell real length of <n>
    //  return null if at end, upper stack will handle null and try in sibling sub tree
    private int[] getNextPosition(int[] positions, int level, int maxLevel) {
        if (level == maxLevel) { // self's next
            return Arrays.copyOf(positions, level + 1);
        }
        int index = positions[level];
        ++level;
        RecyclerTree<T> subTree = subTrees.get(index);
        if (subTree == null) {
            if (level < maxLevel) {
                throw new IndexOutOfBoundsException("Failed to get " + index + "-th sub tree from" + toString());
            }
        } else {
            int[] positions2 = subTree.getNextPosition(positions, level, maxLevel);
            if (positions2 != null) {
                return positions2;
            }
        }
        ++index;
        if (index < subTrees.size()) {
            // next sibling
            positions = Arrays.copyOf(positions, level);
            positions[level - 1] = index;
            return positions;
        }
        // last item in this tree
        return null;
    }

    // Item t is at <firstPos with length firstLevel>
    private void visitTrees(TreeVisitor visitor, int[] firstPos, int firstLevel, int[] lastPos, int level) {
        visitor.visitTree(firstPos, firstLevel, lastPos, level);
        if (firstLevel == level && lastPos.length == level)
            return;
        if (firstPos.length < level + 1) {
            firstPos = Arrays.copyOf(firstPos, level + 1);
        }
        if (firstLevel < level + 1)
            firstLevel = level + 1;
        while (firstPos[level] < lastPos[level]) {
            RecyclerTree<T> subTree = subTrees.get(firstPos[level]);
            if (subTree != null) {
                int[] subEnd = subTree.tailPosition(firstPos, level + 1);
                subTree.visitTrees(visitor, firstPos, firstLevel, subEnd, level + 1);
            }
            for (int i = level + 1; i < firstPos.length; ++i)
                firstPos[i] = 0;
            ++firstPos[level];
            firstLevel = level + 1;
        }
        RecyclerTree<T> subTree = subTrees.get(firstPos[level]);
        if (subTree != null) {
            subTree.visitTrees(visitor, firstPos, firstLevel, lastPos, level + 1);
        }
    }

    // Self is at <position with length level>
    private int[] tailPosition(int[] position, int level) {
        int index = subTrees.size() - 1;
        if (index < 0) {
            // Empty list, return self
            return Arrays.copyOf(position, level);
        }
        RecyclerTree<T> subTree = subTrees.get(index);
        int[] tail;
        if (subTree == null) {
            tail = Arrays.copyOf(position, level + 1);
        } else {
            tail = subTree.tailPosition(position, level + 1);
        }
        tail[level] = index;
        return tail;
    }

    private int itemPosition(int index) {
        if (index == subTrees.size())
            return itemCount;
        RecyclerTree<T> subTree = subTrees.get(index);
        return subTree == null ? leafItemPosition(index) : subTree.firstPosition;
    }

    private int leafItemPosition(int index) {
        for (int i = index - 1; i >= 0; --i) {
            RecyclerTree<T> subTree = subTrees.get(i);
            if (subTree != null) {
                return subTree.firstPosition + subTree.itemCount + index - i - 1;
            }
        }
        return index + 1;
    }

    private void updateSubTreesPosition(int index, int diff) {
        for (int i = index; i < subTrees.size(); ++i) {
            RecyclerTree<T> subTree = subTrees.get(i);
            if (subTree != null)
                subTree.firstPosition += diff;
        }
        itemCount += diff;
    }


    public static void test() {
        class Item {
            List<Item> children;
        }

        List<Item> items = new ArrayList<>();
        items.add(new Item());  // 0
        items.get(0).children = new ArrayList<>();
        items.get(0).children.add(new Item());  // 00
        items.get(0).children.get(0).children = new ArrayList<>();
        items.get(0).children.get(0).children.add(new Item());  // 000
        items.get(0).children.get(0).children.add(new Item());  // 001
        items.get(0).children.add(new Item());  // 01
        items.get(0).children.get(1).children = new ArrayList<>();
        items.get(0).children.get(1).children.add(new Item());  // 010
        items.get(0).children.get(1).children.add(new Item());  // 011
        items.get(0).children.get(1).children.add(new Item());  // 012
        items.add(new Item());  // 1
        items.get(1).children = new ArrayList<>();
        items.get(1).children.add(new Item()); // 10
        items.get(1).children.get(0).children = new ArrayList<>();
        items.get(1).children.get(0).children.add(new Item());  // 100
        items.get(1).children.get(0).children.get(0).children = new ArrayList<>();
        items.get(1).children.get(0).children.get(0).children.add(new Item());  // 1000
        items.get(1).children.get(0).children.get(0).children.add(new Item());  // 1001
        items.get(1).children.get(0).children.add(new Item());  // 101
        items.get(1).children.get(0).children.add(new Item());  // 102
        items.get(1).children.add(new Item()); // 11
        items.get(1).children.get(1).children = new ArrayList<>();
        items.get(1).children.get(1).children.add(new Item());  // 110
        items.get(1).children.get(1).children.add(new Item());  // 111

        RecyclerTreeList<Item> list = new RecyclerTreeList<Item>(items) {
            @Override
            protected Iterable<Item> getChildren(Item t) {
                return t.children;
            }
        };
        int[] p = RootPosition;
        while ((p = list.getNextPosition(p)) != null) {
            Log.d("RecyclerTree", "getNextPosition " + Arrays.toString(p));
        }

        list.visitTrees( (firstPos, firstLevel, lastPos, level) ->
                Log.d("RecyclerTree", "visitTrees level " + level + ": "
                    + Arrays.toString(Arrays.copyOf(firstPos, firstLevel)) + " ~ "
                    + Arrays.toString(lastPos))
            , new int[] {0}, new int[] {1});
    }
}
