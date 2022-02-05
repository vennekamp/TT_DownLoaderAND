package com.teufelsturm.tt_downloader_kotlin.app

import android.content.Context
import androidx.room.Room
import com.teufelsturm.tt_downloader_kotlin.data.db.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): TTDataBase {
        return TTDataBase.getInstance(context = appContext)
    }

    @Singleton
    @Provides
    fun provideTTSummitDAO(ttDatabase: TTDataBase): TTSummitDAO {
        return ttDatabase.ttSummitDAO
    }

    @Singleton
    @Provides
    fun provideTTRouteDAO(ttDatabase: TTDataBase): TTRouteDAO {
        return ttDatabase.ttRouteDAO
    }

    @Singleton
    @Provides
    fun provideTTCommentDAO(ttDatabase: TTDataBase): TTCommentDAO {
        return ttDatabase.ttCommentDAO
    }

    @Singleton
    @Provides
    fun provideTTNeigbourDAO(ttDatabase: TTDataBase): TTNeigbourSummitANDDAO {
        return ttDatabase.ttNeigbourSummitANDDAO
    }

    @Singleton
    @Provides
    fun provideMyTTRouteANDWithPhotos(ttDatabase: TTDataBase): MyTTRouteDAO {
        return ttDatabase.myTTRouteDAO
    }
}