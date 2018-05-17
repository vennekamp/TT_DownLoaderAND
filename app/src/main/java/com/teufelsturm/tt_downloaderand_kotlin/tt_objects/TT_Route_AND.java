package com.teufelsturm.tt_downloaderand_kotlin.tt_objects;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.teufelsturm.tt_downloaderand_kotlin.TT_DB_Helper.DataBaseHelper;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class TT_Route_AND implements Parcelable {
	private Integer intTTWegNr;
	private Integer intTTGipfelNr;
	private String strWegName;
	private String strGipfelName;
	private Boolean blnAusrufeZeichen;
	private Integer intSterne;
	private String strSchwierigkeitsGrad;
	private Integer sachsenSchwierigkeitsGrad;
	private Integer ohneUnterstützungSchwierigkeitsGrad;
	private Integer rotPunktSchwierigkeitsGrad;
	private Integer intSprungSchwierigkeitsGrad;
	private Integer intAnzahlDerKommentare;
	private Float fltMittlereWegBewertung;
	private Integer intBegehungsStil = 0;
	private Long long_DateAsscended;
	private String strKommentar = "";

	public TT_Route_AND(Integer intTTWegNr, Integer intGipfelNr,
			String WegName, String strGipfelName, Boolean blnAusrufeZeichen, 
			Integer intSterne, String strSchwierigkeitsGrad, 
			Integer sachsenSchwierigkeitsGrad,
			Integer ohneUnterstützungSchwierigkeitsGrad,
			Integer rotPunktSchwierigkeitsGrad,
			Integer intSprungSchwierigkeitsGrad,
			Integer intAnzahlDerKommentare, Float fltMittlereWegBewertung,
			Integer isBestiegen, Long datumBestiegen, String strKommentar) {

		Log.i(this.getClass().getSimpleName(),
				"--> intTTWegNr eingetragen...: "
						+ intTTWegNr
						+ "******************************************************************"
						+ "******************************************************************");
		this.intTTWegNr = intTTWegNr;
		this.intTTGipfelNr = intGipfelNr;
		this.strWegName = WegName;
		this.strGipfelName = strGipfelName;
		this.blnAusrufeZeichen = blnAusrufeZeichen;
		this.intSterne = intSterne;
		this.strSchwierigkeitsGrad = strSchwierigkeitsGrad;
		this.sachsenSchwierigkeitsGrad = sachsenSchwierigkeitsGrad;
		this.ohneUnterstützungSchwierigkeitsGrad = ohneUnterstützungSchwierigkeitsGrad;
		this.rotPunktSchwierigkeitsGrad = rotPunktSchwierigkeitsGrad;
		this.intSprungSchwierigkeitsGrad = intSprungSchwierigkeitsGrad;
		this.intAnzahlDerKommentare = intAnzahlDerKommentare;
		this.fltMittlereWegBewertung = fltMittlereWegBewertung;
		this.intBegehungsStil = isBestiegen;
		this.long_DateAsscended = datumBestiegen;
		this.strKommentar = strKommentar;
	}
//	
	public TT_Route_AND(Integer intTTWegNr, Context aContext ) {
		this.intTTWegNr = intTTWegNr;

		Log.i(this.getClass().getSimpleName(),
				"--> intTTWegNr eingetragen...: " + intTTWegNr);
			String QueryString1 = "SELECT a.[intTTWegNr], a.[intTTGipfelNr], a.[WegName], c.[strName]"
									+ ", a.[blnAusrufeZeichen], a.[intSterne], a.[strSchwierigkeitsGrad]"
									+ ", a.[sachsenSchwierigkeitsGrad], a.[ohneUnterstützungSchwierigkeitsGrad]"
									+ ", a.[rotPunktSchwierigkeitsGrad], a.[intSprungSchwierigkeitsGrad]"
									+ ", a.[intAnzahlDerKommentare], a.[fltMittlereWegBewertung]"
									+ ", b.[isAscendedRoute], b.[intDateOfAscend], b.[strMyRouteComment]"
									+ " FROM [TT_Summit_AND] c, TT_Route_AND a"
									+ " LEFT OUTER JOIN myTT_Route_AND b"
									+ " ON (a.[intTTWegNr] = b.[intTTWegNr])"
									+ "WHERE a.[intTTGipfelNr] = c.[intTTGipfelNr]"
									+ "AND a.[intTTWegNr] = " + intTTWegNr;
			Log.i(getClass().getSimpleName(),
					"Neue Wege zum Gipfel suchen:\r\n" + QueryString1);
			
			Cursor cursor = null;
			Log.i(getClass().getSimpleName(),
					"noch bin ich da....");
			SQLiteDatabase newDB;
			DataBaseHelper dbHelper = new DataBaseHelper(aContext );
			newDB = dbHelper.getWritableDatabase();
			cursor = newDB.rawQuery(QueryString1, null);
			Log.i(getClass().getSimpleName(),
					"Neue Wege zum Gipfel suchen:\t c != null'"
							+ (cursor != null));

			if (cursor != null) {
				int iCounter = 0;
				if (cursor.moveToFirst()) {
					do {
						this.intTTWegNr = intTTWegNr;
						String WegName = cursor.getString(cursor
								.getColumnIndex("intTTWegNr"));
						this.intTTGipfelNr = cursor.getInt(cursor
								.getColumnIndex("intTTGipfelNr"));
						this.strWegName = cursor.getString(cursor
								.getColumnIndex("WegName"));
						this.strGipfelName = cursor.getString(cursor
								.getColumnIndex("strName"));
						this.blnAusrufeZeichen = cursor.getInt(cursor
								.getColumnIndex("blnAusrufeZeichen")) > 0;
						this.intSterne = cursor.getInt(cursor
								.getColumnIndex("intSterne"));
						this.strSchwierigkeitsGrad = cursor.getString(cursor
								.getColumnIndex("strSchwierigkeitsGrad"));
						this.sachsenSchwierigkeitsGrad = cursor.getInt(cursor
								.getColumnIndex("sachsenSchwierigkeitsGrad"));
						this.ohneUnterstützungSchwierigkeitsGrad  = cursor.getInt(cursor
								.getColumnIndex("ohneUnterstützungSchwierigkeitsGrad"));
						this.rotPunktSchwierigkeitsGrad = cursor.getInt(cursor
								.getColumnIndex("rotPunktSchwierigkeitsGrad")) ;
						this.intSprungSchwierigkeitsGrad = cursor.getInt(cursor
								.getColumnIndex("intSprungSchwierigkeitsGrad"));
						this.intAnzahlDerKommentare = cursor.getInt(cursor
								.getColumnIndex("intAnzahlDerKommentare"));
						this.fltMittlereWegBewertung = cursor.getFloat(cursor
								.getColumnIndex("fltMittlereWegBewertung"));
						this.long_DateAsscended = cursor.getLong(cursor
								.getColumnIndex("intDateOfAscend"))  ;
						this.strKommentar = cursor.getString(cursor
								.getColumnIndex("strMyRouteComment"));
						Log.i(getClass().getSimpleName(), ++iCounter
								+ " -> Neuer Weg... " + WegName);
					} while (cursor.moveToNext());
				}
			};
		}

	public Integer getIntWegNr() {
		return intTTWegNr;
	}

	public Integer getIntGipfelNr() {
		return intTTGipfelNr;
	}

	public String getStrWegName() {
		return strWegName;
	}
	
	public String getStrGipfelName(){
		return strGipfelName;
	}

	public Boolean getBlnAusrufeZeichen() {
		return blnAusrufeZeichen;
	}

	public Integer getIntSterne() {
		return intSterne;
	}

	public String getStrSchwierigkeitsGrad() {
		return strSchwierigkeitsGrad;
	}

	public Integer getSachsenSchwierigkeitsGrad() {
		return sachsenSchwierigkeitsGrad;
	}

	public Integer getOhneUnterstützungSchwierigkeitsGrad() {
		return ohneUnterstützungSchwierigkeitsGrad;
	}

	public Integer getRotPunktSchwierigkeitsGrad() {
		return rotPunktSchwierigkeitsGrad;
	}

	public Integer getIntSprungSchwierigkeitsGrad() {
		return intSprungSchwierigkeitsGrad;
	}

	public Integer getIntAnzahlDerKommentare() {
		return intAnzahlDerKommentare;
	}

	public Float getFltMittlereWegBewertung() {
		return fltMittlereWegBewertung;
	}

	public Integer getBegehungsStil() {
		return intBegehungsStil;
	}

	public String getDatumBestiegen() {
		if (long_DateAsscended == 0)
			return "";
		Date resultdate = new Date(long_DateAsscended);
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MMM.yyyy", Locale.GERMANY);
		return sdf.format(resultdate);
	}

	public Long getLong_DateAsscended() {
		return long_DateAsscended;
	}

	public String getStrKommentar() {
		if (strKommentar == null)
			return "";
		return strKommentar;
	}

	public void setBegehungsStil(Integer isBestiegen) {
		this.intBegehungsStil = isBestiegen;
	}

	public void setDatumBestiegen(Long datumBestiegen) {
		this.long_DateAsscended = datumBestiegen;
		Log.i(getClass().getSimpleName(), "this.long_DateAsscended = datumBestiegen --> " + this.long_DateAsscended);
	}

	public void setStrKommentar(String strKommentar) {
		this.strKommentar = strKommentar;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
//		int iCounter = 0; 
//		Log.i(getClass().getSimpleName(), ++iCounter
//				+ " -> writeToParcel... intTTWegNr" );
		dest.writeInt(intTTWegNr);
//		Log.i(getClass().getSimpleName(), ++iCounter
//				+ " -> writeToParcel... intTTGipfelNr" );
		dest.writeInt(intTTGipfelNr);
//		Log.i(getClass().getSimpleName(), ++iCounter
//				+ " -> writeToParcel... " );
		dest.writeString(strWegName);
//		Log.i(getClass().getSimpleName(), ++iCounter
//				+ " -> writeToParcel... " );
		dest.writeString(strGipfelName);
//		Log.i(getClass().getSimpleName(), ++iCounter
//				+ " -> writeToParcel... " );		
		dest.writeByte((byte) (blnAusrufeZeichen ? 1 : 0));
//		Log.i(getClass().getSimpleName(), ++iCounter
//				+ " -> writeToParcel... " );
		dest.writeInt(intSterne);
//		Log.i(getClass().getSimpleName(), ++iCounter
//				+ " -> writeToParcel... " );
		dest.writeString(strSchwierigkeitsGrad);
//		Log.i(getClass().getSimpleName(), ++iCounter
//				+ " -> writeToParcel... " );
		dest.writeInt(sachsenSchwierigkeitsGrad);
//		Log.i(getClass().getSimpleName(), ++iCounter
//				+ " -> writeToParcel... " );
		dest.writeInt(ohneUnterstützungSchwierigkeitsGrad);
//		Log.i(getClass().getSimpleName(), ++iCounter
//				+ " -> writeToParcel... " );
		dest.writeInt(rotPunktSchwierigkeitsGrad);
//		Log.i(getClass().getSimpleName(), ++iCounter
//				+ " -> writeToParcel... " );
		dest.writeInt(intSprungSchwierigkeitsGrad);
//		Log.i(getClass().getSimpleName(), ++iCounter
//				+ " -> writeToParcel... " );
		dest.writeInt(intAnzahlDerKommentare);
//		Log.i(getClass().getSimpleName(), ++iCounter
//				+ " -> writeToParcel... " );
		dest.writeFloat(fltMittlereWegBewertung);
//		Log.i(getClass().getSimpleName(), ++iCounter
//				+ " -> writeToParcel... " );
		dest.writeInt(intBegehungsStil);
//		Log.i(getClass().getSimpleName(), ++iCounter
//				+ " -> writeToParcel... " );
		dest.writeLong(long_DateAsscended);
//		Log.i(getClass().getSimpleName(), ++iCounter
//				+ " -> writeToParcel... " );
		dest.writeString(strKommentar);
//		Log.i(getClass().getSimpleName(), ++iCounter
//				+ " -> writeToParcel... strKommentar" + strKommentar );
	}

	private TT_Route_AND(Parcel in) {
		intTTWegNr = in.readInt();
		intTTGipfelNr = in.readInt();
		strWegName = in.readString();
		strGipfelName = in.readString();
		blnAusrufeZeichen = (in.readByte() == 1); // myBoolean == true if byte == 1
		intSterne = in.readInt();
		strSchwierigkeitsGrad = in.readString();
		sachsenSchwierigkeitsGrad = in.readInt();
		ohneUnterstützungSchwierigkeitsGrad = in.readInt();
		rotPunktSchwierigkeitsGrad = in.readInt();
		intSprungSchwierigkeitsGrad = in.readInt();
		intAnzahlDerKommentare = in.readInt();
		fltMittlereWegBewertung = in.readFloat();
		intBegehungsStil = in.readInt(); 
		long_DateAsscended = in.readLong();
		strKommentar = in.readString();
	}

	public static final Creator<TT_Route_AND> CREATOR = new Creator<TT_Route_AND>() {
		public TT_Route_AND createFromParcel(Parcel in) {
			return new TT_Route_AND(in);
		}

		public TT_Route_AND[] newArray(int size) {
			return new TT_Route_AND[size];
		}
	};
}
