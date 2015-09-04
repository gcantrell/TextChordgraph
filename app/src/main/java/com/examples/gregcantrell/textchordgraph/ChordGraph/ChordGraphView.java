/*
 * Copyright (c) 2015. Greg Cantrell, All rights reserved.
 */

package com.examples.gregcantrell.textchordgraph.ChordGraph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

import android.view.View;

import com.examples.gregcantrell.textchordgraph.R;

import java.util.HashMap;

/**
 * Created by gcantrell on 8/21/2015.
 *
 * This class is the View of the MVC pattern of the Chordgraph visualization. The view depends on
 * the ChordGraphModel. An item in the Model will result in a slot and label around the rim of the
 * graph. A chord in the Model will be drawn as an arc with a random color and transparency based
 * on the number of connections.
 *
 * @param <T> - the type that the chordgraph is graphing. This must match the type parameter of
 *           the ChordGraphModel<T>
 */
public class ChordGraphView<T> extends View {

    private ChordGraphModel<T> model;
    private HashMap<String, Integer> distinctValueColors;
    private ColorPalette colorPalette;

    /**
     * Constructor initalizes collections and the color palette
     * @param context the Android context
     * @param model the model that drives this view
     */
    public ChordGraphView(Context context, ChordGraphModel<T> model) {
        super(context);
        this.model = model;
        this.distinctValueColors = new HashMap<String, Integer>();

        // Initialize the color palette
        this.colorPalette = new ColorPalette(
                getResources().getColor(R.color.chord_palette_gradient_begin),
                getResources().getColor(R.color.chord_palette_gradient_end),
                50);
    }

    /**
     * Overrides the View.onDraw() method for drawing the custom graphics
     * @param canvas - the canvas to draw into
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawGraph(canvas);
    }

    /**
     * Draws the graph into the provided canvas
     * @param canvas - the canvas to draw into
     */
    private void drawGraph(Canvas canvas) {
        // draw the rim first
        float buffer = 150 * 2; // buffer from edge TODO: move to style
        float canvasWidth = canvas.getWidth();
        float canvasHeight = canvas.getHeight();
        float radius = (Math.min(canvasWidth, canvasHeight) - buffer) / 2;

        // setup the Paint style
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.graph_primary_color));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4); //TODO: move to style
        paint.setAntiAlias(true);

        // create the rim and set the size
        ShapeDrawable rim = new ShapeDrawable(new OvalShape());
        float left = (canvasWidth / 2) - radius;
        float top = (canvasHeight / 2) - radius;
        float right = (canvasWidth / 2) + radius;
        float bottom = (canvasHeight / 2) + radius;
        rim.setBounds(new Rect((int) left, (int) top, (int) right, (int) bottom));
        rim.getPaint().set(paint);

        // draw the rim to the canvas
        rim.draw(canvas);

        // draw the items around the rim
        drawItems(canvas, rim);
    }

    /**
     * Calls methods to draw the elements for the items
     * @param canvas - the canvas to draw into
     * @param rim - the ShapeDrawable of the chordgraph rim
     */
    private void drawItems(Canvas canvas, ShapeDrawable rim) {
        // calculate the distance between each item around the rim
        float currentIntervalRad = (float) Math.toRadians(360.0f / ((float) model.getItems().size()));
        // draw the chords
        drawItemsChords(canvas, rim, currentIntervalRad);
        // draw the slots aroudn the rim
        drawItemsSlots(canvas, rim, currentIntervalRad);
        // draw the labels around the rim
        drawItemsLabels(canvas, rim, currentIntervalRad);
    }

    /**
     * Draw the slots around the rim of the chordgraph. One for each item.
     * @param canvas - the canvas to draw into
     * @param rim - the ShapeDrawable of the chordgraph rim
     * @param intervalRad - the interval in radians between each slot
     */
    private void drawItemsSlots(Canvas canvas, ShapeDrawable rim, float intervalRad) {
        //set the paint style
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.graph_primary_color));
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);

        // iterate around the rim drawing markers around the rim
        //TODO: Refactor this, move the radius to the style
        float newX, newY;
        int slot = 0;
        while (slot * intervalRad < 6.28) {
            newX = rim.getBounds().exactCenterX() + ((rim.getBounds().width() / 2) * (float) Math.sin(intervalRad * slot));
            newY = rim.getBounds().exactCenterY() - ((rim.getBounds().height() / 2) * (float) Math.cos(intervalRad * slot));
            canvas.drawCircle(newX, newY, 10, paint);
            slot++;
        }
    }

    /**
     * Draw the labels around the rim of the chordgraph. One for each item.
     * @param canvas - the canvas to draw into
     * @param rim - the ShapeDrawable of the chordgraph rim
     * @param intervalRad - the interval in radians between each slot
     */
    private void drawItemsLabels(Canvas canvas, ShapeDrawable rim, float intervalRad) {
        //setup the paint style
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.graph_primary_color));
        paint.setTextSize(34.0f);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setAntiAlias(true);

        // iterate through the models values and draw the labels
        for (ChordGraphModel.ChordGraphItem item : model.getItems().values()) {
            //TODO: Refactor path caclulation, move magic numbers to a style
            String labelStr = (String) item.value.toString();
            // calculate the label bounds and adjust to center the label
            Rect textBounds = new Rect();
            paint.getTextBounds(labelStr, 0, labelStr.length(), textBounds);
            float vCenterTextBounds = textBounds.height() / 2;
            int slot = item.index;
            float spacing = 17.0f;
            float maxLength = 300.0f;
            float startX = rim.getBounds().exactCenterX() + ((rim.getBounds().width() / 2 + spacing) * (float) Math.sin(intervalRad * slot));
            float startY = rim.getBounds().exactCenterY() - ((rim.getBounds().height() / 2 + spacing) * (float) Math.cos(intervalRad * slot));
            float endX = rim.getBounds().exactCenterX() + ((rim.getBounds().width() / 2 + maxLength) * (float) Math.sin(intervalRad * slot));
            float endY = rim.getBounds().exactCenterY() - ((rim.getBounds().height() / 2 + maxLength) * (float) Math.cos(intervalRad * slot));

            // Have text stick out radially
            Path path = new Path();
            if (intervalRad * slot < 3.14) {
                paint.setTextAlign(Paint.Align.LEFT);
                path.setLastPoint(startX, startY);
                path.lineTo(endX, endY);
            } else {
                // flip the label path if it's on the left side
                paint.setTextAlign(Paint.Align.RIGHT);
                path.setLastPoint(endX, endY);
                path.lineTo(startX, startY);
            }
            // draw the text
            canvas.drawTextOnPath(labelStr, path, 0, vCenterTextBounds, paint);
        }
    }

    /**
     * Draws the chords from the model that represent a match between items.
     * @param canvas - the canvas to draw into
     * @param rim - the ShapeDrawable of the chordgraph rim
     * @param intervalRad - the interval in radians between each slot
     */
    private void drawItemsChords(Canvas canvas, ShapeDrawable rim, float intervalRad) {
        // setup the paint style
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);

        // iterate through the model chords and draw the arcs
        for (ChordGraphModel.Chord chord : model.getChords()) {
            int startSlot = ((ChordGraphModel.ChordGraphItem) chord.itemTuple.first).index;
            float startX = rim.getBounds().exactCenterX() + ((rim.getBounds().width() / 2) * (float) Math.sin(intervalRad * startSlot));
            float startY = rim.getBounds().exactCenterY() - ((rim.getBounds().height() / 2) * (float) Math.cos(intervalRad * startSlot));
            int endSlot = ((ChordGraphModel.ChordGraphItem) chord.itemTuple.second).index;
            float endX = rim.getBounds().exactCenterX() + ((rim.getBounds().width() / 2) * (float) Math.sin(intervalRad * endSlot));
            float endY = rim.getBounds().exactCenterY() - ((rim.getBounds().height() / 2) * (float) Math.cos(intervalRad * endSlot));

            // create the Path, add the first point, and add he quadratic Bezier Curve
            Path path = new Path();
            path.setLastPoint(startX, startY);
            path.quadTo(rim.getBounds().exactCenterX(), rim.getBounds().exactCenterY(), endX, endY);

            // get the category String value
            ChordGraphModel.ChordGraphCategory cat = this.model.getChordCategories().get(
                    ((ChordGraphModel.ChordGraphItem) chord.itemTuple.first).value.toString());
            // get the color associated with this category String
            Integer color = getColorForValue(cat.distinctValue);
            paint.setColor(color);

            //TODO: Could move this to a table - if this had to be realtime we would optimize a lot of stuff
            // set the alpha value based on the number of connections, limit if more than 9
            int chordCount = cat.count>9?9:cat.count;
            paint.setAlpha(255 - chordCount*25);

            // draw the path
            canvas.drawPath(path, paint);
        }
    }

    /**
     * Gets the color value for the provided string
     * @param value - the String value of the item to find the color of
     * @return the integer value of the Color
     */
    private Integer getColorForValue(String value){
        // default color
        Integer color = Color.DKGRAY;

        if(model != null && distinctValueColors != null && colorPalette != null){
            // get the color if there is one
            color = distinctValueColors.get(value);
            if(color == null){
                // if there is no color, get a new random one
                color = colorPalette.getColor();
                // TODO: this is unbounded but for this small app it is acceptable
                distinctValueColors.put(value, color);
            }
        }
        return color;
    }

    //TODO Move items into this class for handling touch events
    //TODO add a large hitbox
    private class ChordGraphViewItem {
        public ChordGraphViewItem() { }
        public void draw(Canvas canvas) { }
    }
}
