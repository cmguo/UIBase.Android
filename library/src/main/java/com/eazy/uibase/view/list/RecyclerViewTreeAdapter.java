package com.eazy.uibase.view.list;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/*
  Tree without root
 */

public class RecyclerViewTreeAdapter extends RecyclerViewAdapter {

    public final static int[] RootPosition = new int[0];

    @Override
    protected int getRealItemCount() {
        return getItemsCount(this) - 1;
    }

    @Override
    protected Object getRealItem(int position) {
        return getItem(this, position + 1);
    }

    public Object getRealItem(int[] position) {
        return getRealItem(position, position.length);
    }

    // maxLevel: tell position real length
    public Object getRealItem(int[] position, int maxLevel) {
        return getRealItem(this, position, 0, maxLevel);
    }

    public int[] getTreePosition(int n) {
        return getTreePosition(this, n + 1, 0);
    }

    public int getItemPosition(@NotNull int[] position) {
        return getItemPosition(position, position.length);
    }

    // maxLevel: tell position real length
    public int getItemPosition(@NotNull int[] position, int maxLevel) {
        return getItemPosition(this, position, 0, maxLevel) - 1;
    }

    public int[] getNextPosition(@NotNull int[] position) {
        return getNextPosition(position, position.length);
    }

    // maxLevel: tell position real length
    public int[] getNextPosition(@NotNull int[] position, int maxLevel) {
        return getNextPosition(this, position, 0, maxLevel);
    }

    // Should implement this to build tree structure
    protected Iterable<?> getChildren(Object t) {
        return null;
    }

    // Get number of items in sub tree, include sub root
    protected int getItemsCount(Object t) {
        Iterable<?> children = getChildren2(t);
        if (children == null)
            return 1;
        Iterator<?> iterator = children.iterator();
        int n = 1;
        while (iterator.hasNext()) {
            n += getItemsCount(iterator.next());
        }
        return n;
    }

    // Used direct by other methods, consider adapter self as root
    private Iterable<?> getChildren2(Object t) {
        return t == this ? getData() : getChildren(t);
    }

    // Get n-th item in sub tree, index 0 is sub root
    public Object getItem(Object t, int n) {
        if (n == 0) {
            return t;
        }
        Iterable<?> children = getChildren2(t);
        if (children == null)
            throw new IndexOutOfBoundsException("Failed to get " + (n + 1) + "-th item from" + t.toString());
        Iterator<?> iterator = children.iterator();
        int n2 = 1;
        while (iterator.hasNext()) {
            Object c = iterator.next();
            int n3 = getItemsCount(c);
            if (n < n2 + n3) {
                return getItem(c, n - n2);
            }
            n2 += n3;
        }
        throw new IndexOutOfBoundsException("Failed to get " + (n + 1) + "-th item from" + t.toString());
    }

    // Get n-th item position in sub tree
    //  l: sub tree level, result[j] == 0 if j < l, and is filled by outer stack
    private int[] getTreePosition(Object t, int n, int l) {
        if (n == 0) {
            return new int[l];
        }
        Iterable<?> children = getChildren2(t);
        if (children == null)
            throw new IndexOutOfBoundsException("Failed to get " + (n + 1) + "-th item from" + t.toString());
        Iterator<?> iterator = children.iterator();
        int p = 0;
        int n2 = 1;
        while (iterator.hasNext()) {
            Object c = iterator.next();
            int n3 = getItemsCount(c);
            if (n < n2 + n3) {
                int[] position = getTreePosition(c, n - n2, l + 1);
                position[l] = p;
                return position;
            }
            n2 += n3;
            ++p;
        }
        throw new IndexOutOfBoundsException("Failed to get " + (n + 1) + "-th item from" + t.toString());
    }

    // Get linear index in sub tree with tree position <n>
    //  l: sub tree level
    //  m: tell real length of <n>
    private int getItemPosition(Object t, int[] n, int l, int m) {
        if (l >= m) {
            return 0;
        }
        Iterable<?> children = getChildren2(t);
        if (children == null)
            throw new IndexOutOfBoundsException("Failed to get " + n[l] + "-th child from" + t.toString());
        Iterator<?> iterator = children.iterator();
        int p = n[l];
        int n2 = 1;
        while (p > 0 && iterator.hasNext()) {
            n2 += getItemsCount(iterator.next());
            --p;
        }
        if (iterator.hasNext()) {
            return n2 + getItemPosition(iterator.next(), n, l + 1, m);
        }
        throw new IndexOutOfBoundsException("Failed to get " + n[l] + "-th child from" + t.toString());
    }

    // Get item in sub tree with tree position <n>
    //  l: sub tree level
    //  m: tell real length of <n>
    private Object getRealItem(Object t, int[] n, int l, int m) {
        if (l >= m) {
            return t;
        }
        Iterable<?> children = getChildren2(t);
        if (children == null)
            throw new IndexOutOfBoundsException("Failed to get " + n[l] + "-th child from" + t.toString());
        Iterator<?> iterator = children.iterator();
        int p = n[l];
        while (p > 0 && iterator.hasNext()) {
            --p;
            iterator.next();
        }
        if (iterator.hasNext()) {
            return getRealItem(iterator.next(), n, l + 1, m);
        }
        throw new IndexOutOfBoundsException("Failed to get " + n[l] + "-th child from" + t.toString());
    }

    // Get next item in sub tree with tree position <n>
    //  l: sub tree level
    //  m: tell real length of <n>
    //  return null if at end, upper stack will handle null and try in sibling sub tree
    private int[] getNextPosition(Object t, int[] n, int l, int m) {
        Iterable<?> children = getChildren2(t);
        if (children == null)
            return null;
        Iterator<?> iterator = children.iterator();
        if (l >= m) {
            if (iterator.hasNext())
                return Arrays.copyOf(n, l + 1);
            else
                return null;
        }
        int p = n[l];
        while (p > 0 && iterator.hasNext()) {
            --p;
            iterator.next();
        }
        if (!iterator.hasNext())
            return null;
        int[] position = getNextPosition(iterator.next(), n, l + 1, m);
        if (position == null && iterator.hasNext()) {
            position = Arrays.copyOf(n, l + 1);
            ++position[l];
        }
        return position;
    }

    @FunctionalInterface
    protected interface TreeVisitor {
        void visitTree(int[] firstPos, int firstLevel, int[] lastPos, int level);
    }

    protected void visitTrees(TreeVisitor visitor, int[] firstPos, int[] lastPos) {
        visitTrees(visitor, this, firstPos, firstPos.length, lastPos, 0);
    }

    protected void visitTrees(TreeVisitor visitor, int[] firstPos, int firstLevel, int[] lastPos) {
        visitTrees(visitor, this, firstPos, firstLevel, lastPos, 0);
    }

    // Item t is at <firstPos with length firstLevel>
    private void visitTrees(TreeVisitor visitor, Object t, int[] firstPos, int firstLevel, int[] lastPos, int level) {
        Iterable<?> children = getChildren2(t);
        if (children == null)
            return;
        visitor.visitTree(firstPos, firstLevel, lastPos, level);
        Iterator<?> iterator = children.iterator();
        if (firstLevel == level && lastPos.length == level)
            return;
        if (firstPos.length < level + 1) {
            firstPos = Arrays.copyOf(firstPos, level + 1);
        }
        if (firstLevel < level + 1)
            firstLevel = level + 1;
        int p = firstPos[level];
        while (p > 0 && iterator.hasNext()) {
            --p;
            iterator.next();
        }
        while (firstPos[level] < lastPos[level]) {
            if (!iterator.hasNext())
                throw new IndexOutOfBoundsException("Failed to get " + firstPos[level] + "-th child from" + t.toString());
            Object c = iterator.next();
            int[] subEnd = tailPosition(c, firstPos, level + 1);
            visitTrees(visitor, c, firstPos, firstLevel, subEnd, level + 1);
            for (int i = level + 1; i < firstPos.length; ++i)
                firstPos[i] = 0;
            ++firstPos[level];
            firstLevel = level + 1;
        }
        if (!iterator.hasNext())
            throw new IndexOutOfBoundsException("Failed to get " + firstPos[level] + "-th child from" + t.toString());
        visitTrees(visitor, iterator.next(), firstPos, firstLevel, lastPos, level + 1);
    }

    // Item t is at <position with length level>
    private int[] tailPosition(Object t, int[] position, int level) {
        Iterable<?> children = getChildren2(t);
        if (children == null)
            return Arrays.copyOf(position, level);
        Iterator<?> iterator = children.iterator();
        int n = -1;
        Object c = null;
        while (iterator.hasNext()) {
            ++n;
            c = iterator.next();
        }
        if (c == null)
            return Arrays.copyOf(position, level);
        int[] tail = tailPosition(c, position, level + 1);
        tail[level] = n;
        return tail;
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

        RecyclerViewTreeAdapter adapter = new RecyclerViewTreeAdapter() {
            @Override
            protected Iterable<?> getChildren(Object t) {
                return ((Item) t).children;
            }
        };
        adapter.replace(items);
//        int[] p = RootPosition;
//        while ((p = adapter.getNextPosition(p)) != null) {
//            Log.d("RecyclerViewTreeAdapter", "getNextPosition " + Arrays.toString(p));
//        }

        adapter.visitTrees( (firstPos, firstLevel, lastPos, level) ->
            Log.d("RecyclerViewTreeAdapter", "visitTrees level " + level + ": "
                + Arrays.toString(Arrays.copyOf(firstPos, firstLevel)) + " ~ "
                + Arrays.toString(lastPos))
            , new int[] {0}, new int[] {1});
    }

}
