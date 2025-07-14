package com.arclogbook.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.arclogbook.R

val JetBrainsMono = FontFamily(Font(R.font.jetbrains_mono_regular))
val FiraMono = FontFamily(Font(R.font.fira_mono_regular))
val RobotoSlab = FontFamily(Font(R.font.roboto_slab_regular))
val Montserrat = FontFamily(Font(R.font.montserrat_regular))
val Minimalistic = FontFamily(Font(R.font.minimalistic))
val Classic = FontFamily(Font(R.font.classic))

val cyberpunkTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = JetBrainsMono,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    titleLarge = TextStyle(
        fontFamily = JetBrainsMono,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    )
    // Add more styles as needed
)

val minimalisticTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Minimalistic,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    titleLarge = TextStyle(
        fontFamily = Minimalistic,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    )
)

val classicTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Classic,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    titleLarge = TextStyle(
        fontFamily = Classic,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    )
)
