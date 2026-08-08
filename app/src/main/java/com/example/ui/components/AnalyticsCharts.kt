package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.InstaBlue
import com.example.ui.theme.InstaOrange
import com.example.ui.theme.InstaPink
import com.example.ui.theme.InstaPurple
import androidx.compose.foundation.BorderStroke
import com.example.ui.theme.BentoBorder
import com.example.ui.theme.BentoTextSecondary

@Composable
fun ViewsBarChart(
    reelsData: List<Int>,
    postsData: List<Int>,
    labels: List<String>,
    modifier: Modifier = Modifier
) {
    val maxVal = (reelsData + postsData).maxOrNull()?.coerceAtLeast(100) ?: 100

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, BentoBorder, RoundedCornerShape(24.dp))
            .padding(18.dp)
            .testTag("views_bar_chart")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Views Breakdown",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Reels vs Standard Posts",
                    fontSize = 12.sp,
                    color = BentoTextSecondary
                )
            }

            // Legend
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(InstaPink)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Reels", fontSize = 11.sp, color = BentoTextSecondary)

                Spacer(modifier = Modifier.width(12.dp))

                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(InstaBlue)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Posts", fontSize = 11.sp, color = BentoTextSecondary)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Chart Canvas
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        ) {
            val width = size.width
            val height = size.height
            val itemCount = labels.size
            if (itemCount == 0) return@Canvas

            val groupWidth = width / itemCount
            val barWidth = groupWidth * 0.28f

            labels.forEachIndexed { index, _ ->
                val reelVal = reelsData.getOrElse(index) { 0 }
                val postVal = postsData.getOrElse(index) { 0 }

                val reelHeight = (reelVal.toFloat() / maxVal) * (height - 30.dp.toPx())
                val postHeight = (postVal.toFloat() / maxVal) * (height - 30.dp.toPx())

                val groupX = index * groupWidth + groupWidth * 0.15f

                // Reels bar (Pink Gradient)
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(InstaPink, InstaOrange)
                    ),
                    topLeft = Offset(groupX, height - reelHeight - 20.dp.toPx()),
                    size = Size(barWidth, reelHeight),
                    cornerRadius = CornerRadius(8f, 8f)
                )

                // Posts bar (Blue Gradient)
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(InstaBlue, InstaPurple)
                    ),
                    topLeft = Offset(groupX + barWidth + 4.dp.toPx(), height - postHeight - 20.dp.toPx()),
                    size = Size(barWidth, postHeight),
                    cornerRadius = CornerRadius(8f, 8f)
                )
            }
        }

        // X Axis Labels
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            labels.forEach { label ->
                Text(
                    text = label,
                    fontSize = 11.sp,
                    color = BentoTextSecondary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun ReachCurvedChart(
    points: List<Int>,
    labels: List<String>,
    modifier: Modifier = Modifier
) {
    val maxVal = points.maxOrNull()?.coerceAtLeast(100) ?: 100
    val minVal = points.minOrNull() ?: 0

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, BentoBorder, RoundedCornerShape(24.dp))
            .padding(18.dp)
            .testTag("reach_curved_chart")
    ) {
        Column {
            Text(
                text = "Reach Performance Trend",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Total Account Reach & Impressions Trajectory",
                fontSize = 12.sp,
                color = BentoTextSecondary
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        ) {
            val width = size.width
            val height = size.height - 24.dp.toPx()
            if (points.size < 2) return@Canvas

            val stepX = width / (points.size - 1)
            val range = (maxVal - minVal).coerceAtLeast(1)

            val offsets = points.mapIndexed { index, valPoint ->
                val normY = (valPoint - minVal).toFloat() / range
                val y = height - (normY * (height - 20.dp.toPx()))
                Offset(index * stepX, y)
            }

            // Fill gradient path under curve
            val fillPath = Path().apply {
                moveTo(0f, height)
                offsets.forEachIndexed { index, pt ->
                    if (index == 0) {
                        lineTo(pt.x, pt.y)
                    } else {
                        val prevPt = offsets[index - 1]
                        val controlPt1 = Offset((prevPt.x + pt.x) / 2, prevPt.y)
                        val controlPt2 = Offset((prevPt.x + pt.x) / 2, pt.y)
                        cubicTo(controlPt1.x, controlPt1.y, controlPt2.x, controlPt2.y, pt.x, pt.y)
                    }
                }
                lineTo(width, height)
                close()
            }

            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        InstaPink.copy(alpha = 0.35f),
                        InstaPurple.copy(alpha = 0.05f)
                    )
                )
            )

            // Line stroke path
            val strokePath = Path().apply {
                offsets.forEachIndexed { index, pt ->
                    if (index == 0) {
                        moveTo(pt.x, pt.y)
                    } else {
                        val prevPt = offsets[index - 1]
                        val controlPt1 = Offset((prevPt.x + pt.x) / 2, prevPt.y)
                        val controlPt2 = Offset((prevPt.x + pt.x) / 2, pt.y)
                        cubicTo(controlPt1.x, controlPt1.y, controlPt2.x, controlPt2.y, pt.x, pt.y)
                    }
                }
            }

            drawPath(
                path = strokePath,
                brush = Brush.horizontalGradient(
                    colors = listOf(InstaPink, InstaOrange, InstaPurple)
                ),
                style = Stroke(width = 3.dp.toPx())
            )

            // Data Points
            offsets.forEach { pt ->
                drawCircle(
                    color = Color.White,
                    radius = 4.dp.toPx(),
                    center = pt
                )
                drawCircle(
                    color = InstaPink,
                    radius = 2.5.dp.toPx(),
                    center = pt
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            labels.forEach { label ->
                Text(
                    text = label,
                    fontSize = 11.sp,
                    color = BentoTextSecondary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
