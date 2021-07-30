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

package androidx.constraintLayout.desktop.layout

import androidx.constraintlayout.core.widgets.ConstraintAnchor
import androidx.constraintlayout.core.widgets.ConstraintWidget
import java.awt.Component
import javax.swing.JPanel

/**
 * Utility class to more simply express constraints
 */
class ConstraintSet(private val container: JPanel, function: ConstraintSet.() -> Unit) {
    private val layout : OldConstraintLayout = (container.layout as OldConstraintLayout)
    private val allConstraints = ArrayList<Constraint>()

    init {
        function()
        addAllWidgets()
    }

    fun Constraint(component : Component, constraints: Constraint.() -> Unit) {
        var constraint = Constraint(container, component, constraints)
        allConstraints.add(constraint)
    }

    private fun addAllWidgets() {
        var map : HashMap<Component, ConstraintWidget> = HashMap()
        var root = layout.root
        map[container] = root
        // let's create all the ConstraintWidget first
        for (constraint : Constraint in allConstraints) {
            constraint.createWidget(map)
            container.add(constraint.component, constraint.widget)
            root.add(constraint.widget)
        }
        // now th at the map contains the ConstraintWidget we can safely
        // apply the Constraints.
        for (constraint : Constraint in allConstraints) {
            constraint.apply(map)
        }
    }

}

/**
 * Utility class representing a set of constraints for a single Component
 */
class Constraint(container: Component, component: Component, function: (Constraint) -> Unit) {

    private val function = function
    private val container = container

    enum class Behavior { FIXED, WRAP_CONTENT, MATCH_CONSTRAINT, MATCH_PARENT }

    val component = component
    lateinit var widget: ConstraintWidget

    var widthBehavior : Behavior = Behavior.WRAP_CONTENT
    var heightBehavior : Behavior = Behavior.WRAP_CONTENT
    var width: Int = 0
    var height: Int = 0

    private var leftToLeft : Component? = null
    private var leftToRight : Component? = null
    private var rightToLeft : Component? = null
    private var rightToRight : Component? = null
    private var topToTop : Component? = null
    private var topToBottom : Component? = null
    private var bottomToTop : Component? = null
    private var bottomToBottom : Component? = null

    private var marginLeft : Int = 0
    private var marginTop : Int = 0
    private var marginRight : Int = 0
    private var marginBottom : Int = 0

    fun centerHorizontally(target : Component = container) {
        leftToLeft(target)
        rightToRight(target)
    }

    fun centerVertically(target : Component = container) {
        topToTop(target)
        bottomToBottom(target)
    }

    fun leftToLeft(target : Component = container, margin : Int = 0) {
        leftToLeft = target
        marginLeft = margin
    }

    fun leftToRight(target : Component = container, margin : Int = 0) {
        leftToRight = target
        marginLeft = margin
    }

    fun topToTop(target : Component = container, margin : Int = 0) {
        topToTop = target
        marginTop = margin
    }

    fun topToBottom(target : Component = container, margin : Int = 0) {
        topToBottom = target
        marginTop = margin
    }

    fun rightToLeft(target : Component = container, margin : Int = 0) {
        rightToLeft = target
        marginRight = margin
    }

    fun rightToRight(target : Component = container, margin : Int = 0) {
        rightToRight = target
        marginRight = margin
    }

    fun bottomToTop(target : Component = container, margin : Int = 0) {
        bottomToTop = target
        marginBottom = margin
    }

    fun bottomToBottom(target : Component = container, margin : Int = 0) {
        bottomToBottom = target
        marginBottom = margin
    }

    fun createWidget(map: HashMap<Component, ConstraintWidget>) {
        widget = ConstraintWidget(width, height)
        widget.companionWidget = component
        map[component] = widget
    }

    fun apply(map: HashMap<Component, ConstraintWidget>) {
        function(this)
        widget.width = width
        widget.height = height
        when (widthBehavior) {
            Behavior.WRAP_CONTENT -> widget.horizontalDimensionBehaviour =
                ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            Behavior.FIXED -> widget.horizontalDimensionBehaviour =
                ConstraintWidget.DimensionBehaviour.FIXED
            Behavior.MATCH_CONSTRAINT -> widget.horizontalDimensionBehaviour =
                ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            Behavior.MATCH_PARENT -> widget.horizontalDimensionBehaviour =
                ConstraintWidget.DimensionBehaviour.MATCH_PARENT
        }
        when (heightBehavior) {
            Behavior.WRAP_CONTENT -> widget.verticalDimensionBehaviour =
                ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            Behavior.FIXED -> widget.verticalDimensionBehaviour =
                ConstraintWidget.DimensionBehaviour.FIXED
            Behavior.MATCH_CONSTRAINT -> widget.verticalDimensionBehaviour =
                ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            Behavior.MATCH_PARENT -> widget.verticalDimensionBehaviour =
                ConstraintWidget.DimensionBehaviour.MATCH_PARENT
        }
        if (leftToLeft != null) {
            var target = map[leftToLeft!!]
            widget.connect(
                ConstraintAnchor.Type.LEFT, target,
                ConstraintAnchor.Type.LEFT, marginLeft)
        }
        if (leftToRight != null) {
            var target = map[leftToRight!!]
            widget.connect(
                ConstraintAnchor.Type.LEFT, target,
                ConstraintAnchor.Type.RIGHT, marginLeft)
        }
        if (topToTop != null) {
            var target = map[topToTop!!]
            widget.connect(
                ConstraintAnchor.Type.TOP, target,
                ConstraintAnchor.Type.TOP, marginTop)
        }
        if (topToBottom != null) {
            var target = map[topToBottom!!]
            widget.connect(
                ConstraintAnchor.Type.TOP, target,
                ConstraintAnchor.Type.BOTTOM, marginTop)
        }
        if (rightToLeft != null) {
            var target = map[rightToLeft!!]
            widget.connect(
                ConstraintAnchor.Type.RIGHT, target,
                ConstraintAnchor.Type.LEFT, marginRight)
        }
        if (rightToRight != null) {
            var target = map[rightToRight!!]
            widget.connect(
                ConstraintAnchor.Type.RIGHT, target,
                ConstraintAnchor.Type.RIGHT, marginRight)
        }
        if (bottomToTop != null) {
            var target = map[bottomToTop!!]
            widget.connect(
                ConstraintAnchor.Type.BOTTOM, target,
                ConstraintAnchor.Type.TOP, marginBottom)
        }
        if (bottomToBottom != null) {
            var target = map[bottomToBottom!!]
            widget.connect(
                ConstraintAnchor.Type.BOTTOM, target,
                ConstraintAnchor.Type.BOTTOM, marginBottom)
        }
    }
}