package com.teufelsturm.tt_downloaderand_kotlin.foundSummitSingle;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.teufelsturm.tt_downloaderand_kotlin.dbHelper.DataBaseHelper;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class TT_Summit_AND implements Parcelable {
    private static String TAG = TT_Summit_AND.class.getSimpleName();

	private int intTTGipfelNr;
	private String str_SummitName;
	private int int_SummitNumberOfficial;
	private String str_Area;
	private int int_NumberOfRoutes;
	private int int_NumberofStarRoutes;
	private String str_EasiestGrade;
	private double dbl_GpsLong;
	private double dbl_GpsLat;
	private boolean bln_Asscended;
	private long long_DateAsscended;
	private String str_MyComment;

	public TT_Summit_AND(int intTTGipfelNr,
			int int_SummitNumberOfficial, String str_SummitName,
			String str_Area, Integer int_NumberOfRoutes,
			int int_NumberofStarRoutes, String str_EasiestGrade,
			double dbl_GpsLong, double dbl_GpsLat, boolean bln_Asscended,
			long long_DateAsscended, String str_MyComment) {
		this.intTTGipfelNr = intTTGipfelNr;

		Log.i(this.getClass().getSimpleName(),
				"--> intTTGipfelNr eingetragen...: " + intTTGipfelNr);

		this.int_SummitNumberOfficial = int_SummitNumberOfficial;
		this.str_SummitName = str_SummitName;
		this.str_Area = str_Area;
		this.int_NumberOfRoutes = int_NumberOfRoutes;
		this.int_NumberofStarRoutes = int_NumberofStarRoutes;
		this.str_EasiestGrade = str_EasiestGrade;
		this.dbl_GpsLong = dbl_GpsLong;
		this.dbl_GpsLat = dbl_GpsLat;
		this.bln_Asscended = bln_Asscended;
		this.long_DateAsscended = long_DateAsscended;
		this.str_MyComment = str_MyComment;
	}
	
	public TT_Summit_AND(int intTTGipfelNr, Context aContext ) {
		this.intTTGipfelNr = intTTGipfelNr;

		Log.i(this.getClass().getSimpleName(),
				"--> intTTGipfelNr eingetragen...: " + intTTGipfelNr);
			String QueryString1 = new StringBuilder()
					.append("SELECT   a.intTTGipfelNr, a.strName, a.strGebiet")
					.append(", a.intKleFuGipfelNr , a.intAnzahlWege , a.intAnzahlSternchenWege")
					.append(", a.strLeichtesterWeg, a.dblGPS_Latitude, a.dblGPS_Longitude")
					.append(", b.[isAscendedSummit], b.[intDateOfAscend], b.[strMySummitComment] ")
					.append(" FROM ")
					.append(" TT_Summit_AND a ")
					.append(" LEFT OUTER JOIN myTT_Summit_AND b")
					.append(" ON (a.[intTTGipfelNr] = b.[intTTGipfelNr]) ")
					.append(" WHERE a.[intTTGipfelNr] = ")
					.append(intTTGipfelNr).toString();
			Log.i(TAG, "intTTGipfelNr... : " + intTTGipfelNr);
			Log.i(TAG,
					"Neue Wege zum Gipfel suchen:\r\n" + QueryString1);
			
			Cursor cursor = null;
			Log.i(TAG,
					"noch bin ich da....");
			SQLiteDatabase newDB;
			DataBaseHelper dbHelper = new DataBaseHelper(aContext );
			newDB = dbHelper.getWritableDatabase();
			cursor = newDB.rawQuery(QueryString1, null);
			Log.i(TAG,
					"Neue Wege zum Gipfel suchen:\t c != null'"
							+ (cursor != null));

			if (cursor != null) {
				int iCounter = 0;
				if (cursor.moveToFirst()) {
					do {
						this.intTTGipfelNr = intTTGipfelNr;
						String WegName = cursor.getString(cursor
								.getColumnIndex("strName"));
						this.int_SummitNumberOfficial = cursor.getInt(cursor
								.getColumnIndex("intKleFuGipfelNr"));
						this.str_SummitName = cursor.getString(cursor
								.getColumnIndex("strName"));
						this.str_Area = cursor.getString(cursor
								.getColumnIndex("strGebiet"));
						this.int_NumberOfRoutes = cursor.getInt(cursor
								.getColumnIndex("intAnzahlWege"));
						this.int_NumberofStarRoutes = cursor.getInt(cursor
								.getColumnIndex("intAnzahlSternchenWege"));
						this.str_EasiestGrade = cursor.getString(cursor
								.getColumnIndex("strLeichtesterWeg"));
						this.dbl_GpsLong = cursor.getDouble(cursor
								.getColumnIndex("dblGPS_Longitude"));
						this.dbl_GpsLat  = cursor.getDouble(cursor
								.getColumnIndex("dblGPS_Latitude"));
						this.bln_Asscended = cursor.getInt(cursor
								.getColumnIndex("isAscendedSummit"))  > 0;
						this.long_DateAsscended = cursor.getLong(cursor
								.getColumnIndex("intDateOfAscend"))  ;
						this.str_MyComment = cursor.getString(cursor
								.getColumnIndex("strMySummitComment"));
						Log.i(TAG, ++iCounter
								+ " -> Neuer Weg... " + WegName);
					} while (cursor.moveToNext());
				}
			};
		}

	public Integer getInt_TTGipfelNr() {
		return intTTGipfelNr;
	}
	public Integer getInt_SummitNumberOfficial() {
		return int_SummitNumberOfficial;
	}
	public String getStr_SummitName() {
		return str_SummitName;
	}
	public String getStr_Area() {
		return str_Area;
	}
	public Integer getInt_NumberOfRoutes() {
		return int_NumberOfRoutes;
	}
	public Integer getInt_NumberofStarRoutes() {
		return int_NumberofStarRoutes;
	}
	public String getStr_EasiestGrade() {
		return str_EasiestGrade;
	}
	public Double getDbl_GpsLong() {
		return dbl_GpsLong;
	}
	public Double getDbl_GpsLat() {
		return dbl_GpsLat;
	}
	public boolean getBln_Asscended() {
		return bln_Asscended;
	}
	public String getStr_DateAsscended() {
		if (long_DateAsscended == 0 )
			return "   ";
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MMM.yyyy",
				Locale.GERMANY);
		Date resultdate = new Date( long_DateAsscended );
		return sdf.format(resultdate) ;
	}
	public String getStr_MyComment() {
		if ( str_MyComment == null )
			return "";
		return str_MyComment;
	}
	public Long getLong_DateAsscended() {
		return long_DateAsscended;
	}

	public void setDatumBestiegen(Long long_DateAsscended) {
		this.long_DateAsscended = long_DateAsscended;
	}

	public void setBln_Asscended(Boolean bln_Asscended) {
		this.bln_Asscended = bln_Asscended;
	}

	public void setStr_MyComment(String str_MyComment) {
		this.str_MyComment = str_MyComment;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(intTTGipfelNr);
		dest.writeInt(int_SummitNumberOfficial);
		dest.writeString(str_SummitName);
		dest.writeString(str_Area);
		dest.writeInt(int_NumberOfRoutes);
		dest.writeInt(int_NumberofStarRoutes);
		dest.writeString(str_EasiestGrade);
		dest.writeDouble(dbl_GpsLong);
		dest.writeDouble(dbl_GpsLat);
		dest.writeByte((byte) (bln_Asscended ? 1 : 0)); // if bln_Asscended ==
														// true, byte == 1
		dest.writeLong(long_DateAsscended);
		dest.writeString(str_MyComment);
	}

	private TT_Summit_AND(Parcel in) {
		intTTGipfelNr = in.readInt();
		int_SummitNumberOfficial = in.readInt();
		str_SummitName = in.readString();
		str_Area = in.readString();
		int_NumberOfRoutes = in.readInt();
		int_NumberofStarRoutes = in.readInt();
		str_EasiestGrade = in.readString();
		dbl_GpsLong = in.readDouble();
		dbl_GpsLat = in.readDouble();
		bln_Asscended = in.readByte() == 1; // myBoolean == true if byte == 1
		long_DateAsscended = in.readLong();
		str_MyComment = in.readString();
	}

	public static final Creator<TT_Summit_AND> CREATOR = new Creator<TT_Summit_AND>() {
		public TT_Summit_AND createFromParcel(Parcel in) {
			return new TT_Summit_AND(in);
		}

		public TT_Summit_AND[] newArray(int size) {
			return new TT_Summit_AND[size];
		}
	};
}
