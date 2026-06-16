package com.newtieba.ui.login

import androidx.lifecycle.ViewModel
import com.newtieba.data.api.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class LoginUiState(
    val isLoggedIn: Boolean = false,
    val userName: String? = null,
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authDataStore: AuthDataStore,
) : ViewModel() {

    val isLoggedIn: Boolean get() = authDataStore.isLoggedIn
    val userName: String? get() = authDataStore.userName
    val bduss: String? get() = authDataStore.bduss

    fun saveCredentials(bduss: String, stoken: String?, userName: String? = null) {
        authDataStore.bduss = bduss
        if (!stoken.isNullOrBlank()) authDataStore.stoken = stoken
        if (!userName.isNullOrBlank()) authDataStore.userName = userName
    }

    fun logout() {
        authDataStore.logout()
    }
}
