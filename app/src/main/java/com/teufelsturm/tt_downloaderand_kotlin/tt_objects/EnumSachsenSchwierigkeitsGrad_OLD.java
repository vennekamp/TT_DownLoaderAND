package com.teufelsturm.tt_downloaderand_kotlin.tt_objects;

/**
 *
 * @author Martin
 */
public enum EnumSachsenSchwierigkeitsGrad_OLD {
    // Die Schwierigkeit 'mal 10'
    na(Integer.MIN_VALUE), I(10), II(20), III(30), IV(40), V(50), VI(60), 
    VIIa(67), VIIb(70), VIIc(73), VIIIa(77), VIIIb(80), VIIIc(83),
    IXa(87), IXb(90), IXc(93), Xa(97), Xb(100), Xc(103), 
    XIa(107), XIb(110), XIc(113), XIIa(117), XIIb(120), XIIc(123);
    private int value;
    public static Integer getMinInteger(){ return 10; }
    public static Integer getMaxInteger(){ return 125; }
    
    private EnumSachsenSchwierigkeitsGrad_OLD(int value) {
         this.value = value;
        }
    public double getValue() {
        if (this.value == Integer.MIN_VALUE ) 
            return Double.NaN;
        return this.value / 10d;
    }
    public static String toString(double valueD){
    	valueD = (int)(valueD * 100) ;
        if (valueD < 150) return "I";
        if (valueD < 250) return "II";
        if (valueD < 350) return "III";
        if (valueD < 450) return "IV";
        if (valueD < 550) return "V";
        if (valueD < 650) return "VI";
        if (valueD < 685) return "VIIa";
        if (valueD < 715) return "VIIb";
        if (valueD < 750) return "VIIc";
        if (valueD < 785) return "VIIIa";
        if (valueD < 815) return "VIIIb";
        if (valueD < 850) return "VIIIc";
        if (valueD < 885) return "IXa";
        if (valueD < 915) return "IXb";
        if (valueD < 950) return "IXc";
        if (valueD < 985) return "Xa";
        if (valueD < 1015) return "Xb";
        if (valueD < 1050) return "Xc";
        if (valueD < 1085) return "XIa";
        if (valueD < 1115) return "XIb";
        if (valueD < 1150) return "XIc";
        if (valueD < 1185) return "XIIa";
        if (valueD < 1215) return "XIIb";
        if (valueD <= 1250) return "XIIc";
        return "na";
    }


    
}