package com.example.practicas.components

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.zIndex
import kotlinx.coroutines.CoroutineScope

// ==============================
//  1. GESTOR PARA LISTAS (LazyColumn)
// ==============================
@Composable
fun rememberDragDropState(
    lazyListState: LazyListState,
    onMove: (Int, Int) -> Unit,
    onDragEnd: () -> Unit
): DragDropState {
    val scope = rememberCoroutineScope()
    val state = remember(lazyListState) {
        DragDropState(
            state = lazyListState,
            onMove = onMove,
            onDragEnd = onDragEnd,
            scope = scope
        )
    }
    return state
}

class DragDropState(
    private val state: LazyListState,
    private val onMove: (Int, Int) -> Unit,
    private val onDragEnd: () -> Unit,
    private val scope: CoroutineScope
) {
    var draggingItemIndex by mutableStateOf<Int?>(null)
        private set
    internal var draggingItemOffset by mutableStateOf(0f)
        private set

    internal fun onDragStart(offset: Offset) {
        state.layoutInfo.visibleItemsInfo
            .firstOrNull { item ->
                offset.y.toInt() in item.offset..(item.offset + item.size)
            }?.also {
                draggingItemIndex = it.index
            }
    }

    internal fun onDragInterrupted() {
        if (draggingItemIndex != null) onDragEnd()
        draggingItemIndex = null
        draggingItemOffset = 0f
    }

    internal fun onDrag(offset: Offset) {
        draggingItemOffset += offset.y
        val currentDraggingItemIndex = draggingItemIndex ?: return
        val currentDraggingItem = state.layoutInfo.visibleItemsInfo
            .find { it.index == currentDraggingItemIndex } ?: return

        val startOffset = currentDraggingItem.offset + draggingItemOffset
        val middleOffset = startOffset + (currentDraggingItem.size / 2f)

        val targetItem = state.layoutInfo.visibleItemsInfo.find { item ->
            middleOffset.toInt() in item.offset..(item.offset + item.size) &&
                    currentDraggingItem.index != item.index
        }

        if (targetItem != null) {
            val indexToMove = if (currentDraggingItem.index < targetItem.index) targetItem.index else targetItem.index
            onMove(currentDraggingItem.index, indexToMove)
            draggingItemIndex = indexToMove
            draggingItemOffset = 0f
        }
    }
}

fun Modifier.dragContainer(dragDropState: DragDropState): Modifier {
    return pointerInput(dragDropState) {
        detectDragGesturesAfterLongPress(
            onDrag = { change, dragAmount ->
                change.consume()
                dragDropState.onDrag(dragAmount)
            },
            onDragStart = { offset -> dragDropState.onDragStart(offset) },
            onDragEnd = { dragDropState.onDragInterrupted() },
            onDragCancel = { dragDropState.onDragInterrupted() }
        )
    }
}

fun Modifier.draggableItem(dragDropState: DragDropState, index: Int): Modifier {
    return if (index == dragDropState.draggingItemIndex) {
        zIndex(1f)
            .graphicsLayer {
                translationY = dragDropState.draggingItemOffset
                scaleX = 1.05f
                scaleY = 1.05f
                alpha = 0.9f
                shadowElevation = 8f
            }
    } else {
        this
    }
}


// ==============================
//  2. GESTOR PARA GRIDS (LazyVerticalGrid)
// ==============================
@Composable
fun rememberGridDragDropState(
    gridState: LazyGridState,
    onMove: (Int, Int) -> Unit,
    onDragEnd: () -> Unit
): GridDragDropState {
    val scope = rememberCoroutineScope()
    return remember(gridState) {
        GridDragDropState(gridState, onMove, onDragEnd)
    }
}

class GridDragDropState(
    private val state: LazyGridState,
    private val onMove: (Int, Int) -> Unit,
    private val onDragEnd: () -> Unit
) {
    var draggingItemIndex by mutableStateOf<Int?>(null)
        private set
    internal var draggingItemOffset by mutableStateOf(Offset.Zero)
        private set

    internal fun onDragStart(offset: Offset) {
        // En Grid necesitamos checar X e Y
        state.layoutInfo.visibleItemsInfo
            .firstOrNull { item ->
                val x = item.offset.x
                val y = item.offset.y
                offset.x.toInt() in x..(x + item.size.width) &&
                        offset.y.toInt() in y..(y + item.size.height)
            }?.also {
                draggingItemIndex = it.index
            }
    }

    internal fun onDragInterrupted() {
        if (draggingItemIndex != null) onDragEnd()
        draggingItemIndex = null
        draggingItemOffset = Offset.Zero
    }

    internal fun onDrag(offset: Offset) {
        draggingItemOffset += offset
        val currentIndex = draggingItemIndex ?: return
        val currentItem = state.layoutInfo.visibleItemsInfo.find { it.index == currentIndex } ?: return

        // Calculamos el centro del item arrastrado
        val currentCenterX = currentItem.offset.x + draggingItemOffset.x + (currentItem.size.width / 2)
        val currentCenterY = currentItem.offset.y + draggingItemOffset.y + (currentItem.size.height / 2)

        // Buscamos con quién choca
        val targetItem = state.layoutInfo.visibleItemsInfo.find { item ->
            currentCenterX.toInt() in item.offset.x..(item.offset.x + item.size.width) &&
                    currentCenterY.toInt() in item.offset.y..(item.offset.y + item.size.height) &&
                    currentIndex != item.index
        }

        if (targetItem != null) {
            onMove(currentIndex, targetItem.index)
            draggingItemIndex = targetItem.index
            draggingItemOffset = Offset.Zero // Reset visual suave
        }
    }
}

fun Modifier.gridDragContainer(dragDropState: GridDragDropState): Modifier {
    return pointerInput(dragDropState) {
        detectDragGesturesAfterLongPress(
            onDrag = { change, dragAmount ->
                change.consume()
                dragDropState.onDrag(dragAmount)
            },
            onDragStart = { offset -> dragDropState.onDragStart(offset) },
            onDragEnd = { dragDropState.onDragInterrupted() },
            onDragCancel = { dragDropState.onDragInterrupted() }
        )
    }
}

fun Modifier.gridDraggableItem(dragDropState: GridDragDropState, index: Int): Modifier {
    return if (index == dragDropState.draggingItemIndex) {
        zIndex(1f)
            .graphicsLayer {
                translationX = dragDropState.draggingItemOffset.x
                translationY = dragDropState.draggingItemOffset.y
                scaleX = 1.05f
                scaleY = 1.05f
                alpha = 0.9f
                shadowElevation = 8f
            }
    } else {
        this
    }
}