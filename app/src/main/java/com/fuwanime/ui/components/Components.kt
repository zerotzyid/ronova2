package com.fuwanime.ui.components

import com.fuwanime.util.text
import com.fuwanime.util.UiText
import androidx.compose.ui.res.stringResource
import com.fuwanime.R
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.icons.rounded.CloudOff
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.fuwanime.util.remainingLabel
import androidx.compose.ui.unit.em
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.draw.alpha
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.fuwanime.data.Anime
import com.fuwanime.data.local.ThemeMode
import com.fuwanime.ui.components.haptic.FuwaHapticFeedback
import com.fuwanime.ui.components.haptic.rememberFuwaHapticFeedback
import com.fuwanime.ui.motion.LocalReduceMotion
import com.fuwanime.ui.motion.Motion
import com.fuwanime.ui.motion.aboveSharedElements
import com.fuwanime.ui.motion.rememberSharedKey
import com.fuwanime.ui.motion.revealWhenLanded
import com.fuwanime.ui.motion.pressScale
import com.fuwanime.ui.motion.rememberStaggerState
import com.fuwanime.ui.motion.sharedEpisodeKey
import com.fuwanime.ui.motion.sharedKey
import com.fuwanime.ui.motion.sharedPosterKey
import com.fuwanime.ui.motion.staggerIn
import com.fuwanime.data.local.WatchHistoryEntity
import com.fuwanime.util.statusLabel

/** Network image with a subtle surface tint so empty slots never flash white. */
@Composable
fun NetImage(
    url: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop,
) {
    val context = LocalContext.current
    Box(modifier.background(MaterialTheme.colorScheme.surfaceContainerLow)) {
        if (url.isNotBlank()) {
            AsyncImage(
                model = ImageRequest.Builder(context).data(url).placeholderMemoryCacheKey(url).crossfade(250).build(),
                contentDescription = contentDescription,
                contentScale = contentScale,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

fun Modifier.shimmer(): Modifier = composed {
    if (LocalReduceMotion.current) background(MaterialTheme.colorScheme.surfaceContainerLow) else background(shimmerBrush())
}

@Composable
private fun shimmerBrush(): Brush {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val x by transition.animateFloat(
        initialValue = -600f,
        targetValue = 1400f,
        animationSpec = infiniteRepeatable(tween(1300, easing = LinearEasing), RepeatMode.Restart),
        label = "shimmerX",
    )
    val base = MaterialTheme.colorScheme.surfaceContainerLow
    val highlight = MaterialTheme.colorScheme.surfaceContainer
    return Brush.linearGradient(listOf(base, highlight, base), start = Offset(x, 0f), end = Offset(x + 500f, 300f))
}

@Composable
fun SkeletonBox(modifier: Modifier, shape: androidx.compose.ui.graphics.Shape = MaterialTheme.shapes.small) {
    Box(modifier.clip(shape).shimmer())
}

/** Neon pill badge — high-contrast, fully rounded. */
@Composable
fun Pill(
    text: String,
    modifier: Modifier = Modifier,
    container: Color = MaterialTheme.colorScheme.secondary,
    content: Color = MaterialTheme.colorScheme.onSecondary,
    leading: ImageVector? = null,
) {
    Row(
        modifier
            .clip(RoundedCornerShape(50))
            .background(container)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (leading != null) {
            Icon(leading, null, Modifier.size(12.dp), tint = content)
            Spacer(Modifier.width(4.dp))
        }
        Text(text, style = MaterialTheme.typography.labelSmall, color = content, maxLines = 1)
    }
}

@Composable
fun LiveDot(modifier: Modifier = Modifier) {
    Box(modifier.size(7.dp).clip(CircleShape).background(Color(0xFFFF00DE)))
}

/** Poster card with opensauce styling: deep surface, thin neon border when selected. */
@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalFoundationApi::class)
@Composable
fun PosterCard(
    anime: Anime,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    width: Dp? = null,
    caption: String? = null,
    sharedOrigin: String? = null,
    isNew: Boolean = false,
    highlighted: Boolean = false,
    badge: String? = null,
    onLongClick: (() -> Unit)? = null,
    selected: Boolean = false,
    longClickLabel: String = stringResource(R.string.common_select),
) {
    val interaction = remember { MutableInteractionSource() }
    val res = LocalContext.current.resources
    val haptic = rememberFuwaHapticFeedback()
    val marked = highlighted || selected
    Column(
        (if (width != null) modifier.width(width) else modifier)
            .pressScale(interaction)
            .clip(MaterialTheme.shapes.small)
            .combinedClickable(
                interactionSource = interaction,
                indication = LocalIndication.current,
                onLongClick = onLongClick,
                onClick = {
                    haptic.performClick()
                    onClick()
                },
            )
            .semantics(mergeDescendants = true) {
                contentDescription = posterDescription(res, anime.title, caption, badge, isNew, anime.isOngoing, selected)
                if (onLongClick != null) customActions = listOf(CustomAccessibilityAction(longClickLabel) { onLongClick(); true })
            },
    ) {
        val poster = rememberSharedKey(sharedOrigin?.let { sharedPosterKey(anime.id, it) })
        Box(
            Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 3f)
                .clip(MaterialTheme.shapes.small)
                .then(if (marked) Modifier.border(2.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.small) else Modifier),
        ) {
            NetImage(
                anime.poster,
                Modifier.fillMaxSize().alpha(if (marked) 0.55f else 1f).sharedKey(poster).clip(MaterialTheme.shapes.small),
                null,
            )
            val cornerBadge = Modifier.align(Alignment.TopStart).aboveSharedElements().revealWhenLanded(poster).padding(8.dp)
            when {
                isNew -> NewBadge(cornerBadge)
                anime.isOngoing -> Row(
                    cornerBadge
                        .clip(RoundedCornerShape(50))
                        .background(ImageScrim.copy(alpha = 0.85f))
                        .padding(start = 7.dp, end = 8.dp, top = 4.dp, bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    LiveDot()
                    Spacer(Modifier.width(5.dp))
                    Text(stringResource(R.string.common_status_airing), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
            if (badge != null && !selected) {
                Text(
                    badge,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .aboveSharedElements()
                        .revealWhenLanded(poster)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(50))
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                )
            }
            if (selected) {
                Icon(
                    Icons.Rounded.CheckCircle,
                    contentDescription = stringResource(R.string.common_selected_cd),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.Center).size(36.dp),
                )
            }
        }
        Text(
            anime.title,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 2,
            minLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = 6.dp, end = 6.dp, top = 10.dp),
        )
        val meta = caption ?: listOf(anime.type, anime.displayYear).filter { it.isNotBlank() }.joinToString(" · ")
        if (meta.isNotBlank()) {
            Text(
                meta,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = if (highlighted) FontWeight.Bold else null,
                color = if (highlighted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 6.dp),
            )
        }
    }
}

/** Near-black used for scrims and pills drawn on top of artwork. */
val ImageScrim = Color(0xFF050505)

@Composable
fun NewBadge(modifier: Modifier = Modifier) {
    Text(
        stringResource(R.string.common_new_upper),
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = 0.08.em,
        color = MaterialTheme.colorScheme.onPrimary,
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 8.dp, vertical = 2.dp),
    )
}

/** Rounded filter/genre chip — pill-shaped, neon when selected. */
@Composable
fun TagChip(label: String, onClick: () -> Unit, modifier: Modifier = Modifier, selected: Boolean = false) {
    val colors = MaterialTheme.colorScheme
    val haptic = rememberFuwaHapticFeedback()
    Surface(
        selected = selected,
        onClick = {
            haptic.performClick()
            onClick()
        },
        modifier = modifier.defaultMinSize(minHeight = 40.dp),
        shape = RoundedCornerShape(50),
        color = if (selected) colors.primary else colors.surfaceContainerLow,
        contentColor = if (selected) colors.onPrimary else colors.onSurface,
        border = if (selected) null else BorderStroke(1.dp, colors.outlineVariant),
    ) {
        Box(Modifier.padding(horizontal = 16.dp), contentAlignment = Alignment.Center) {
            Text(label, style = MaterialTheme.typography.labelMedium, fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold, maxLines = 1)
        }
    }
}

@Composable
fun PosterSkeleton(modifier: Modifier = Modifier, width: Dp? = null) {
    Column(if (width != null) modifier.width(width) else modifier) {
        SkeletonBox(Modifier.fillMaxWidth().aspectRatio(2f / 3f))
        Spacer(Modifier.height(10.dp))
        SkeletonBox(Modifier.fillMaxWidth(0.9f).height(14.dp), RoundedCornerShape(6.dp))
        Spacer(Modifier.height(8.dp))
        SkeletonBox(Modifier.fillMaxWidth(0.55f).height(14.dp), RoundedCornerShape(6.dp))
    }
}

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    onSeeAll: (() -> Unit)? = null,
) {
    Row(
        modifier.fillMaxWidth().padding(start = 20.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            if (subtitle != null) {
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        if (onSeeAll != null) {
            TextButton(onClick = onSeeAll) {
                Text(stringResource(R.string.common_see_all), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
fun PosterRow(
    items: List<Anime>,
    loading: Boolean,
    onClick: (Anime) -> Unit,
    caption: (Anime) -> String? = { null },
    origin: String? = null,
    isNew: (Anime) -> Boolean = { false },
    highlightId: String? = null,
) {
    val stagger = rememberStaggerState()
    Crossfade(loading && items.isEmpty(), animationSpec = tween(Motion.Medium), label = "posterRow") { skeleton ->
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            userScrollEnabled = !skeleton,
        ) {
            if (skeleton) {
                items(6) { PosterSkeleton(width = 124.dp) }
            } else {
                itemsIndexed(items, key = { _, a -> a.id }) { index, anime ->
                    PosterCard(
                        anime,
                        onClick = { onClick(anime) },
                        modifier = Modifier.staggerIn(stagger, anime.id, index),
                        width = 124.dp,
                        caption = caption(anime),
                        sharedOrigin = origin,
                        isNew = isNew(anime),
                        highlighted = anime.id == highlightId,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ContinueWatchingCard(
    entry: WatchHistoryEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    sharedOrigin: String? = null,
) {
    val interaction = remember { MutableInteractionSource() }
    Column(
        modifier.width(240.dp)
            .pressScale(interaction)
            .clip(MaterialTheme.shapes.small)
            .clickable(interactionSource = interaction, indication = LocalIndication.current, onClick = onClick),
    ) {
        val thumb = rememberSharedKey(sharedOrigin?.let { sharedEpisodeKey(entry.episodeId, it) })
        Box(Modifier.fillMaxWidth().aspectRatio(16f / 9f).clip(MaterialTheme.shapes.small)) {
            NetImage(
                entry.episodeThumbnail.ifBlank { entry.animeCover },
                Modifier.fillMaxSize().sharedKey(thumb).clip(MaterialTheme.shapes.small),
                "${entry.animeTitle} — ${stringResource(R.string.watch_episode_label, entry.episodeNumber)}",
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .aboveSharedElements()
                    .revealWhenLanded(thumb)
                    .background(Brush.verticalGradient(0.5f to Color.Transparent, 1f to ImageScrim.copy(alpha = 0.85f))),
            )
            Text(
                stringResource(R.string.mine_ep_badge, entry.episodeNumber),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.align(Alignment.BottomStart).aboveSharedElements().revealWhenLanded(thumb).padding(start = 12.dp, bottom = 12.dp),
            )
            LinearProgressIndicator(
                progress = { entry.progress },
                modifier = Modifier.align(Alignment.BottomCenter).aboveSharedElements().revealWhenLanded(thumb).fillMaxWidth().height(4.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
                drawStopIndicator = {},
                gapSize = 0.dp,
            )
        }
        Text(
            entry.animeTitle,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 10.dp, start = 4.dp, end = 4.dp),
        )
        Text(
            if (entry.isFinished) stringResource(R.string.common_finished_watching) else remainingLabel(LocalContext.current.resources, entry.positionMs, entry.durationMs) ?: entry.episodeTitle,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 4.dp),
        )
    }
}

@Composable
fun MessageState(
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier,
    message: String? = null,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) {
    Column(
        modifier.fillMaxWidth().padding(horizontal = 32.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            Modifier.size(80.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceContainerLow),
            contentAlignment = Alignment.Center,
        ) {
            Icon(icon, null, Modifier.size(40.dp), tint = MaterialTheme.colorScheme.primary)
        }
        Spacer(Modifier.height(20.dp))
        Text(title, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
        if (message != null) {
            Spacer(Modifier.height(8.dp))
            Text(
                message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
        if (actionLabel != null && onAction != null) {
            Spacer(Modifier.height(24.dp))
            FilledTonalButton(onClick = onAction) {
                Icon(Icons.Rounded.Refresh, null, Modifier.size(20.dp))
                Spacer(Modifier.width(10.dp))
                Text(actionLabel)
            }
        }
    }
}

@Composable
fun ErrorState(message: UiText, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    MessageState(
        icon = Icons.Rounded.CloudOff,
        title = stringResource(R.string.common_error_title),
        message = message.text(),
        actionLabel = stringResource(R.string.common_retry),
        onAction = onRetry,
        modifier = modifier,
    )
}

/** Bottom-to-background fade used under hero and detail backdrops. */
fun Modifier.fadeToBackground(color: Color): Modifier = drawWithContent {
    drawContent()
    drawRect(Brush.verticalGradient(0.4f to Color.Transparent, 1f to color))
}

@Composable
fun StatusPill(status: String, modifier: Modifier = Modifier) {
    if (status.isBlank()) return
    val ongoing = status.equals("ONGOING", true)
    Pill(
        statusLabel(LocalContext.current.resources, status),
        modifier,
        container = if (ongoing) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.surfaceContainerHighest,
        content = if (ongoing) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

/** Enhanced ripple button with neon accent on press. */
@Composable
fun NeonRippleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    label: String? = null,
    iconTint: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceContainerLow,
    shape: androidx.compose.ui.graphics.Shape = MaterialTheme.shapes.small,
) {
    Box(
        modifier
            .clickable(
                onClick = onClick,
            )
            .background(
                backgroundColor,
                shape = shape,
            )
            .clip(shape),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                )
            }
            if (label != null) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = textColor,
                )
            }
        }
    }
}

/** Visual theme mode toggle with live color preview. */
@Composable
fun ThemeModeToggle(
    currentMode: ThemeMode,
    onModeChange: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    val modes = listOf(
        ThemeMode.LIGHT to stringResource(R.string.mine_theme_light),
        ThemeMode.DARK to stringResource(R.string.mine_theme_dark),
        ThemeMode.SYSTEM to stringResource(R.string.mine_theme_system),
    )
    val icons = mapOf(
        ThemeMode.LIGHT to Icons.Rounded.LightMode as ImageVector,
        ThemeMode.DARK to Icons.Rounded.DarkMode as ImageVector,
        ThemeMode.SYSTEM to Icons.Rounded.AutoAwesome as ImageVector,
    )
    Row(
        modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .clip(RoundedCornerShape(16.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        modes.forEachIndexed { index, (mode, label) ->
            val isSelected = currentMode == mode
            Box(
                Modifier
                    .weight(1f)
                    .heightIn(min = 44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
                    .clickable(onClick = { onModeChange(mode) })
                    .animateContentSize(),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Icon(
                        imageVector = icons[mode] ?: Icons.Rounded.AutoAwesome,
                        contentDescription = null,
                        tint = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp),
                    )
                    // Color preview chip showing the accent color for this theme mode
                    Box(
                        Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(themePreviewColor(mode))
                            .border(1.dp, if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.outlineVariant, CircleShape),
                    )
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                    )
                }
            }
        }
    }
}

/** Enhanced shimmer loading with faster, smoother animation. */
@Composable
fun EnhancedSkeletonBox(
    modifier: Modifier,
    shape: androidx.compose.ui.graphics.Shape = MaterialTheme.shapes.small,
    height: Dp? = null,
    width: Dp? = null,
) {
    Box(
        modifier
            .clip(shape)
            .then(if (width != null) Modifier.width(width) else Modifier)
            .then(if (height != null) Modifier.height(height) else Modifier)
            .shimmer(),
    )
}

/** Pulse animation for live/airing indicators. Falls back to static dot when animations are complex. */
@Composable
fun PulseIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    size: Dp = 8.dp,
) {
    Box(
        modifier
            .size(size)
            .clip(CircleShape)
            .background(color),
    )
}

/** Preview color chip for theme mode toggle - shows the accent color each mode would use. */
@Composable
private fun themePreviewColor(mode: ThemeMode): Color = when (mode) {
    ThemeMode.LIGHT -> MaterialTheme.colorScheme.primary
    ThemeMode.DARK -> MaterialTheme.colorScheme.secondary
    ThemeMode.SYSTEM -> MaterialTheme.colorScheme.tertiary
}
