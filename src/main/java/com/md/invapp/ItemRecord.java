/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md.invapp;

/**
 *
 * @author user
 */
public class ItemRecord {

    public static final String NON_STOCK_ITEM = "*";
    
    private String itemCode, dsc, comments, serialNum, lastReceivedDate;
    private int categoryId, groupId, qtyAvailable, lastReceivedQty;
    private double costPrice, sellingPrice ;
    
    public final static int NEW_RECORD = 0;
    
    public final static int CANCELLED_RECORD = -1;
            
    public void initVars() {        
        setItemCode(null);
        setDsc(null);
        
        setComments(null);
        setSerialNum(null);
        
        setCategoryId(NEW_RECORD);
        setGroupId(NEW_RECORD);
        
        setQtyAvailable(0);
        setCostPrice(0);
        setSellPrice(0);
    }
        
    /**
     * @return the serialNum
     */
    public String getSerialNum() {
        return serialNum;
    }

    /**
     * @param serialNum the serialNum to set
     */
    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
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
     * @return the qtyAvailable
     */
    public int getQtyAvailable() {
        return qtyAvailable;
    }

    /**
     * @param qtyAvailable the qtyAvailable to set
     */
    public void setQtyAvailable(int qtyAvailable) {
        this.qtyAvailable = qtyAvailable;
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
     * @return the sellPrice
     */
    public double getSellPrice() {
        return sellingPrice;
    }

    /**
     * @param sellPrice the sellPrice to set
     */
    public void setSellPrice(double sellPrice) {
        this.sellingPrice = sellPrice;
    }

    /**
     * @return the dsc
     */
    public String getDsc() {
        return dsc;
    }

    /**
     * @param dsc the dsc to set
     */
    public void setDsc(String dsc) {
        this.dsc = dsc;
    }


    /**
     * @return the lastReceivedDate
     */
    public String getLastReceivedDate() {
        return lastReceivedDate;
    }

    /**
     * @param lastReceivedDate the lastReceivedDate to set
     */
    public void setLastReceivedDate(String lastReceivedDate) {
        this.lastReceivedDate = lastReceivedDate;
    }

    /**
     * @return the lastReceivedQty
     */
    public int getLastReceivedQty() {
        return lastReceivedQty;
    }

    /**
     * @param lastReceivedQty the lastReceivedQty to set
     */
    public void setLastReceivedQty(int lastReceivedQty) {
        this.lastReceivedQty = lastReceivedQty;
    }

    
}
