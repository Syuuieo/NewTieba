package com.newtieba.ui.createpost

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixTheme
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.basic.TopAppBar

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreatePostScreen(
    onBack: () -> Unit = {},
    onSuccess: () -> Unit = {},
    viewModel: CreatePostViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { uri ->
        uri?.let { viewModel.addImage(it.toString()) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = "发帖",
                onBack = onBack,
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(12.dp),
        ) {
            // 标题
            TextField(
                value = uiState.title,
                onValueChange = { viewModel.updateTitle(it) },
                placeholder = "标题",
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            // 选择贴吧
            TextField(
                value = uiState.forumName,
                onValueChange = { viewModel.updateForum(it) },
                placeholder = "选择贴吧",
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                singleLine = true,
            )

            // 正文
            TextField(
                value = uiState.content,
                onValueChange = { viewModel.updateContent(it) },
                placeholder = "正文内容",
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp).let {
                    it.then(
                        Modifier
                            .fillMaxWidth()
                            .let { it1 -> it1.padding(bottom = 0.dp) }
                    )
                },
                minLines = 6,
                maxLines = 12,
            )

            // 图片选择
            if (uiState.selectedImages.isNotEmpty()) {
                FlowRow(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    uiState.selectedImages.forEachIndexed { index, uri ->
                        Column {
                            AsyncImage(
                                model = uri,
                                contentDescription = null,
                                modifier = Modifier.size(80.dp).aspectRatio(1f),
                            )
                            IconButton(
                                onClick = { viewModel.removeImage(index) },
                                modifier = Modifier.size(20.dp),
                            ) {
                                Icon(Icons.Default.Close, "删除")
                            }
                        }
                    }
                }
            }

            Button(
                onClick = {
                    imagePickerLauncher.launch("image/*")
                },
                modifier = Modifier.padding(top = 8.dp),
            ) {
                Icon(Icons.Default.Add, "添加图片")
                Text(" 添加图片")
            }

            // 错误提示
            uiState.error?.let { error ->
                Text(
                    text = error,
                    color = MiuixTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }

            // 提交按钮
            Button(
                onClick = {
                    viewModel.submit()
                },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                enabled = !uiState.isSubmitting,
            ) {
                Text(
                    text = if (uiState.isSubmitting) "发布中…" else "发布",
                    color = MiuixTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}
