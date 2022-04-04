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
    public boolean add(E o) {
        if (super.add(o)) {
            listener.onAdd(this);
            return true;
        }
        return false;
    }

    @Override
    public void add(int index, E element) {
        super.add(index, element);
        listener.onAdd(this);
    }

    @Override
    public boolean addAll(int index, @NonNull Collection<? extends E> c) {
        if (super.addAll(index, c)) {
            listener.onAdd(this);
            return true;
        }
        return false;
    }
}
