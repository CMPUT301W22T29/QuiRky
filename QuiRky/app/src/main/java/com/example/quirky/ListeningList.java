/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Sean
 * Wrapper Class for an ArrayList<> that contains a listener for adding objects into the list
 * @param <E> The data type to hold
 */
public class ListeningList<E> extends ArrayList<E> {
    private OnAddListener<E> listener;

    /**
     * Constructor to initialise an empty list
     */
    public ListeningList() {
        super();
    }

    /**
     * Add a listener to call when an object is added to the list
     * @param listener The listener to be called
     */
    public void setOnAddListener(OnAddListener<E> listener) {
        this.listener = listener;
    }

    /**
     * Add an object to the list
     * @param o The object to be added
     * @return If the object was successfully added
     */
    @Override
    public boolean add(E o) {
        if (super.add(o)) {
            listener.onAdd(this);
            return true;
        }
        return false;
    }

    /**
     * Add an object to a specific index.
     * @param index The index to insert the object
     * @param element The object to be added
     */
    @Override
    public void add(int index, E element) {
        super.add(index, element);
        listener.onAdd(this);
    }

    /**
     * Add a collection of items to the list at once. This method is a mandatory override, but not used by QuiRky anywhere.
     * @param index The index to insert the items
     * @param c The collection of items to add
     * @return If the items were successfully added
     */
    @Override
    public boolean addAll(int index, @NonNull Collection<? extends E> c) {
        if (super.addAll(index, c)) {
            listener.onAdd(this);
            return true;
        }
        return false;
    }

    /**
     * Add a collection of items without specifying the index. Objects are added to the end of the list
     * @param c The collection of objects to add
     * @return If all objects were successfully added
     */
    @Override
    public boolean addAll(@NonNull Collection<? extends E> c) {
        if(super.addAll(c)) {
            listener.onAdd(this);
            return true;
        }
        return false;
    }

    /**
     * Activates the listener without adding any items to the list.
     * To be called when there are no objects to be added to the list, but activating the listener is still desirable
     */
    public void addNone() {
        listener.onAdd(this);
    }
}
