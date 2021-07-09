package com.teufelsturm.tt_downloader3.model;

import android.util.Log;

import com.teufelsturm.tt_downloader3.repositories.RepositoryFactory;

public class TT_Comment_AND extends BaseModel{
	private String strEntryKommentar;
	private String strWegName;
	private Integer intTTGipfelNr;
	private Integer intEntryBewertung;
	private String strEntryUser;
	private Long longEntryDatum;

	public String getStrEntryKommentar() {
		return strEntryKommentar;
	}
	public String getStrWegName() {
		return strWegName;
	}

	public int getIntTTGipfelNr() {
		// TODO Auto-generated method stub
		return intTTGipfelNr;
	}
	public Integer getIntEntryBewertung() {
		return intEntryBewertung;
	}
	public String getStrEntryUser() {
		Log.i(this.getClass().getSimpleName(), "getStrEntryUser ...: " + strEntryUser);
		return strEntryUser;
	}
	public Long getLongEntryDatum() {
		Log.i(this.getClass().getSimpleName(), "longEntryDatum ...: " + longEntryDatum);
		return longEntryDatum;
	}


	public TT_Comment_AND(Integer intTTWegNr, String strEntryKommentar,
			String strWegName, String strGipfelName,  int intTTGipfelNr,
			Integer intEntryBewertung, String strEntryUser
			, Long longEntryDatum) {
		super.intTT_IDOrdinal = intTTWegNr;
		this.strEntryKommentar = strEntryKommentar;
		this.strWegName = strWegName;
        super.str_TTSummitName  = strGipfelName;
		this.intTTGipfelNr = intTTGipfelNr;
		this.intEntryBewertung = intEntryBewertung;
		Log.i(this.getClass().getSimpleName(), "this.strEntryUser = strEntryUser; ...: " + strEntryUser);
		this.strEntryUser = strEntryUser;
		Log.i(this.getClass().getSimpleName(), "this.longEntryDatum = longEntryDatum; ...: " + longEntryDatum);
		this.longEntryDatum = longEntryDatum;
		RepositoryFactory.getCommentRepository(null).saveItem(this);
	}
}
