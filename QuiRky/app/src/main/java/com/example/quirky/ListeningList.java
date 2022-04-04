/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;

//TODO: javadocs
public class ListeningList<E> extends ArrayList<E> {
    private OnAddListener<E> listener;

    public ListeningList() {
        super();
    }

    public void setOnAddListener(OnAddListener<E> listener) {
        this.listener = listener;
    }

    @Override
    public boolean add(Object o) {
        if (super.add((E) o)) {
            listener.onAdd(this);
            return true;
        }
        return false;
    }

    @Override
    public void add(int index, Object element) {
        super.add(index, (E) element);
        listener.onAdd(this);
    }

    @Override
    public boolean addAll(@NonNull Collection c) {
        if (super.addAll(c)) {
            listener.onAdd(this);
            return true;
        }
        return false;
    }

    @Override
    public boolean addAll(int index, @NonNull Collection c) {
        if (super.addAll(index, c)) {
            listener.onAdd(this);
            return true;
        }
        return false;
    }
}
