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
public class CodeList<E> extends ArrayList<E> {
    private OnCodeAddedListener<E> listener;

    public CodeList() {
        super();
    }

    public void setOnCodeAddedListener(OnCodeAddedListener<E> listener) {
        this.listener = listener;
    }

    @Override
    public boolean add(Object o) {
        if (super.add((E) o)) {
            listener.onCodeAdded(this);
            return true;
        }
        return false;
    }

    @Override
    public void add(int index, Object element) {
        super.add(index, (E) element);
        listener.onCodeAdded(this);
    }

    @Override
    public boolean addAll(@NonNull Collection c) {
        if (super.addAll(c)) {
            listener.onCodeAdded(this);
            return true;
        }
        return false;
    }

    @Override
    public boolean addAll(int index, @NonNull Collection c) {
        if (super.addAll(index, c)) {
            listener.onCodeAdded(this);
            return true;
        }
        return false;
    }
}
