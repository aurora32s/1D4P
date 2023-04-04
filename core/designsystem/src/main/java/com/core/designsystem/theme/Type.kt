package com.core.designsystem.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.core.designsystem.R

private val BM = FontFamily(
    Font(R.font.bm_dohyeon, weight = FontWeight.Bold),
    Font(R.font.bm_hanna_pro, weight = FontWeight.Medium),
    Font(R.font.bm_hanna_air, weight = FontWeight.Light)
)

val Typography = Typography(
    h2 = TextStyle(
        fontFamily = BM,
        fontSize = 60.sp,
        fontWeight = FontWeight.Bold
    ),
    h4 = TextStyle(
        fontFamily = BM,
        fontSize = 33.sp,
        fontWeight = FontWeight.Bold
    ),
    h5 = TextStyle(
        fontFamily = BM,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    ),
    h6 = TextStyle(
        fontFamily = BM,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    ),
    subtitle1 = TextStyle(
        fontFamily = BM,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium
    ),
    body1 = TextStyle(
        fontFamily = BM,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium
    ),
    body2 = TextStyle(
        fontFamily = BM,
        fontSize = 10.sp,
        fontWeight = FontWeight.Light
    )
)