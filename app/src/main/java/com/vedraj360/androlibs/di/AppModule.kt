package com.vedraj360.androlibs.di

import android.content.Context
import androidx.room.Room
import com.vedraj360.androlibs.api.LibraryApi
import com.vedraj360.androlibs.db.LibraryDatabase
import com.vedraj360.androlibs.utils.Base.baseUrl
import com.vedraj360.androlibs.utils.Constants.Companion.LIBRARY_DATABASE
import com.vedraj360.androlibs.utils.Helper.Companion.okHttp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object AppModule {
    @Provides
    @Singleton
    fun providerRetrofit(@ApplicationContext app: Context): Retrofit =
        Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttp(app))
            .build()

    @Provides
    @Singleton
    fun provideAndroLibsApi(retrofit: Retrofit): LibraryApi =
        retrofit.create(LibraryApi::class.java)

    @Provides
    @Singleton
    fun provideLibraryDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        LibraryDatabase::class.java,
        LIBRARY_DATABASE
    ).build()


    @Singleton
    @Provides
    fun provideLibraryDao(db: LibraryDatabase) = db.getLibraryDao()


}