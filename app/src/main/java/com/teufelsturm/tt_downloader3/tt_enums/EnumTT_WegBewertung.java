package com.teufelsturm.tt_downloader3.tt_enums;

import java.text.DecimalFormat;

/**
 *
 * @author Martin
 */
public enum EnumTT_WegBewertung {
   KAMIKAZE(0), SEHR_SCHLECHT(1), SCHLECHT(2), NORMAL(3), GUT(4), SEHT_GUT(5), HERAUSRAGEND(6);
   private int value;
   public static Integer getMinInteger(){ return 0; }
   public static Integer getMaxInteger(){ return 6; }

    private EnumTT_WegBewertung(int value) {
         this.value = value;
        }

    public static String getStrUpdate(Number progress) {

        if ( (int)(progress.floatValue() * 1000f) - (1000 * progress.intValue()) != 0 ) {
            // a broken number in the first 3 digits
            return new DecimalFormat("0.#").format(progress);
        }
        // *else* an even number in the first 3 digits 1.00005, 1, 2.0000004
        return String.valueOf(progress.intValue()) + " ("
                + values()[progress.intValue()] + ")";
    }

    public int getValue() {
        return this.value;
    }
    @Override
    public String toString() {
         switch (this) {
           case KAMIKAZE:
                return "'---' (Kamikaze)";
           case SEHR_SCHLECHT:
                return "'--' (sehr schlecht)";
           case SCHLECHT:
                return "'-' (schlecht)";
           case NORMAL:
                return "(Normal)";
           case GUT:
                return "'+' (gut)";
           case SEHT_GUT:
                return "'++' (sehr gut)";
           case HERAUSRAGEND:
                return "'+++' (Herausragend)";
          }
    return super.toString();
    }
    public static EnumTT_WegBewertung toTT_Bewertung(String aDescription) {
         if (aDescription.equals("--- (Kamikaze)")) {
            return KAMIKAZE;
        }
         if (aDescription.equals("-- (sehr schlecht)")) {
            return SEHR_SCHLECHT;
        }
         if (aDescription.equals("- (schlecht)")) {
            return SCHLECHT;
        }
         if (aDescription.equals("(Normal)")) {
            return NORMAL;
        }
         if (aDescription.equals("+ (gut)")) {
            return GUT;
        }
         if (aDescription.equals("++ (sehr gut)")) {
            return SEHT_GUT;
        }
         if (aDescription.equals("+++ (Herausragend)")) {
            return HERAUSRAGEND;
        }
    return null;
    }
}
