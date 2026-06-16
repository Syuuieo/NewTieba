package com.newtieba.ui.message

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newtieba.data.model.ContentPiece
import com.newtieba.data.model.ContentType
import com.newtieba.data.model.TiebaUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MessageItem(
    val id: Long,
    val fromUser: TiebaUser,
    val content: String,
    val time: Long,
    val isRead: Boolean = false,
)

data class PrivateConversation(
    val id: Long,
    val withUser: TiebaUser,
    val lastMessage: String,
    val lastTime: Long,
    val unreadCount: Int = 0,
)

data class MessageUiState(
    val selectedTab: Int = 0,
    val replyMessages: List<MessageItem> = emptyList(),
    val atMessages: List<MessageItem> = emptyList(),
    val conversations: List<PrivateConversation> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class MessageViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(MessageUiState())
    val uiState: StateFlow<MessageUiState> = _uiState.asStateFlow()

    init {
        loadMessages()
    }

    fun selectTab(index: Int) {
        _uiState.value = _uiState.value.copy(selectedTab = index)
    }

    private fun loadMessages() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                // TODO: 接入真实 API (TiebaAppApiService.getReplys / getAts / getMsgList)
                _uiState.value = _uiState.value.copy(
                    replyMessages = List(6) { i ->
                        MessageItem(
                            id = 1001L + i,
                            fromUser = TiebaUser(
                                uid = 7001L + i,
                                name = "吧友${i + 1}",
                                portrait = "tb.1.xxx_msg${i}",
                            ),
                            content = listOf(
                                "回复了你的帖子：说得好！",
                                "在帖子中@了你：来看看这个",
                                "回复了你的评论：同意+1",
                            )[i % 3],
                            time = System.currentTimeMillis() - (i * 3600_000L),
                            isRead = i > 2,
                        )
                    },
                    atMessages = List(3) { i ->
                        MessageItem(
                            id = 2001L + i,
                            fromUser = TiebaUser(
                                uid = 8001L + i,
                                name = "用户${i + 1}",
                                portrait = "tb.1.xxx_at${i}",
                            ),
                            content = "@你 来看看这个帖子",
                            time = System.currentTimeMillis() - (i * 7200_000L),
                            isRead = false,
                        )
                    },
                    conversations = List(4) { i ->
                        PrivateConversation(
                            id = 3001L + i,
                            withUser = TiebaUser(
                                uid = 9001L + i,
                                name = "联系人${i + 1}",
                                portrait = "tb.1.xxx_chat${i}",
                            ),
                            lastMessage = "这是最后一条消息",
                            lastTime = System.currentTimeMillis() - (i * 3600_000L),
                            unreadCount = if (i < 2) (1..5).random() else 0,
                        )
                    },
                    isLoading = false,
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message,
                )
            }
        }
    }
}
