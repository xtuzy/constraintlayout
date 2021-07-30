/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.constraintLayout.desktop.layout;

import androidx.constraintlayout.core.widgets.ConstraintWidget;
import androidx.constraintlayout.core.widgets.ConstraintWidgetContainer;
import androidx.constraintlayout.core.widgets.Optimizer;
import androidx.constraintlayout.core.widgets.analyzer.BasicMeasure;

import java.awt.*;

import java.util.HashMap;

/**
 * Naive implementation of ConstraintLayout as a Swing LayoutManager
 */
public class OldConstraintLayout implements LayoutManager2, BasicMeasure.Measurer {

    private ConstraintWidgetContainer mLayout = new ConstraintWidgetContainer();
    private HashMap<Component, ConstraintWidget> mViewsToConstraints = new HashMap<>();

    public OldConstraintLayout() {
        mLayout.setMeasurer(this);
    }

    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        ConstraintWidget widget = (ConstraintWidget) constraints;
        widget.setCompanionWidget(comp);
        mViewsToConstraints.put(comp, widget);
        mLayout.add(widget);
    }

    @Override
    public Dimension maximumLayoutSize(Container target) {
        return null;
    }

    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0;
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0;
    }

    @Override
    public void invalidateLayout(Container target) {

    }

    @Override
    public void addLayoutComponent(String name, Component comp) {

    }

    @Override
    public void removeLayoutComponent(Component comp) {
        mLayout.remove(mViewsToConstraints.get(comp));
        mViewsToConstraints.remove(comp);
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return null;
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return null;
    }

    @Override
    public void layoutContainer(Container parent) {
        int width = parent.getWidth();
        int height = parent.getHeight();

        mLayout.setWidth(width);
        mLayout.setHeight(height);
        mLayout.layout();
        mLayout.measure(Optimizer.OPTIMIZATION_NONE, BasicMeasure.EXACTLY, width, BasicMeasure.EXACTLY, height,
                0, 0, 0, 0);

        for (ConstraintWidget child : mLayout.getChildren()) {
            Component component = (Component) child.getCompanionWidget();
            component.setBounds(child.getX(), child.getY(), child.getWidth(), child.getHeight());
        }
    }

    public ConstraintWidgetContainer getRoot() {
        return mLayout;
    }

    @Override
    public void measure(ConstraintWidget constraintWidget, BasicMeasure.Measure measure) {
        Component component = (Component) constraintWidget.getCompanionWidget();
        int measuredWidth = constraintWidget.getWidth();
        int measuredHeight = constraintWidget.getHeight();
        if (measure.horizontalBehavior == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
            measuredWidth = component.getMinimumSize().width;
        }
        if (measure.verticalBehavior == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
            measuredHeight = component.getMinimumSize().height;
        }
        measure.measuredWidth = measuredWidth;
        measure.measuredHeight = measuredHeight;
    }

    @Override
    public void didMeasures() {

    }
}
