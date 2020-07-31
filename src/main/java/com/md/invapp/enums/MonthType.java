/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.md.invapp.enums;

/**
 * Enum Values for currencies
 * 
 * @author Martin Degiorgio <www.nlasystems.com>
 */
public enum MonthType {

    JAN ("Jan","January"),
    FEB ("Feb","February"),
    MAR ("Mar","March"),
    APR ("Apr","April"),
    MAY ("May","May"),
    JUN ("Jun","June"),
    JUL ("Jul","July"),
    AUG ("Aug","August"),
    SEP ("Sep","September"),
    OCT ("Oct","October"),
    NOV ("Nov","November"),
    DEC ("Dec","December");


    private final String code;
    private final String name;

    private MonthType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String toString()       {return code;}

    public String getDisplayName() {return name;};


   /** Get the code for this Currencies description and return an empty String
     * if no matching description is found in this Currencies enum
     *
     * @param dsc
     * @return code
     */
    public static String getCode(String dsc) {

        String code = "";
        for (MonthType m: MonthType.values()) {
            if (m.getDisplayName().equals(dsc)) {
                code = m.toString();
            }
        }

        return code;
    }

    /**
     * Get the display name of this <code>Currencies</code> code and return an empty
     * String if the code is not found
     *
     * @param code
     * @return name
     */
    public static String getDisplayName(String code) {

        String name="";
        for (MonthType e : MonthType.values()) {
            if (e.code.equals(code)) {
                name = e.getDisplayName();
            }
        }
        return name;
    }

}
