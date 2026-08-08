package com.example.ui.components.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AutoCommentItem
import com.example.repository.AnalyticsRepository
import androidx.compose.foundation.BorderStroke
import com.example.ui.theme.BentoBorder
import com.example.ui.theme.BentoTextSecondary
import com.example.ui.theme.InstaBlue
import com.example.ui.theme.InstaPink
import com.example.ui.theme.MetricGreen
import com.example.ui.theme.OnDarkTextSecondary
import kotlinx.coroutines.launch

@Composable
fun AutoCommentScreen(
    repository: AnalyticsRepository,
    modifier: Modifier = Modifier
) {
    val autoCommentsList by repository.autoComments.collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()

    var postTitleInput by remember { mutableStateOf("") }
    var commentTextInput by remember { mutableStateOf("") }
    var triggerKeywordInput by remember { mutableStateOf("Collab") }
    var isReelTarget by remember { mutableStateOf(true) }

    val presetTemplates = listOf(
        "🔥 Incredible content! DM us for a collab! 📩",
        "Super creative! Sent details to your inbox! 🚀",
        "Check out our link in bio for full details! 🔗",
        "Love this aesthetic! 👏"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .testTag("auto_comment_screen")
    ) {
        Text(
            text = "Automated Comment Bot",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Set up automated, pre-scheduled comments & keyword auto-replies for Posts and Reels",
            fontSize = 12.sp,
            color = OnDarkTextSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Schedule Form Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Bot",
                            tint = InstaPink,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "New Scheduled Auto-Comment",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (isReelTarget) "Reel" else "Post",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = InstaPink
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Switch(
                            checked = isReelTarget,
                            onCheckedChange = { isReelTarget = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = androidx.compose.ui.graphics.Color.White,
                                checkedTrackColor = InstaPink
                            ),
                            modifier = Modifier.testTag("reel_post_switch")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = postTitleInput,
                    onValueChange = { postTitleInput = it },
                    label = { Text("Target Post or Reel Title / ID", fontSize = 12.sp) },
                    placeholder = { Text("e.g. Sunset Vibes Reel #04", fontSize = 12.sp) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("target_post_title_input"),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = InstaPink,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = triggerKeywordInput,
                    onValueChange = { triggerKeywordInput = it },
                    label = { Text("Trigger Keyword (Optional)", fontSize = 12.sp) },
                    placeholder = { Text("e.g. Collab, Price, Link", fontSize = 12.sp) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("trigger_keyword_input"),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = InstaPink,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = commentTextInput,
                    onValueChange = { commentTextInput = it },
                    label = { Text("Automated Comment Text", fontSize = 12.sp) },
                    placeholder = { Text("Enter comment or select preset below...", fontSize = 12.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                        .testTag("comment_text_input"),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = InstaPink,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Presets horizontal list
                Text(text = "Quick Presets:", fontSize = 11.sp, color = OnDarkTextSecondary)
                Spacer(modifier = Modifier.height(4.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(presetTemplates) { template ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { commentTextInput = template }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = template,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        if (postTitleInput.isNotEmpty() && commentTextInput.isNotEmpty()) {
                            scope.launch {
                                repository.addAutoComment(
                                    targetPostTitle = postTitleInput,
                                    commentText = commentTextInput,
                                    scheduledTimeFormatted = "Every 2 Hours",
                                    triggerKeyword = triggerKeywordInput,
                                    isReel = isReelTarget
                                )
                                postTitleInput = ""
                                commentTextInput = ""
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("schedule_comment_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = InstaPink),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Schedule Automated Comment Bot", fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Active Automation Schedules (${autoCommentsList.size})",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (autoCommentsList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = "Empty",
                        tint = OnDarkTextSecondary,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No scheduled comments yet.",
                        fontSize = 13.sp,
                        color = OnDarkTextSecondary
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(autoCommentsList, key = { it.id }) { item ->
                    AutoCommentCard(
                        item = item,
                        onToggle = {
                            scope.launch {
                                repository.toggleAutoCommentStatus(item.id, item.status)
                            }
                        },
                        onDelete = {
                            scope.launch {
                                repository.deleteAutoComment(item.id)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun AutoCommentCard(
    item: AutoCommentItem,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, BentoBorder)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (item.isReel) InstaPink else InstaBlue)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (item.isReel) "REEL" else "POST",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = androidx.compose.ui.graphics.Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = item.targetPostTitle,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "\"${item.commentText}\"",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Trigger: ${item.triggerKeyword} • Schedule: ${item.scheduledTimeFormatted}",
                        fontSize = 11.sp,
                        color = OnDarkTextSecondary
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (item.status == "Scheduled") MetricGreen.copy(alpha = 0.15f)
                                else OnDarkTextSecondary.copy(alpha = 0.15f)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = item.status,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (item.status == "Scheduled") MetricGreen else OnDarkTextSecondary
                        )
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onToggle) {
                    Icon(
                        imageVector = if (item.status == "Scheduled") Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Toggle",
                        tint = InstaPink
                    )
                }

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = OnDarkTextSecondary
                    )
                }
            }
        }
    }
}
