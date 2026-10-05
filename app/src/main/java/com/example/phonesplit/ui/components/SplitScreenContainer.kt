package com.example.phonesplit.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

enum class SplitOrientation {
    VERTICAL,   // Left-Right split (side-by-side)
    HORIZONTAL  // Top-Bottom split (stacked)
}

@Composable
fun SplitScreenContainer(
    modifier: Modifier = Modifier,
    orientation: SplitOrientation = SplitOrientation.VERTICAL,
    initialSplitRatio: Float = 0.5f,
    paneA: @Composable () -> Unit,
    paneB: @Composable () -> Unit
) {
    var splitRatio by remember(orientation) { mutableFloatStateOf(initialSplitRatio) }

    BoxWithConstraints(
        modifier = modifier.fillMaxSize()
    ) {
        val maxWidth = constraints.maxWidth.toFloat()
        val maxHeight = constraints.maxHeight.toFloat()

        if (orientation == SplitOrientation.VERTICAL) {
            // Left-Right split
            val paneAWidth = (maxWidth * splitRatio).coerceIn(100f, maxWidth - 100f)
            val dividerWidth = 12.dp

            Row(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(LocalDensity.current.run { paneAWidth.toDp() })
                ) {
                    paneA()
                }

                // Draggable Divider
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(dividerWidth)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .pointerInput(Unit) {
                            detectDragGestures { change, dragAmount ->
                                change.consume()
                                val newWidth = paneAWidth + dragAmount.x
                                splitRatio = (newWidth / maxWidth).coerceIn(0.2f, 0.8f)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight(0.2f)
                            .width(4.dp)
                            .background(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                                shape = MaterialTheme.shapes.small
                            )
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                ) {
                    paneB()
                }
            }
        } else {
            // Top-Bottom split
            val paneAHeight = (maxHeight * splitRatio).coerceIn(100f, maxHeight - 100f)
            val dividerHeight = 12.dp

            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(LocalDensity.current.run { paneAHeight.toDp() })
                ) {
                    paneA()
                }

                // Draggable Divider
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dividerHeight)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .pointerInput(Unit) {
                            detectDragGestures { change, dragAmount ->
                                change.consume()
                                val newHeight = paneAHeight + dragAmount.y
                                splitRatio = (newHeight / maxHeight).coerceIn(0.2f, 0.8f)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.2f)
                            .height(4.dp)
                            .background(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                                shape = MaterialTheme.shapes.small
                            )
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    paneB()
                }
            }
        }
    }
}
