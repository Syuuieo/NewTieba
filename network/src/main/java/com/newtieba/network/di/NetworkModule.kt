package com.newtieba.network.di

import com.newtieba.network.interceptors.CommonParamInterceptor
import com.newtieba.network.interceptors.CookieInterceptor
import com.newtieba.network.interceptors.SignInterceptor
import com.newtieba.protocol.api.TiebaApiService
import com.newtieba.protocol.api.endpoints.*
import com.newtieba.protocol.auth.AuthManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * 网络依赖注入模块
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://tiebac.baidu.com/"
    private const val CONNECT_TIMEOUT = 30L
    private const val READ_TIMEOUT = 30L
    private const val WRITE_TIMEOUT = 30L

    @Provides
    @Singleton
    fun provideAuthManager(): AuthManager {
        return AuthManager.getInstance()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        commonParamInterceptor: CommonParamInterceptor,
        signInterceptor: SignInterceptor,
        cookieInterceptor: CookieInterceptor
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(commonParamInterceptor)
            .addInterceptor(signInterceptor)
            .addInterceptor(cookieInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideForumApi(retrofit: Retrofit): ForumApi {
        return retrofit.create(ForumApi::class.java)
    }

    @Provides
    @Singleton
    fun provideThreadApi(retrofit: Retrofit): ThreadApi {
        return retrofit.create(ThreadApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMessageApi(retrofit: Retrofit): MessageApi {
        return retrofit.create(MessageApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTiebaApiService(
        authApi: AuthApi,
        forumApi: ForumApi,
        threadApi: ThreadApi,
        userApi: UserApi,
        messageApi: MessageApi,
        authManager: AuthManager
    ): TiebaApiService {
        return TiebaApiService.getInstance(
            authApi,
            forumApi,
            threadApi,
            userApi,
            messageApi,
            authManager
        )
    }
}
