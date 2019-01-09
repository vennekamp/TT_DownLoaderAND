package com.teufelsturm.tt_downloader3.model;

public class BaseModel {
    public enum Type
    {
        SUMMIT, ROUTE, COMMEMT
    }

    private long updated;

    public long getUpdated() {
        return updated;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }

    /**
     * The ordinal of the item in the TT Database (i.e. the ID of the item (summit, route or comment)
     */
    protected Integer intTT_IDOrdinal;
    public Integer getIntTT_IDOrdinal() {
        return intTT_IDOrdinal;
    }

    protected String str_TTSummitName;
    public String getStr_TTSummitName() {
        return str_TTSummitName;
    }


}
