package com.arclogbook.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.arclogbook.R
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeColor
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.shadow.Shadow
import androidx.compose.ui.geometry.Offset

val JetBrainsMono = FontFamily(Font(R.font.jetbrains_mono_regular))
val FiraMono = FontFamily(Font(R.font.fira_mono_regular))
val RobotoSlab = FontFamily(Font(R.font.roboto_slab_regular))
val Montserrat = FontFamily(Font(R.font.montserrat_regular))
val Minimalistic = FontFamily(Font(R.font.minimalistic))
val Classic = FontFamily(Font(R.font.classic))
val Orbitron = FontFamily(Font(R.font.orbitron_regular))
val Audiowide = FontFamily(Font(R.font.audiowide_regular))
val Inter = FontFamily(Font(R.font.inter_regular))
val SFPro = FontFamily(Font(R.font.sfpro_regular))

val cyberpunkTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = JetBrainsMono,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = Color(0xFF00FFFF)
    ),
    titleLarge = TextStyle(
        fontFamily = JetBrainsMono,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        color = Color(0xFF39FF14)
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

val ArcLogbookTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = Orbitron,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        color = Color(0xFF00FFFF)
    ),
    titleLarge = TextStyle(
        fontFamily = Orbitron,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        color = Color(0xFF00FFFF)
    ),
    bodyLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = Color.White
    ),
    labelSmall = TextStyle(
        fontFamily = SFPro,
        fontWeight = FontWeight.Light,
        fontSize = 12.sp,
        color = Color.Cyan
    )
    // ...other styles...
)

fun getTypographyForFont(font: String): Typography {
    return when (font) {
        "Orbitron" -> Typography(
            displayLarge = TextStyle(fontFamily = Orbitron, fontWeight = FontWeight.Bold, fontSize = 36.sp, color = Color(0xFF00FFFF)),
            titleLarge = TextStyle(fontFamily = Orbitron, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Color(0xFF00FFFF)),
            bodyLarge = TextStyle(fontFamily = Orbitron, fontWeight = FontWeight.Normal, fontSize = 16.sp, color = Color.White),
            labelSmall = TextStyle(fontFamily = Orbitron, fontWeight = FontWeight.Light, fontSize = 12.sp, color = Color.Cyan)
        )
        "Exo" -> Typography(
            displayLarge = TextStyle(fontFamily = FontFamily(Font(R.font.exo_regular)), fontWeight = FontWeight.Bold, fontSize = 36.sp, color = Color(0xFF00FFFF)),
            titleLarge = TextStyle(fontFamily = FontFamily(Font(R.font.exo_regular)), fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Color(0xFF00FFFF)),
            bodyLarge = TextStyle(fontFamily = FontFamily(Font(R.font.exo_regular)), fontWeight = FontWeight.Normal, fontSize = 16.sp, color = Color.White),
            labelSmall = TextStyle(fontFamily = FontFamily(Font(R.font.exo_regular)), fontWeight = FontWeight.Light, fontSize = 12.sp, color = Color.Cyan)
        )
        "Audiowide" -> Typography(
            displayLarge = TextStyle(fontFamily = Audiowide, fontWeight = FontWeight.Bold, fontSize = 36.sp, color = Color(0xFF00FFFF)),
            titleLarge = TextStyle(fontFamily = Audiowide, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Color(0xFF00FFFF)),
            bodyLarge = TextStyle(fontFamily = Audiowide, fontWeight = FontWeight.Normal, fontSize = 16.sp, color = Color.White),
            labelSmall = TextStyle(fontFamily = Audiowide, fontWeight = FontWeight.Light, fontSize = 12.sp, color = Color.Cyan)
        )
        "Rajdhani" -> Typography(
            displayLarge = TextStyle(fontFamily = FontFamily(Font(R.font.rajdhani_regular)), fontWeight = FontWeight.Bold, fontSize = 36.sp, color = Color(0xFF00FFFF)),
            titleLarge = TextStyle(fontFamily = FontFamily(Font(R.font.rajdhani_regular)), fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Color(0xFF00FFFF)),
            bodyLarge = TextStyle(fontFamily = FontFamily(Font(R.font.rajdhani_regular)), fontWeight = FontWeight.Normal, fontSize = 16.sp, color = Color.White),
            labelSmall = TextStyle(fontFamily = FontFamily(Font(R.font.rajdhani_regular)), fontWeight = FontWeight.Light, fontSize = 12.sp, color = Color.Cyan)
        )
        "SF Pro" -> Typography(
            displayLarge = TextStyle(fontFamily = SFPro, fontWeight = FontWeight.Bold, fontSize = 36.sp, color = Color(0xFF00FFFF)),
            titleLarge = TextStyle(fontFamily = SFPro, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Color(0xFF00FFFF)),
            bodyLarge = TextStyle(fontFamily = SFPro, fontWeight = FontWeight.Normal, fontSize = 16.sp, color = Color.White),
            labelSmall = TextStyle(fontFamily = SFPro, fontWeight = FontWeight.Light, fontSize = 12.sp, color = Color.Cyan)
        )
        else -> ArcLogbookTypography
    }
}
