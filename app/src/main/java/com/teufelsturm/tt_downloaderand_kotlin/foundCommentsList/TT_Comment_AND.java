//package com.teufelsturm.tt_downloaderand_kotlin.foundCommentsList;
//
//import android.os.Parcel;
//import android.os.Parcelable;
//import android.util.Log;
//
//import com.teufelsturm.tt_downloader3.model.BaseModel;
//
//public class TT_Comment_AND  extends BaseModel implements Parcelable {
//	private Integer intTTWegNr;
//	private String strEntryKommentar;
//	private String strWegName;
//	private Integer intTTGipfelNr;
//	private String strGipfelName;
//	private Integer intEntryBewertung;
//	private String strEntryUser;
//	private Long longEntryDatum;
//
//	public Integer getIntWegNr() {
//		return intTTWegNr;
//	}
//	public String getStrEntryKommentar() {
//		return strEntryKommentar;
//	}
//	public String getStrWegName() {
//		return strWegName;
//	}
//
//	public String getStrGipfelName() {
//		// TODO Auto-generated method stub
//		return strGipfelName;
//	}
//
//	public int getIntTTGipfelNr() {
//		// TODO Auto-generated method stub
//		return intTTGipfelNr;
//	}
//	public Integer getIntEntryBewertung() {
//		return intEntryBewertung;
//	}
//	public String getStrEntryUser() {
//		Log.i(this.getClass().getSimpleName(), "getStrEntryUser ...: " + strEntryUser);
//		return strEntryUser;
//	}
//	public Long getLongEntryDatum() {
//		Log.i(this.getClass().getSimpleName(), "longEntryDatum ...: " + longEntryDatum);
//		return longEntryDatum;
//	}
//
//
//	public TT_Comment_AND(Integer intTTWegNr, String strEntryKommentar,
//			String strWegName, String strGipfelName,  int intTTGipfelNr,
//			Integer intEntryBewertung, String strEntryUser
//			, Long longEntryDatum) {
//		this.intTTWegNr = intTTWegNr;
//		this.strEntryKommentar = strEntryKommentar;
//		this.strWegName = strWegName;
//		this.strGipfelName = strGipfelName;
//		this.intTTGipfelNr = intTTGipfelNr;
//		this.intEntryBewertung = intEntryBewertung;
//		Log.i(this.getClass().getSimpleName(), "this.strEntryUser = strEntryUser; ...: " + strEntryUser);
//		this.strEntryUser = strEntryUser;
//		Log.i(this.getClass().getSimpleName(), "this.longEntryDatum = longEntryDatum; ...: " + longEntryDatum);
//		this.longEntryDatum = longEntryDatum;
//	}
//
//	@Override
//	public int describeContents() {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	public void writeToParcel(Parcel dest, int flags) {
//		dest.writeInt(intTTWegNr);
//		dest.writeString(strEntryKommentar);
//		dest.writeString(strWegName);
//		dest.writeInt(intTTGipfelNr);
//		dest.writeString(strGipfelName);
//		dest.writeInt(intEntryBewertung);
//		dest.writeString(strEntryUser);
//		dest.writeLong(longEntryDatum);
//	}
//
//	private TT_Comment_AND(Parcel in) {
//		intTTWegNr = in.readInt();
//		strEntryKommentar = in.readString();
//		strWegName = in.readString();
//		strGipfelName = in.readString();
//		intTTGipfelNr = in.readInt();
//		intEntryBewertung = in.readInt();
//		strEntryUser = in.readString();
//		longEntryDatum = in.readLong();
//	}
//
//	public static final Creator<TT_Comment_AND> CREATOR = new Creator<TT_Comment_AND>() {
//		public TT_Comment_AND createFromParcel(Parcel in) {
//			return new TT_Comment_AND(in);
//		}
//
//		public TT_Comment_AND[] newArray(int size) {
//			return new TT_Comment_AND[size];
//		}
//	};
//
//}
