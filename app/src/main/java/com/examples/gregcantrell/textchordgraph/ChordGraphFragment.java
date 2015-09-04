/*
 * Copyright (c) 2015. Greg Cantrell, All rights reserved.
 */

package com.examples.gregcantrell.textchordgraph;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.examples.gregcantrell.textchordgraph.ChordGraph.ChordGraphModel;
import com.examples.gregcantrell.textchordgraph.ChordGraph.ChordGraphView;

import java.util.Timer;

/**
 * Created by gcantrell on 8/25/2015.
 *
 * This class is the fragment that will contain the elements for the Chordgraph visualization. The
 * fragment contains an EditText that will provide the String to feed into the ChordGraphModel
 * and the ChordGraphView.
 */

public class ChordGraphFragment extends Fragment {

    private ChordGraphModel<String> model;
    private EditText textEnter;

    /**
     * Overrides the View.onCreateView so that we can add our logic.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Collect the view elements
        View view = inflater.inflate(R.layout.chordgraph_fragment, container, false);
        RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.graph_area);

        // Setup the EditText for the chordgraph input string
        textEnter = (EditText) view.findViewById(R.id.text_enter);
        textEnter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(final Editable s) {
                updateChordGraph(s.toString());
            }
        });

        // Create the chordgraph model
        model = new ChordGraphModel<String>();

        // Fill the model with the current String
        updateChordGraph(textEnter.getText().toString());

        // Add the ChordGraphView to the layout
        layout.addView(new ChordGraphView<String>(container.getContext(), model));

        return view;
    }

    /**
     * Updates the model with the given String
     * @param newText - the String to update the model with
     */
    public void updateChordGraph(String newText){
        if(model != null){
            // Clear the previous String
            model.clear();
            // Remove all non-alphanumeric values
            String [] stringArray = newText.split("[\\W]|_");
            // Populate the model
            for (String str : stringArray) {
                model.addItem(str);
            }
        }
    }

}
