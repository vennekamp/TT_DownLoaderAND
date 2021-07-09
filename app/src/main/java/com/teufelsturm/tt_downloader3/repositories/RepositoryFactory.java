package com.teufelsturm.tt_downloader3.repositories;

import android.content.Context;

import com.teufelsturm.tt_downloader3.model.BaseModel;
import com.teufelsturm.tt_downloader3.model.TT_Comment_AND;
import com.teufelsturm.tt_downloader3.model.TT_Route_AND;
import com.teufelsturm.tt_downloader3.model.TT_Summit_AND;

public class RepositoryFactory {
    private static IRepository<TT_Summit_AND> mSummitRepository = null;
    private static IRepository<TT_Route_AND> mRouteRepository = null;
    private static IRepository<TT_Comment_AND> mCommentRepository = null;

    public static IRepository<TT_Route_AND> getRouteRepository (Context context) {
        if (mRouteRepository  == null) {
            mRouteRepository = new Repository<>(context, BaseModel.Type.ROUTE);
        }
        return mRouteRepository;
    }

    public static IRepository<TT_Summit_AND> getSummitRepository(Context context) {
        if (mSummitRepository == null) {
            mSummitRepository = new Repository<>(context, BaseModel.Type.SUMMIT);
        }
        return mSummitRepository;
    }

    public static IRepository<TT_Comment_AND> getCommentRepository(Context context) {
        if (mCommentRepository == null) {
            mCommentRepository = new Repository<>(context, BaseModel.Type.COMMEMT);
        }
        return mCommentRepository;
    }
}
