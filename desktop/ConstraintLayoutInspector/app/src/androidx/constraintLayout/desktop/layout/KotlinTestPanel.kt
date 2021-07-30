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

import androidx.constraintLayout.desktop.layout.OldConstraintLayout
import androidx.constraintLayout.desktop.layout.ConstraintSet
import javax.swing.*

class KotlinTestPanel : JPanel(OldConstraintLayout()) {
    init {
        val button = JButton("Hello World")
        val label = JLabel("ConstraintLayout")
        val label2 = JLabel("In Swing!")

        ConstraintSet(this) {
            Constraint(button) {
                widthBehavior = androidx.constraintLayout.desktop.layout.Constraint.Behavior.MATCH_PARENT
                centerVertically()
            }

            Constraint(label) {
                centerHorizontally(button)
                bottomToTop(button, 100)
            }

            Constraint(label2) {
                rightToRight(margin = 20)
                bottomToBottom(margin = 20)
            }
        }
    }
}

fun main(args: Array<String>) {
    val f = JFrame("ConstraintLayout Test")
    val p = KotlinTestPanel()
    f.contentPane = p
    f.setBounds(100, 100, 800, 800)
    f.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
    f.isVisible = true
}
