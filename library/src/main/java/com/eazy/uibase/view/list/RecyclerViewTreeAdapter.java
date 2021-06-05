package com.eazy.uibase.view.list;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/*
  Tree without root
 */

public class RecyclerViewTreeAdapter extends RecyclerViewAdapter {

    @Override
    public int getItemCount() {
        return getItemsCount(this) - 1;
    }

    @Override
    public Object getItem(int position) {
        return getItem(this, position + 1);
    }

    public int[] getTreePosition(int n) {
        return getTreePosition(this, n + 1, 0);
    }

    public int getItemPosition(@NotNull int[] position) {
        return getItemPosition(this, position, 0) - 1;
    }

    protected Iterable<?> getChildren(Object t) {
        return null;
    }

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

    private Iterable<?> getChildren2(Object t) {
        return t == this ? getData() : getChildren(t);
    }

    public Object getItem(Object t, int n) {
        if (n == 0) {
            return t;
        }
        Iterable<?> children = getChildren2(t);
        if (children == null)
            throw new IndexOutOfBoundsException("Failed to get " + (n + 1) + "th item from" + t.toString());
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
        throw new IndexOutOfBoundsException("Failed to get " + (n + 1) + "th item from" + t.toString());
    }

    private int[] getTreePosition(Object t, int n, int l) {
        if (n == 0) {
            return new int[l];
        }
        Iterable<?> children = getChildren2(t);
        if (children == null)
            throw new IndexOutOfBoundsException("Failed to get " + (n + 1) + "th item from" + t.toString());
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
        throw new IndexOutOfBoundsException("Failed to get " + (n + 1) + "th item from" + t.toString());
    }

    private int getItemPosition(Object t, int[] n, int l) {
        if (l >= n.length) {
            return 0;
        }
        Iterable<?> children = getChildren2(t);
        if (children == null)
            throw new IndexOutOfBoundsException("Failed to get " + n[l] + "th child from" + t.toString());
        Iterator<?> iterator = children.iterator();
        int p = n[l];
        int n2 = 1;
        while (p > 0 && iterator.hasNext()) {
            n2 += getItemsCount(iterator.next());
            --p;
        }
        if (p == 0 && iterator.hasNext()) {
            return n2 + getItemPosition(iterator.next(), n, l + 1);
        }
        throw new IndexOutOfBoundsException("Failed to get " + n[l] + "th child from" + t.toString());
    }
}
