package com.eazy.uibase.view.list;

import androidx.databinding.ListChangeRegistry;
import androidx.databinding.ObservableList;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public class RecyclerList<T> extends ArrayList<T> implements ObservableList<T> {

    private transient ListChangeRegistry mListeners = new ListChangeRegistry();

    public RecyclerList() {}

    public RecyclerList(int initialCapacity) {
        super(initialCapacity);
    }

    public RecyclerList(Collection<? extends T> c) {
        super(c);
    }

    @Override
    public void addOnListChangedCallback(ObservableList.OnListChangedCallback listener) {
        if (mListeners == null) {
            mListeners = new ListChangeRegistry();
        }
        mListeners.add(listener);
    }

    @Override
    public void removeOnListChangedCallback(ObservableList.OnListChangedCallback listener) {
        if (mListeners != null) {
            mListeners.remove(listener);
        }
    }

    @Override
    public boolean add(T object) {
        super.add(object);
        notifyAdd(size() - 1, 1);
        return true;
    }

    @Override
    public void add(int index, T object) {
        super.add(index, object);
        notifyAdd(index, 1);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends T> collection) {
        int oldSize = size();
        boolean added = super.addAll(collection);
        if (added) {
            notifyAdd(oldSize, size() - oldSize);
        }
        return added;
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends T> collection) {
        boolean added = super.addAll(index, collection);
        if (added) {
            notifyAdd(index, collection.size());
        }
        return added;
    }

    @Override
    public void clear() {
        int oldSize = size();
        super.clear();
        if (oldSize != 0) {
            notifyRemove(0, oldSize);
        }
    }

    @Override
    public T remove(int index) {
        T val = super.remove(index);
        notifyRemove(index, 1);
        return val;
    }

    @Override
    public boolean remove(Object object) {
        int index = indexOf(object);
        if (index >= 0) {
            remove(index);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public T set(int index, T object) {
        T val = super.set(index, object);
        if (mListeners != null) {
            mListeners.notifyChanged(this, index, 1);
        }
        return val;
    }

    public void move(int fromPosition, int toPosition) {
        move(fromPosition, toPosition, 1);
    }

    public void move(int fromPosition, int toPosition, int itemCount) {
        if (fromPosition < toPosition) {
            int t = toPosition + itemCount - 1;
            for (int i = 0; i < itemCount; ++i) {
                super.add(t, super.remove(fromPosition));
            }
        } else {
            int f = fromPosition;
            int t = toPosition;
            for (int i = 0; i < itemCount; ++i) {
                super.add(t, super.remove(f));
                ++f;
                ++t;
            }
        }
        if (mListeners != null) {
            mListeners.notifyMoved(this, fromPosition, toPosition, itemCount);
        }
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        super.removeRange(fromIndex, toIndex);
        notifyRemove(fromIndex, toIndex - fromIndex);
    }

    private void notifyAdd(int start, int count) {
        if (mListeners != null) {
            mListeners.notifyInserted(this, start, count);
        }
    }

    private void notifyRemove(int start, int count) {
        if (mListeners != null) {
            mListeners.notifyRemoved(this, start, count);
        }
    }
}
