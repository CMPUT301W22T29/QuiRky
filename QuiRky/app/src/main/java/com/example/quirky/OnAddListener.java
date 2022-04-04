/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

/**
 * @Author Sean
 * Listener to be used with ListeningList
 * @param <E> The datatype the associated ListeningList holds
 * @see ListeningList
 */
public abstract class OnAddListener<E> {

    public abstract void onAdd(ListeningList<E> listeningList);
}
