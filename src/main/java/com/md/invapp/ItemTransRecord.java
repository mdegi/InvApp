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
public class ItemTransRecord {

    private String itemCode, itemDsc, comments, transType;
    
    private int reference, groupId, categoryId, transQty;
    
    private double costPrice, sellingPrice, vatRate, sellDiscount ;
    
    public final static int NEW_RECORD = 0;
    
    public final static int CANCELLED_RECORD = -1;
            
    public void initVars() {                
    }
    
    /**
     * @return the itemCode
     */
    public String getItemCode() {
        return itemCode;
    }

    /**
     * @param itemCode the itemCode to set
     */
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    /**
     * @return the itemDsc
     */
    public String getItemDsc() {
        return itemDsc;
    }

    /**
     * @param itemDsc the itemDsc to set
     */
    public void setItemDsc(String itemDsc) {
        this.itemDsc = itemDsc;
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
     * @return the groupId
     */
    public int getGroupId() {
        return groupId;
    }

    /**
     * @param groupId the groupId to set
     */
    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    /**
     * @return the categoryId
     */
    public int getCategoryId() {
        return categoryId;
    }

    /**
     * @param categoryId the categoryId to set
     */
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * @return the transQty
     */
    public int getTransQty() {
        return transQty;
    }

    /**
     * @param transQty the transQty to set
     */
    public void setTransQty(int transQty) {
        this.transQty = transQty;
    }

    /**
     * @return the costPrice
     */
    public double getCostPrice() {
        return costPrice;
    }

    /**
     * @param costPrice the costPrice to set
     */
    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }

    /**
     * @return the sellingPrice
     */
    public double getSellingPrice() {
        return sellingPrice;
    }

    /**
     * @param sellingPrice the sellingPrice to set
     */
    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    /**
     * @return the vatRate
     */
    public double getVatRate() {
        return vatRate;
    }

    /**
     * @param vatRate the vatRate to set
     */
    public void setVatRate(double vatRate) {
        this.vatRate = vatRate;
    }

    /**
     * @return the sellDiscount
     */
    public double getSellDiscount() {
        return sellDiscount;
    }

    /**
     * @param sellDiscount the sellDiscount to set
     */
    public void setSellDiscount(double sellDiscount) {
        this.sellDiscount = sellDiscount;
    }    
    
}
