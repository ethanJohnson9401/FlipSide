package com.ethanjohnson.flipside.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val FlipSideShapes = Shapes(

    // Tags, tiny controls
    extraSmall = RoundedCornerShape(6.dp),

    // Inputs, smaller controls
    small = RoundedCornerShape(10.dp),

    // Buttons / compact cards
    medium = RoundedCornerShape(14.dp),

    // Main media cards
    large = RoundedCornerShape(18.dp),

    // Bottom sheets / large surfaces
    extraLarge = RoundedCornerShape(24.dp)
)