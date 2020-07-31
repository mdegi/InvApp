/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.md.invapp.enums;

import java.util.Arrays;
import java.util.Optional;

/**
 * Enum Values for transaction types
 * 
 * @author Martin Degiorgio <www.nlasystems.com>
 */
public enum TransType {

    INVOICE   ("Invoice",1),
    CASH_SALE ("CashSale",1),
    QUOTATION ("Quotation",1),
    CASH_SALE_RETURN ("CashSaleReturn",-1),
    CREDIT_NOTE ("CreditNote",-1);

    private final String displayName;
    private final int multiplyingFactor;

    private TransType(String displayName, int multiplyingFactor) {
        this.displayName = displayName;
        this.multiplyingFactor = multiplyingFactor;
    }

    public String getDisplayName()       {return displayName;}
    public int getMultiplyingFactor()    {return multiplyingFactor;}
    
    
    public static TransType getTransType(String displayCompareName) {
               
        Optional<TransType> trType = Arrays.stream(values())
                .filter(trT -> trT.getDisplayName().equals(displayCompareName))
                .findAny();

        if (trType.isPresent()) {
            return trType.get();
        } else {
            return null;
        }                
    }
    
    
    
}
