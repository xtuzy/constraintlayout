package androidx.constraintLayout.desktop.layout;/*
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

import org.intellij.lang.annotations.Language;

import javax.swing.*;

public class TestPanel extends JPanel {

    public TestPanel() {
        super (new ConstraintLayout("""
        {
            Header: { exportAs: 'SwingDemo'},
            button: {
                width: 'parent',
                centerVertically: 'parent'
            },
            text1: {
                start: ['button', 'start'],
                end: ['button', 'end'],
                bottom: ['button', 'bottom', 100]
            },
            text2: {
                end: ['parent','end', 20],
                bottom: ['parent','bottom', 20]
            }            
        }
        """));
//        ConstraintLayout layout = (ConstraintLayout) getLayout();
//        ConstraintWidgetContainer parent = layout.getRoot();


        JButton button = new JButton("Hello World");
        add(button, "button");
        add(new JLabel("ConstraintLayout"), "text1");
        add(new JLabel("In Swing!"), "text2");


//        ConstraintWidget constraint = new ConstraintWidget(200, 40);
//        constraint.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_PARENT);
//        constraint.connect(ConstraintAnchor.Type.TOP, parent, ConstraintAnchor.Type.TOP, 0);
//        constraint.connect(ConstraintAnchor.Type.BOTTOM, parent, ConstraintAnchor.Type.BOTTOM, 0);
//        add(button, constraint);

//        ConstraintWidget constraint2 = new ConstraintWidget(200, 40);
//        constraint2.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.WRAP_CONTENT);
//        constraint2.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.WRAP_CONTENT);
//        constraint2.connect(ConstraintAnchor.Type.LEFT, constraint, ConstraintAnchor.Type.LEFT);
//        constraint2.connect(ConstraintAnchor.Type.RIGHT, constraint, ConstraintAnchor.Type.RIGHT);
//        constraint2.connect(ConstraintAnchor.Type.BOTTOM, constraint, ConstraintAnchor.Type.TOP, 100);
//        add(new JLabel("ConstraintLayout"), constraint2);

//        ConstraintWidget constraint3 = new ConstraintWidget(100, 40);
//        constraint3.connect(ConstraintAnchor.Type.RIGHT, parent, ConstraintAnchor.Type.RIGHT, 20);
//        constraint3.connect(ConstraintAnchor.Type.BOTTOM, parent, ConstraintAnchor.Type.BOTTOM, 20);
//        add(new JLabel("In Swing!"), constraint3);
    }

    private void JSONConstraintSet(@Language("JSON5") String s) {

    }

    public static void main(String[] args) {
        JFrame f = new JFrame("ConstraintLayout Test");
        TestPanel p = new TestPanel();
        f.setContentPane(p);
        f.setBounds(100, 100, 800, 800);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

}
