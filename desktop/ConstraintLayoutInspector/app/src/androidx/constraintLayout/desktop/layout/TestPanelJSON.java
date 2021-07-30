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

import javax.swing.*;

public class TestPanelJSON extends JPanel {

    public TestPanelJSON() {
        super (new ConstraintLayout("""
        {
            Header: { exportAs: 'SwingDemo'},
            g1: { type: 'vGuideline', percent: 0.3 },
            button: {
                width: 'parent',
                centerVertically: 'parent'
            },
            text1: {
                start: ['g1', 'start'],
                bottom: ['button', 'bottom', 100]
            },
            text2: {
                end: ['parent','end', 20],
                bottom: ['parent','bottom', 20]
            }            
        }
        """));

        JButton button = new JButton("Hello World");
        add(button, "button");
        add(new JLabel("ConstraintLayout"), "text1");
        add(new JLabel("In Swing!"), "text2");
    }

    public static void main(String[] args) {
        JFrame f = new JFrame("ConstraintLayout Test");
        TestPanelJSON p = new TestPanelJSON();
        f.setContentPane(p);
        f.setBounds(100, 100, 800, 800);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        LinkServer server = new LinkServer();
        server.start();
        f.setVisible(true);
    }

}
