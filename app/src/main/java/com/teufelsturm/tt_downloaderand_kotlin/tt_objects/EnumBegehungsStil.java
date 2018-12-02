/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teufelsturm.tt_downloaderand_kotlin.tt_objects;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.teufelsturm.tt_downloaderand_kotlin.R;

/**
 *
 * @author Martin
 */
public enum EnumBegehungsStil {
    GARNICHT, 
    TO_DO,
    SACK,
    NACHSTIEG, 
    SITZSCHLINGE, 
    ALLESFREI, 
    GETEILTEFUEHRUNG,
    ROTPUNKT, 
    ONSIGHT,
    SOLO;  
    

    
    @NonNull
    @Override
    public String toString() {
         switch (this) {
           case GARNICHT:           return "Nicht Bestiegen";
           case TO_DO:              return "Will mal: ToDo  ";
           case SACK:               return "Gesackt...";
           case NACHSTIEG:          return "Nachstieg: v.o.g. ";
           case SITZSCHLINGE:       return "mit Ruheschlinge: RS";
           case ALLESFREI:          return "alles Frei: a.f.";
           case GETEILTEFUEHRUNG:   return "geteilte Führung";
           case ROTPUNKT:           return "Rotpunkt: RP";
           case ONSIGHT:            return "on-sight: OS";
           case SOLO:               return "free solo: solo";
           default:
                 throw new IllegalArgumentException("Unknown argument: " + this.toString());
          }
    }

    private int getDrawable() {
        switch (this) {
            case GARNICHT:
                return R.drawable.ic_checkbox;      //  "Nicht Bestiegen";
            case TO_DO:
                return R.drawable.ic_todo;          // "Will mal: To-Do  ";
            case SACK:
                return R.drawable.ic_sack;          // "Gesackt...";
            case NACHSTIEG:
                return R.drawable.ic_vog;           //  "Nachstieg: v.o.g. ";
            case SITZSCHLINGE:
                return R.drawable.ic_ruheschlinge;  // "mit Ruheschlinge: RS";
            case ALLESFREI:
                return R.drawable.ic_allesfrei;     // "alles Frei: a.f.";
            case GETEILTEFUEHRUNG:
                return R.drawable.ic_seilschaft;    // "geteilte Führung";
            case ROTPUNKT:
                return R.drawable.ic_rp;            //  "Rotpunkt: RP";
            case ONSIGHT:
                return R.drawable.ic_onsight;       //  "on-sight: OS";
            case SOLO:
                return R.drawable.ic_solo;          //  "free solo: solo";
            default:
                throw new IllegalArgumentException("Unknown argument: " + this.toString());
        }
    }

    public static SpannableString[] getBegehungsStile(Context context) {
		int myCount = EnumBegehungsStil.values().length;
        SpannableString[] rtnArray = new SpannableString[myCount];
		for (int i = 0; i < myCount; i++) {
            rtnArray[i] = new SpannableString("   " /* spaces to be replaced */
                    + EnumBegehungsStil.values()[i].toString());
            Drawable d = ContextCompat.getDrawable(context, EnumBegehungsStil.values()[i].getDrawable());
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            ImageSpan imageSpan = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
            rtnArray[i] .setSpan(imageSpan,0,2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
		return rtnArray;
	}

	public static SpannableString getBegehungsStil(Context mContext, int intBegehungsStil) {
        EnumBegehungsStil enumBegehungsStil = EnumBegehungsStil.values()[intBegehungsStil];
        SpannableString ss = new SpannableString("  " + enumBegehungsStil.toString());
        Drawable d;
        switch ( enumBegehungsStil ) {
            case GARNICHT: // EnumBegehungsStil.GARNICHT.ordinal():
                d = mContext.getResources().getDrawable(R.drawable.ic_checkbox);
                break;
            case TO_DO: // EnumBegehungsStil.TO_DO.ordinal():
                d = mContext.getResources().getDrawable(R.drawable.ic_todo);
                break;
            case SACK: // EnumBegehungsStil.SACK.ordinal():
                d = mContext.getResources().getDrawable(R.drawable.ic_sack);
                break;
            case NACHSTIEG: // EnumBegehungsStil.NACHSTIEG.ordinal():
                d = mContext.getResources().getDrawable(R.drawable.ic_vog);
                break;
            case SITZSCHLINGE: // EnumBegehungsStil.SITZSCHLINGE.ordinal():
                d = mContext.getResources().getDrawable(R.drawable.ic_ruheschlinge);
                break;
            case ALLESFREI: // EnumBegehungsStil.ALLESFREI.ordinal():
                d = mContext.getResources().getDrawable(R.drawable.ic_allesfrei);
                break;
            case GETEILTEFUEHRUNG: // EnumBegehungsStil.GETEILTEFUEHRUNG.ordinal():
                d = mContext.getResources().getDrawable(R.drawable.ic_seilschaft);
                break;
            case ROTPUNKT: // EnumBegehungsStil.ROTPUNKT.ordinal():
                d = mContext.getResources().getDrawable(R.drawable.ic_rp);
                break;
            case ONSIGHT: // EnumBegehungsStil.ONSIGHT.ordinal():
                d = mContext.getResources().getDrawable(R.drawable.ic_onsight);
                break;
            case SOLO: // EnumBegehungsStil.SOLO.ordinal():
                d = mContext.getResources().getDrawable(R.drawable.ic_solo);
                break;
            default:
                throw new IllegalArgumentException("Argument not known");
        }

        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
        ss.setSpan(span, 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return ss;
    }
}
