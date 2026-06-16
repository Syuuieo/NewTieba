package com.newtieba.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.newtieba.data.api.ProtobufConverterFactory
import com.newtieba.data.api.interceptor.TiebaAppInterceptor
import com.newtieba.data.api.service.TiebaAppApiService
import com.newtieba.data.api.service.TiebaWebApiService
import com.newtieba.data.local.TiebaDatabase
import com.newtieba.data.local.ForumDao
import com.newtieba.data.local.ThreadDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AppApiRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WebApiRetrofit

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideOkHttpClient(
        tiebaInterceptor: TiebaAppInterceptor,
    ): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.HEADERS
        }
        return OkHttpClient.Builder()
            .addInterceptor(tiebaInterceptor)
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    // ── App Protobuf 接口 Retrofit ──────────────────────────────────

    @Provides
    @Singleton
    @AppApiRetrofit
    fun provideAppApiRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://c.tieba.baidu.com/")
            .client(okHttpClient)
            .addConverterFactory(ProtobufConverterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideTiebaAppApiService(
        @AppApiRetrofit retrofit: Retrofit,
    ): TiebaAppApiService = retrofit.create(TiebaAppApiService::class.java)

    // ── Web JSON 接口 Retrofit ──────────────────────────────────────

    @Provides
    @Singleton
    @WebApiRetrofit
    fun provideWebApiRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://tieba.baidu.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideTiebaWebApiService(
        @WebApiRetrofit retrofit: Retrofit,
    ): TiebaWebApiService = retrofit.create(TiebaWebApiService::class.java)

    // ── Room Database ─────────────────────────────────────────────

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TiebaDatabase {
        return androidx.room.Room.databaseBuilder(
            context,
            TiebaDatabase::class.java,
            TiebaDatabase.NAME,
        ).build()
    }

    @Provides
    fun provideThreadDao(db: TiebaDatabase): ThreadDao = db.threadDao()

    @Provides
    fun provideForumDao(db: TiebaDatabase): ForumDao = db.forumDao()

    // ── AuthDataStore ─────────────────────────────────────────────
    // 由 Hilt @Inject constructor 自动提供
}
