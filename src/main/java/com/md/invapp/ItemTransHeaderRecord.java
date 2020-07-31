/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md.invapp;

import com.md.invapp.enums.TransType;
import stdClasses.InvAppException;

/**
 *
 * @author Martin.Degiorgio
 */
public class ItemTransHeaderRecord {

    /**
     * @return the accNumber
     */
    public String getAccNumber() {
        return accNumber;
    }

    /**
     * @param accNumber the accNumber to set
     */
    public void setAccNumber(String accNumber) {
        this.accNumber = accNumber;
    }

    /**
     * @return the accName
     */
    public String getAccName() {
        return accName;
    }

    /**
     * @param accName the accName to set
     */
    public void setAccName(String accName) {
        this.accName = accName;
    }

    /**
     * @return the accAddLine1
     */
    public String getAccAddLine1() {
        return accAddLine1;
    }

    /**
     * @param accAddLine1 the accAddLine1 to set
     */
    public void setAccAddLine1(String accAddLine1) {
        this.accAddLine1 = accAddLine1;
    }

    /**
     * @return the accAddLine2
     */
    public String getAccAddLine2() {
        return accAddLine2;
    }

    /**
     * @param accAddLine2 the accAddLine2 to set
     */
    public void setAccAddLine2(String accAddLine2) {
        this.accAddLine2 = accAddLine2;
    }

    /**
     * @return the accAddLine3
     */
    public String getAccAddLine3() {
        return accAddLine3;
    }

    /**
     * @param accAddLine3 the accAddLine3 to set
     */
    public void setAccAddLine3(String accAddLine3) {
        this.accAddLine3 = accAddLine3;
    }

    /**
     * @return the accVatNum
     */
    public String getAccVatNum() {
        return accVatNum;
    }

    /**
     * @param accVatNum the accVatNum to set
     */
    public void setAccVatNum(String accVatNum) {
        this.accVatNum = accVatNum;
    }

    private String comments, transType, transDate, accNumber, accName, accAddLine1, accAddLine2, accAddLine3, accVatNum;
    
    private int reference;
    
    private double percentageDisc, floatingDisc;
    
    public final static int NEW_RECORD = 0;
    
    public final static int CANCELLED_RECORD = -1;

    
    public void initVars() {                
    }

    /**
     * @return the comments
     */
    public String getComments() {
        return comments;
    }

    /**
     * @param comments the comments to set
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * @return the transType
     */
    public TransType getTransType() {        
        TransType tr = null;        
        for (TransType trEnum : TransType.values()) {
            if (trEnum.toString().equals(transType)) {
                tr = trEnum;
                break;
            }            
        }        
        return tr;        
    }

    /**
     * This method needs to be private so as external classes can only access
     * the method setTransType(TransType tr). This will limit them to
     * predefined values only and cannot insert any other value in our variable
     * 
     * @param trType - String
     */    
    private void setTransTypeVal(String trType) {
        this.transType = trType;
    }

    /**
     * This method is to be accessible from other classes so as to set
     * the private String transType according to pre-defined Enum values
     *
     * @param tr TransType
     */   
    public void setTransType(TransType tr){
        if (tr != null) {
            setTransTypeVal(tr.toString());
        }
    }

    public void setTransType(String transType) throws InvAppException {
        boolean validRec = false;
        
        for (TransType tr : TransType.values()) {
            if (tr.toString().equals(transType)) {
                setTransTypeVal(transType);
                validRec = true;
            }
        }        
        if (!validRec) {
            throw new InvAppException("Value not found in enum TransType");
        }
    }    
    
    /**
     * @return the reference
     */
    public int getReference() {
        return reference;
    }

    /**
     * @param reference the reference to set
     */
    public void setReference(int reference) {
        this.reference = reference;
    }

    /**
     * @return the percentageDisc
     */
    public double getPercentageDisc() {
        return percentageDisc;
    }

    /**
     * @param percentageDisc the percentageDisc to set
     */
    public void setPercentageDisc(double percentageDisc) {
        this.percentageDisc = percentageDisc;
    }


    /**
     * @return the transDate
     */
    public String getTransDate() {
        return transDate;
    }

    /**
     * @param transDate the transDate to set
     */
    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    /**
     * @return the floatingDisc
     */
    public double getFloatingDisc() {
        return floatingDisc;
    }

    /**
     * @param floatingDisc the floatingDisc to set
     */
    public void setFloatingDisc(double floatingDisc) {
        this.floatingDisc = floatingDisc;
    }

}
