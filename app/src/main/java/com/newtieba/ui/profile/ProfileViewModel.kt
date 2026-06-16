package com.newtieba.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newtieba.data.model.Forum
import com.newtieba.data.model.Thread
import com.newtieba.data.model.TiebaUser
import com.newtieba.data.api.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val uid: Long = 0,
    val isMe: Boolean = false,
    val user: TiebaUser? = null,
    val selectedTab: Int = 0,
    val threads: List<Thread> = emptyList(),
    val forums: List<Forum> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authDataStore: AuthDataStore,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    val isLoggedIn: Boolean get() = authDataStore.isLoggedIn

    fun init(uid: Long, isMe: Boolean) {
        if (_uiState.value.uid == uid && _uiState.value.isMe == isMe) return
        _uiState.value = _uiState.value.copy(uid = uid, isMe = isMe)
        loadProfile()
    }

    fun selectTab(index: Int) {
        _uiState.value = _uiState.value.copy(selectedTab = index)
        loadTabData()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                // TODO: 接入真实 API (TiebaAppApiService.getUserInfo)
                _uiState.value = _uiState.value.copy(
                    user = TiebaUser(
                        uid = _uiState.value.uid,
                        name = if (_uiState.value.isMe) "我" else "用户${_uiState.value.uid}",
                        portrait = "tb.1.xxx_profile",
                        level = 10,
                        intro = if (_uiState.value.isMe) "这个人很懒，什么都没写…" else "个人签名",
                    ),
                    isLoading = false,
                )
                loadTabData()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message,
                )
            }
        }
    }

    private fun loadTabData() {
        viewModelScope.launch {
            when (_uiState.value.selectedTab) {
                0 -> {
                    // 帖子
                    _uiState.value = _uiState.value.copy(
                        threads = List(8) { i ->
                            Thread(
                                tid = 2001L + i,
                                title = "我的帖子标题 #${i + 1}",
                                author = _uiState.value.user ?: TiebaUser(uid = 0, name = "", portrait = ""),
                                replyNum = (0..100).random(),
                                agreeNum = (0..50).random(),
                                lastTime = System.currentTimeMillis() - (i * 86400_000L),
                                abstract = if (i % 3 == 0) "这是第 ${i + 1} 个帖子的摘要" else "",
                            )
                        },
                        forums = emptyList(),
                    )
                }
                1 -> {
                    // 回复（简化处理，复用 threads 字段展示）
                    _uiState.value = _uiState.value.copy(
                        threads = List(5) { i ->
                            Thread(
                                tid = 3001L + i,
                                title = "回复的帖子 #${i + 1}（回复内容占位）",
                                author = TiebaUser(uid = 6000L + i, name = "楼主${i + 1}", portrait = ""),
                                replyNum = (0..30).random(),
                                agreeNum = (0..20).random(),
                                lastTime = System.currentTimeMillis() - (i * 43200_000L),
                            )
                        },
                        forums = emptyList(),
                    )
                }
                2 -> {
                    // 关注的吧
                    _uiState.value = _uiState.value.copy(
                        threads = emptyList(),
                        forums = List(12) { i ->
                            Forum(
                                fid = 1001L + i,
                                name = "吧${i + 1}",
                                memberNum = (1000..100000).random(),
                                isFollowed = true,
                            )
                        },
                    )
                }
            }
        }
    }
}
