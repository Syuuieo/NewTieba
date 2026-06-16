package com.newtieba.ui.createpost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CreatePostUiState(
    val title: String = "",
    val content: String = "",
    val forumName: String = "",
    val selectedImages: List<String> = emptyList(),
    val isSubmitting: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class CreatePostViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(CreatePostUiState())
    val uiState: StateFlow<CreatePostUiState> = _uiState.asStateFlow()

    fun updateTitle(title: String) {
        _uiState.value = _uiState.value.copy(title = title)
    }

    fun updateContent(content: String) {
        _uiState.value = _uiState.value.copy(content = content)
    }

    fun updateForum(name: String) {
        _uiState.value = _uiState.value.copy(forumName = name)
    }

    fun addImage(uri: String) {
        _uiState.value = _uiState.value.copy(
            selectedImages = _uiState.value.selectedImages + uri
        )
    }

    fun removeImage(index: Int) {
        _uiState.value = _uiState.value.copy(
            selectedImages = _uiState.value.selectedImages.toMutableList().apply { removeAt(index) }
        )
    }

    fun submit() {
        val state = _uiState.value
        if (state.title.isBlank() || state.content.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "标题和内容不能为空")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSubmitting = true)
            try {
                // TODO: 调用 TiebaAppApiService.addPost
                kotlinx.coroutines.delay(1000) // 模拟网络请求
                _uiState.value = _uiState.value.copy(
                    isSubmitting = false,
                    isSuccess = true,
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSubmitting = false,
                    error = e.message,
                )
            }
        }
    }

    fun reset() {
        _uiState.value = CreatePostUiState()
    }
}
