/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teufelsturm.tt_downloaderand_kotlin.tt_objects;

/**
 *
 * @author Martin
 */
public enum EnumSachsenSchwierigkeitsGrad {
    // Die Schwierigkeit 'mal 10'
    na(Integer.MIN_VALUE), I(10), II(20), III(30), IV(40), V(50), VI(60), 
    VIIa(71), VIIb(72), VIIc(73), VIIIa(81), VIIIb(82), VIIIc(83),
    IXa(91), IXb(92), IXc(93), Xa(101), Xb(102), Xc(103), 
    XIa(111), XIb(112), XIc(113), XIIa(121), XIIb(122), XIIc(123), XIIIa(130),
    Sprung1(151),Sprung2(152),Sprung3(153),Sprung4(154);
    private int value;
    public static Integer getMinInteger(){ return 1; }
    public static Integer getMaxInteger(){ return EnumSachsenSchwierigkeitsGrad.values().length - 1; } // 29, but "NA" is skipped!
    
    private EnumSachsenSchwierigkeitsGrad(int value) {
         this.value = value;
        }
    public Integer getValue() {
        /* if (this.value == Integer.MIN_VALUE ) 
            return Float.NaN; */
        return this.value; //  / 10.0f;
    }
    
    public static String toStringFromSkaleOrdinal(int valueI){
        switch(valueI){
            case 1: return "I";
            case 2: return "II";
            case 3: return "III";
            case 4: return "IV";
            case 5: return "V";
            case 6: return "VI";
            case 7: return "VIIa";
            case 8: return "VIIb";
            case 9: return "VIIc";
            case 10: return "VIIIa";
            case 11: return "VIIIb";
            case 12: return "VIIIc";
            case 13: return "IXa";
            case 14: return "IXb";
            case 15: return "IXc";
            case 16: return "Xa";
            case 17: return "Xb";
            case 18: return "Xc";
            case 19: return "XIa";
            case 20: return "XIb";
            case 21: return "XIc";
            case 22: return "XIIa";
            case 23: return "XIIb";
            case 24: return "XIIc";
            case 25: return "XIIIa";
            case 26: return "Sprung 1";
            case 27: return "Sprung 2";
            case 28: return "Sprung 3";
            case 29: return "Sprung 4";
        }
        return "";
    }

    public static String toString(Integer valueInt){
        switch(valueInt){
            case 10: return "I";
            case 20: return "II";
            case 30: return "III";
            case 40: return "IV";
            case 50: return "V";
            case 60: return "VI";
            case 71: return "VIIa";
            case 72: return "VIIb";
            case 73: return "VIIc";
            case 81: return "VIIIa";
            case 82: return "VIIIb";
            case 83: return "VIIIc";
            case 91: return "IXa";
            case 92: return "IXb";
            case 93: return "IXc";
            case 101: return "Xa";
            case 102: return "Xb";
            case 103: return "Xc";
            case 111: return "XIa";
            case 112: return "XIb";
            case 113: return "XIc";
            case 121: return "XIIa";
            case 122: return "XIIb";
            case 123: return "XIIc";
            case 130: return "XIIIa";
            case 151: return "1";
            case 152: return "2";
            case 153: return "3";
            case 154: return "4";
        }
    return "";
    }
    
    
    public static EnumSachsenSchwierigkeitsGrad values(Integer valueInt){
        switch(valueInt){
            case 10: return EnumSachsenSchwierigkeitsGrad.I;
            case 20: return EnumSachsenSchwierigkeitsGrad.II;
            case 30: return EnumSachsenSchwierigkeitsGrad.III;
            case 40: return EnumSachsenSchwierigkeitsGrad.IV;
            case 50: return EnumSachsenSchwierigkeitsGrad.V;
            case 60: return EnumSachsenSchwierigkeitsGrad.VI;
            case 71: return EnumSachsenSchwierigkeitsGrad.VIIa;
            case 72: return EnumSachsenSchwierigkeitsGrad.VIIb;
            case 73: return EnumSachsenSchwierigkeitsGrad.VIIc;
            case 81: return EnumSachsenSchwierigkeitsGrad.VIIIa;
            case 82: return EnumSachsenSchwierigkeitsGrad.VIIIb;
            case 83: return EnumSachsenSchwierigkeitsGrad.VIIIc;
            case 91: return EnumSachsenSchwierigkeitsGrad.IXa;
            case 92: return EnumSachsenSchwierigkeitsGrad.IXb;
            case 93: return EnumSachsenSchwierigkeitsGrad.IXc;
            case 101: return EnumSachsenSchwierigkeitsGrad.Xa;
            case 102: return EnumSachsenSchwierigkeitsGrad.Xb;
            case 103: return EnumSachsenSchwierigkeitsGrad.Xc;
            case 111: return EnumSachsenSchwierigkeitsGrad.XIa;
            case 112: return EnumSachsenSchwierigkeitsGrad.XIb;
            case 113: return EnumSachsenSchwierigkeitsGrad.XIc;
            case 121: return EnumSachsenSchwierigkeitsGrad.XIIa;
            case 122: return EnumSachsenSchwierigkeitsGrad.XIIb;
            case 123: return EnumSachsenSchwierigkeitsGrad.XIIc;
            case 130: return EnumSachsenSchwierigkeitsGrad.XIIIa;
            case 151: return EnumSachsenSchwierigkeitsGrad.Sprung1;
            case 152: return EnumSachsenSchwierigkeitsGrad.Sprung2;
            case 153: return EnumSachsenSchwierigkeitsGrad.Sprung3;
            case 154: return EnumSachsenSchwierigkeitsGrad.Sprung4;
        }
       return null;
    }
    public static EnumSachsenSchwierigkeitsGrad valuesFromSkaleOrdinal(int rangeSliderValue) {
        switch (rangeSliderValue) {
            case 1: return EnumSachsenSchwierigkeitsGrad.I;
            case 2: return EnumSachsenSchwierigkeitsGrad.II;
            case 3: return EnumSachsenSchwierigkeitsGrad.III;
            case 4: return EnumSachsenSchwierigkeitsGrad.IV;
            case 5: return EnumSachsenSchwierigkeitsGrad.V;
            case 6: return EnumSachsenSchwierigkeitsGrad.VI;
            case 7: return EnumSachsenSchwierigkeitsGrad.VIIa;
            case 8: return EnumSachsenSchwierigkeitsGrad.VIIb;
            case 9: return EnumSachsenSchwierigkeitsGrad.VIIc;
            case 10: return EnumSachsenSchwierigkeitsGrad.VIIIa;
            case 11: return EnumSachsenSchwierigkeitsGrad.VIIIb;
            case 12: return EnumSachsenSchwierigkeitsGrad.VIIIc;
            case 13: return EnumSachsenSchwierigkeitsGrad.IXa;
            case 14: return EnumSachsenSchwierigkeitsGrad.IXb;
            case 15: return EnumSachsenSchwierigkeitsGrad.IXc;
            case 16: return EnumSachsenSchwierigkeitsGrad.Xa;
            case 17: return EnumSachsenSchwierigkeitsGrad.Xb;
            case 18: return EnumSachsenSchwierigkeitsGrad.Xc;
            case 19: return EnumSachsenSchwierigkeitsGrad.XIa;
            case 20: return EnumSachsenSchwierigkeitsGrad.XIb;
            case 21: return EnumSachsenSchwierigkeitsGrad.XIc;
            case 22: return EnumSachsenSchwierigkeitsGrad.XIIa;
            case 23: return EnumSachsenSchwierigkeitsGrad.XIIb;
            case 24: return EnumSachsenSchwierigkeitsGrad.XIIc;
            case 25: return EnumSachsenSchwierigkeitsGrad.XIIIa;
            case 26: return EnumSachsenSchwierigkeitsGrad.Sprung1;
            case 27: return EnumSachsenSchwierigkeitsGrad.Sprung2;
            case 28: return EnumSachsenSchwierigkeitsGrad.Sprung3;
            case 29: return EnumSachsenSchwierigkeitsGrad.Sprung4;
            }
        return null;
    }

}
