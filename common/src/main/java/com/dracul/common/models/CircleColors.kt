package com.dracul.common.models

import androidx.compose.runtime.Stable


@Stable
data class CircleColor(val color: Int, val selected: Boolean = false)

class CircleColorList() {
    private val list = listOf(
        CircleColor(0),
        CircleColor(1),
        CircleColor(2),
        CircleColor(3),
        CircleColor(4),
        CircleColor(5),
        CircleColor(6),
        CircleColor(7),
        CircleColor(8),
        CircleColor(9),
        CircleColor(10),
        CircleColor(11),
    )

    fun getColors(): List<CircleColor> {
        return list
    }

    fun getSelected(noteColor: CircleColor): List<CircleColor> {
        return list.map {
            if (it.color == noteColor.color) it.copy(selected = true)
            else it
        }.toList()
    }
    fun getSelected(noteColor: Int): List<CircleColor> {
        return list.map {
            if (it.color == noteColor) it.copy(selected = true)
            else it
        }.toList()
    }
}
