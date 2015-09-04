/*
 * Copyright (c) 2015. Greg Cantrell, All rights reserved.
 */

package com.examples.gregcantrell.textchordgraph.ChordGraph;

import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by gcantrell on 8/27/2015.
 *
 * This class is a data model that contains the data needed to create and visualize a Chord Graph.
 * Items are added and chords are created when an added item has matching values.
 * TODO: currently not taking into account multi-threading. Unless we add animation or realtime
 * TODO: updating it isn't necessary.
 *
 * @param <T> - .equals() is used so any type used should take that into consideration
 */
public class ChordGraphModel<T> {
    private HashMap<UUID, ChordGraphItem<T>> items;
    private HashMap<String, ChordGraphCategory> chordCategories;
    private ArrayList<Chord> chords;

    /**
     * Constructor just initializes values that were passed in
     */
    public ChordGraphModel() {
        items = new HashMap<UUID, ChordGraphItem<T>>();
        chords = new ArrayList<Chord>();
        chordCategories = new HashMap<String, ChordGraphCategory>();
    }

    /**
     * Adds a new item to the Chord Graph data model and creates chords between matching values.
     *
     * @param item the new item to add.
     */
    public void addItem(T item) {
        ChordGraphItem<T> graphItem = new ChordGraphItem<T>((items.size()), item);
        //Chord creation logic
        addChords(graphItem);
        //add the new graph item to the collection
        addItem(graphItem);
    }

    /**
     * Takes a new item and adds new chords if matching items are found
     *
     * @param newItem the new item to be stored in the model
     */
    private void addChords(ChordGraphItem<T> newItem) {
        //Loop through all existing items and see if there is a match, then add a chord
        for (ChordGraphItem<T> item : items.values()) {
            // if the item has already been added to the items collection skip it
            if (item.id == newItem.id) {
                continue;
            }

            // Logic for creating a link between two elements.
            // The .equals() function is used currently. This would not work with user created
            // objects unless the .equals function is overridden.
            if (item.value.equals(newItem.value)) {
                // create the new chord
                Chord chord = new Chord(item, newItem, 1.0f);

                // register it with the items
                item.addChord(chord);
                newItem.addChord(chord);

                // add it to the collection
                chords.add(chord);
            }
        }
    }

    /**
     * Adds an item to the data model.
     *
     * @param newItem the new item to be stored in the model
     */
    private void addItem(ChordGraphItem<T> newItem) {
        // Keep some stats about the items
        ChordGraphCategory stats = this.chordCategories.get(newItem.value.toString());
        if (stats != null) {
            // if there already is a stats entry just add this to it
            stats.addItem(newItem);
        } else {
            // put a new stats entry in the stats colletion
            this.chordCategories.put(newItem.value.toString(), new ChordGraphCategory(newItem));
        }

        // add the item to the collection
        this.items.put(newItem.id, newItem);
    }


    /**
     * Clears the data model.
     */
    public void clear() {
        if (null != this.chordCategories)
            chordCategories.clear();
        if (null != this.chords)
            chords.clear();
        if (null != this.items)
            items.clear();
    }

    /**
     * getter for the collection of items
     * @return
     */
    protected HashMap<UUID, ChordGraphItem<T>> getItems() {
        return items;
    }

    /**
     * getter for the collection of categories
     * @return
     */
    protected HashMap<String, ChordGraphCategory> getChordCategories() {
        return chordCategories;
    }

    /**
     * getter for the collection of chords
     * @return
     */
    protected ArrayList<Chord> getChords() {
        return chords;
    }

    /**
     * This class represents a link between two items in the data model. A value can be assigned
     * for changing the visualization in some way(ie. higher alpha value based on number of
     * connections).
     */
    protected class Chord {
        public Pair<ChordGraphItem<T>, ChordGraphItem<T>> itemTuple;
        public float value;

        /**
         * Constructor just initializes values that were passed in.
         *
         * @param head  the head of the chord link
         * @param tail  the tail of the chord link
         * @param value the value of this chord
         */
        Chord(ChordGraphItem<T> head, ChordGraphItem<T> tail, float value) {
            itemTuple = new Pair<ChordGraphItem<T>, ChordGraphItem<T>>(head, tail);
            this.value = value;
        }
    }

    /**
     * This class represents one item that will be or has been loaded into the model. An item can
     * be any sub-class of Object as it will use the .equals() to determine a link between other
     * items.
     *
     * @param <T>
     */
    protected class ChordGraphItem<T> {
        protected UUID id; //a unique id for finding this item
        protected int index; //the index in the ChordGraph for ordering
        protected T value; //the value to store, will be used to link to other items
        protected ArrayList<ChordGraphModel.Chord> chords; //the collection of chords

        /**
         * Constructor just initializes values that were passed in
         *
         * @param index the index of this item
         * @param value the value this item is representing
         */
        protected ChordGraphItem(int index, T value) {
            id = UUID.randomUUID();
            this.index = index;
            this.value = value;
            this.chords = new ArrayList<ChordGraphModel.Chord>();
        }

        /**
         * Adds a chord to the list of chords linked to this item
         *
         * @param chord the chord to add to the list
         */
        protected void addChord(ChordGraphModel.Chord chord) {
            this.chords.add(chord);
        }
    }

    /**
     * This class represents a distinct value category. There should be one per distinct value in
     * the model. A list of the items that have this value is stored as well as the count of
     * items that have the same value.
     */
    protected class ChordGraphCategory {
        protected String distinctValue;
        protected int count;
        protected ArrayList<ChordGraphItem<T>> items;

        /**
         * Constructor just initializes values that were passed in.
         *
         * @param item the first item to be added to the category
         */
        protected ChordGraphCategory(ChordGraphItem<T> item) {
            distinctValue = item.value.toString();
            count = 1;
            items = new ArrayList<ChordGraphItem<T>>();
            items.add(item);
        }

        /**
         * Adds another item to list of items with the same value.
         *
         * @param item the item to be added to the category
         */
        protected void addItem(ChordGraphItem<T> item) {
            if (item.value.toString().equals(distinctValue)) {
                // we could check for duplicates but for now it is unnecessary
                items.add(item);
                count++;
            }
        }
    }
}