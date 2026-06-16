package com.newtieba.ui.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NotificationItem(
    val id: Long,
    val icon: String, // emoji or icon key
    val title: String,
    val summary: String,
    val time: Long,
    val isRead: Boolean = false,
    val type: NotificationType = NotificationType.SYSTEM,
)

enum class NotificationType { SYSTEM, REPLY, AT, FOLLOW }

data class NotificationsUiState(
    val notifications: List<NotificationItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class NotificationsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

    init {
        loadNotifications()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            loadNotifications()
        }
    }

    fun markAsRead(id: Long) {
        val updated = _uiState.value.notifications.map {
            if (it.id == id) it.copy(isRead = true) else it
        }
        _uiState.value = _uiState.value.copy(notifications = updated)
    }

    private fun loadNotifications() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                // TODO: 接入真实通知 API
                _uiState.value = _uiState.value.copy(
                    notifications = listOf(
                        NotificationItem(
                            1, "👍", "点赞通知", "吧友XXX 赞了你的帖子",
                            System.currentTimeMillis() - 1800_000, false, NotificationType.REPLY,
                        ),
                        NotificationItem(
                            2, "💬", "回复通知", "吧友YYY 回复了你的帖子：说得好！",
                            System.currentTimeMillis() - 3600_000, false, NotificationType.REPLY,
                        ),
                        NotificationItem(
                            3, "@", "@通知", "吧友ZZZ @了你：来看看这个",
                            System.currentTimeMillis() - 7200_000, false, NotificationType.AT,
                        ),
                        NotificationItem(
                            4, "❤️", "新粉丝", "用户ABC 关注了你",
                            System.currentTimeMillis() - 14400_000, true, NotificationType.FOLLOW,
                        ),
                        NotificationItem(
                            5, "📢", "系统通知", "贴吧系统维护通知",
                            System.currentTimeMillis() - 86400_000, true, NotificationType.SYSTEM,
                        ),
                    ),
                    isLoading = false,
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }
}
