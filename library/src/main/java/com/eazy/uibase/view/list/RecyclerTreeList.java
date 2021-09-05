package com.eazy.uibase.view.list;

import androidx.annotation.NonNull;
import androidx.databinding.ListChangeRegistry;
import androidx.databinding.ObservableList;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class RecyclerTreeList<T> extends RecyclerTree<T> implements ObservableList<T> {

    public RecyclerTreeList() {
        this(new ArrayList<>());
    }

    public RecyclerTreeList(Iterable<T> collection) {
        super(new MyRecyclerTree<T>(), collection);
    }

    @Override
    public void addOnListChangedCallback(OnListChangedCallback listener) {
        ((MyRecyclerTree<T>) (parentTree)).mListeners.add(listener);
    }

    @Override
    public void removeOnListChangedCallback(OnListChangedCallback listener) {
        ((MyRecyclerTree<T>) (parentTree)).mListeners.remove(listener);
    }

    @Override
    public int size() {
        return super.size() - 1;
    }

    @Override
    public T get(int position) {
        return super.get(position + 1);
    }

    @NonNull
    @Override
    public Iterator<T> iterator() {
        Iterator<T> iterator = super.iterator();
        iterator.next();
        return iterator;
    }

    private static class MyRecyclerTree<T> extends RecyclerTree<T> {

        private transient ListChangeRegistry mListeners = new ListChangeRegistry();

        public MyRecyclerTree() {
            super(null);
        }

        @Override
        protected void notifyInserted(RecyclerTree<T> subTree, int position, int count) {
            mListeners.notifyInserted((RecyclerTreeList<T>) subTree, position - 1, count);
        }

        @Override
        protected void notifyChanged(RecyclerTree<T> subTree, int position, int count) {
            mListeners.notifyChanged((RecyclerTreeList<T>) subTree, position - 1, count);
        }

        @Override
        protected void notifyMoved(RecyclerTree<T> subTree, int position, int position2, int count) {
            mListeners.notifyMoved((RecyclerTreeList<T>) subTree, position - 1, position2 -1, count);
        }

        @Override
        protected void notifyRemoved(RecyclerTree<T> subTree, int position, int count) {
            mListeners.notifyRemoved((RecyclerTreeList<T>) subTree, position - 1, count);
        }

        @Override
        protected Iterable<T> getChildren(T t) {
            return null;
        }
    }
}
