package com.dracul.notes.utills

import androidx.compose.runtime.mutableStateOf
import com.mohamedrejeb.richeditor.model.RichTextState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class ListNode<RichTextState>(
    var value: RichTextState,
    var prev: ListNode<RichTextState>? = null,
    var next: ListNode<RichTextState>? = null
)


class History(state: RichTextState) {
    private var node = MutableStateFlow(ListNode(state))
    val current = node.asStateFlow()
    var isHasNext = mutableStateOf(node.value.next != null)
    var isHasPrev = mutableStateOf(node.value.prev != null)
    val coroutineScope = CoroutineScope(Dispatchers.IO)

    init {
        coroutineScope.launch {
            node.collect {
                isHasPrev.value = it.prev != null
                isHasNext.value = it.next != null
                poop("IsHasNext")
                poop(isHasNext.value)
                poop("IsHasPrev")
                poop(isHasPrev.value)
            }
        }

    }

    fun add(element: RichTextState) {
        node.value.next = ListNode(element, prev = node.value)
        node.value = node.value.next!!
        poop(element.annotatedString.text)
    }

    fun next() {
        node.value.next?.let {
            node.value = it
        }
        poop(node.value.value.annotatedString.text)

    }

    fun prev() {
        node.value.prev?.let {
            node.value = it
        }
        poop(node.value.value.annotatedString.text)
    }

}