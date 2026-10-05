package com.example.phonesplit.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.phonesplit.ui.modules.ChatMessagingModule
import com.example.phonesplit.ui.modules.VideoPlayerModule
import com.example.phonesplit.ui.components.SplitOrientation
import com.example.phonesplit.ui.components.SplitScreenContainer
import com.example.phonesplit.ui.theme.VideoPlayerTheme
import com.example.phonesplit.ui.theme.ChatMessagingTheme

enum class PaneModuleType {
    VIDEO_PLAYER,
    CHAT_MESSAGING
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainSplitScreen() {
    var orientation by remember { mutableStateOf(SplitOrientation.VERTICAL) }
    var swapped by remember { mutableStateOf(false) }

    // Module assignment per pane
    var paneAModule by remember { mutableStateOf(PaneModuleType.VIDEO_PLAYER) }
    var paneBModule by remember { mutableStateOf(PaneModuleType.CHAT_MESSAGING) }

    // Independent per-pane brightness/dimming states (0.0f = 0% dim, 0.7f = 70% dim)
    var paneADimAlpha by remember { mutableFloatStateOf(0.0f) }
    var paneBDimAlpha by remember { mutableFloatStateOf(0.0f) }

    // Brightness slider visibility popup per pane
    var showPaneABrightnessSlider by remember { mutableStateOf(false) }
    var showPaneBBrightnessSlider by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "PhoneSplit Studio",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (orientation == SplitOrientation.VERTICAL) "Vertical Split (Side-by-Side)" else "Horizontal Split (Stacked)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    // Swap Panes Button
                    IconButton(onClick = { 
                        swapped = !swapped 
                        val tempMod = paneAModule
                        paneAModule = paneBModule
                        paneBModule = tempMod
                        val tempDim = paneADimAlpha
                        paneADimAlpha = paneBDimAlpha
                        paneBDimAlpha = tempDim
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.SwapHoriz,
                            contentDescription = "Swap Panes"
                        )
                    }
                    // Orientation Toggle Button
                    IconButton(onClick = {
                        orientation = if (orientation == SplitOrientation.VERTICAL) {
                            SplitOrientation.HORIZONTAL
                        } else {
                            SplitOrientation.VERTICAL
                        }
                    }) {
                        Icon(
                            imageVector = if (orientation == SplitOrientation.VERTICAL) {
                                Icons.Rounded.ViewAgenda
                            } else {
                                Icons.Rounded.ViewColumn
                            },
                            contentDescription = "Toggle Split Orientation"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val contentA: @Composable () -> Unit = {
                PaneContainerWrapper(
                    paneTitle = if (paneAModule == PaneModuleType.VIDEO_PLAYER) "Pane A: Video Player" else "Pane A: Chat & Messaging",
                    moduleType = paneAModule,
                    onToggleModule = {
                        paneAModule = if (paneAModule == PaneModuleType.VIDEO_PLAYER) PaneModuleType.CHAT_MESSAGING else PaneModuleType.VIDEO_PLAYER
                    },
                    dimAlpha = paneADimAlpha,
                    onDimAlphaChanged = { paneADimAlpha = it },
                    showBrightnessSlider = showPaneABrightnessSlider,
                    onToggleBrightnessSlider = { showPaneABrightnessSlider = !showPaneABrightnessSlider }
                )
            }

            val contentB: @Composable () -> Unit = {
                PaneContainerWrapper(
                    paneTitle = if (paneBModule == PaneModuleType.VIDEO_PLAYER) "Pane B: Video Player" else "Pane B: Chat & Messaging",
                    moduleType = paneBModule,
                    onToggleModule = {
                        paneBModule = if (paneBModule == PaneModuleType.VIDEO_PLAYER) PaneModuleType.CHAT_MESSAGING else PaneModuleType.VIDEO_PLAYER
                    },
                    dimAlpha = paneBDimAlpha,
                    onDimAlphaChanged = { paneBDimAlpha = it },
                    showBrightnessSlider = showPaneBBrightnessSlider,
                    onToggleBrightnessSlider = { showPaneBBrightnessSlider = !showPaneBBrightnessSlider }
                )
            }

            if (!swapped) {
                SplitScreenContainer(
                    orientation = orientation,
                    paneA = contentA,
                    paneB = contentB
                )
            } else {
                SplitScreenContainer(
                    orientation = orientation,
                    paneA = contentB,
                    paneB = contentA
                )
            }
        }
    }
}

@Composable
fun PaneContainerWrapper(
    paneTitle: String,
    moduleType: PaneModuleType,
    onToggleModule: () -> Unit,
    dimAlpha: Float,
    onDimAlphaChanged: (Float) -> Unit,
    showBrightnessSlider: Boolean,
    onToggleBrightnessSlider: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Pane Header Bar with Independent Brightness & Switch controls
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f),
                    tonalElevation = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (moduleType == PaneModuleType.VIDEO_PLAYER) Icons.Rounded.PlayCircle else Icons.Rounded.Chat,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = paneTitle,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Brightness / Dimming Toggle Button
                                IconButton(onClick = onToggleBrightnessSlider) {
                                    Icon(
                                        imageVector = if (dimAlpha > 0f) Icons.Rounded.BrightnessMedium else Icons.Rounded.BrightnessHigh,
                                        contentDescription = "Per-Pane Brightness & Dimming",
                                        tint = if (dimAlpha > 0f) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                // Switch Module Button
                                IconButton(onClick = onToggleModule) {
                                    Icon(
                                        imageVector = Icons.Rounded.SwapHoriz,
                                        contentDescription = "Switch Module",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        // Expandable Brightness & Dimming Slider (0% to 70% dimming)
                        AnimatedVisibility(visible = showBrightnessSlider) {
                            Surface(
                                color = MaterialTheme.colorScheme.surface,
                                tonalElevation = 3.dp,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Dimming Level: ${(dimAlpha * 100f).toInt()}%",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        TextButton(onClick = { onDimAlphaChanged(0f) }) {
                                            Text("Reset")
                                        }
                                    }
                                    Slider(
                                        value = dimAlpha,
                                        onValueChange = onDimAlphaChanged,
                                        valueRange = 0.0f..0.7f,
                                        colors = SliderDefaults.colors(
                                            thumbColor = MaterialTheme.colorScheme.primary,
                                            activeTrackColor = MaterialTheme.colorScheme.primary
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                // Module Content Area (Dual Themed: Video Player gets Dark Theme + Neon Blue; Chat gets Light Theme + Pastel Orange)
                Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                    when (moduleType) {
                        PaneModuleType.VIDEO_PLAYER -> VideoPlayerTheme { VideoPlayerModule() }
                        PaneModuleType.CHAT_MESSAGING -> ChatMessagingTheme { ChatMessagingModule() }
                    }
                }
            }

            // Independent Per-Pane Dimming Overlay (alpha 0% to 70%)
            if (dimAlpha > 0f) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = dimAlpha))
                ) {
                    // Optional indicator when heavily dimmed
                    if (dimAlpha >= 0.4f) {
                        Surface(
                            color = Color.Black.copy(alpha = 0.6f),
                            shape = MaterialTheme.shapes.small,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.BrightnessLow,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Dimmed (${(dimAlpha * 100f).toInt()}%)",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
