/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teufelsturm.tt_downloaderand_kotlin.tt_objects;


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
    

    
    @Override
    public String toString() {
         switch (this) {
           case GARNICHT:
                return "Nicht Bestiegen";
           case TO_DO:
                return "Will mal: ToDo  ";
           case SACK:
                return "Gesackt...";
           case NACHSTIEG:
                return "Nachstieg: v.o.g. ";
           case SITZSCHLINGE:
                return "mit Ruheschlinge: RS";
           case ALLESFREI:
                return "alles Frei: a.f.";
           case GETEILTEFUEHRUNG:
                return "geteilte Führung";
           case ROTPUNKT:
                return "Rotpunkt: RP";
           case ONSIGHT:
                return "on-sight: OS";
           case SOLO:
                return "free solo: solo";
          }
    return "?!?";
    }

	public static String[] getBegehungsStile() {
		int myCount = EnumBegehungsStil.values().length;
		String[] rtnArray = new String[myCount];
		for (int i = 0; i < myCount; i++) {
			rtnArray[i] = EnumBegehungsStil.values()[i].toString();
		}
		return rtnArray;
	}
}
