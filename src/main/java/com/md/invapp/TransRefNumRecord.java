/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md.invapp;

/**
 *
 * @author martin.degiorgio
 */
public class TransRefNumRecord {

    private int id, saleNum, quoteNum, creditNum;
   
    public void initVars() {
        id = 0;
        saleNum = 0;
        quoteNum = 0;
        creditNum = 0;
    }
    
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the saleNum
     */
    public int getSaleNum() {
        return saleNum;
    }

    /**
     * @param saleNum the saleNum to set
     */
    public void setSaleNum(int saleNum) {
        this.saleNum = saleNum;
    }

    /**
     * @return the quoteNum
     */
    public int getQuoteNum() {
        return quoteNum;
    }

    /**
     * @param quoteNum the quoteNum to set
     */
    public void setQuoteNum(int quoteNum) {
        this.quoteNum = quoteNum;
    }

    /**
     * @return the creditNum
     */
    public int getCreditNum() {
        return creditNum;
    }

    /**
     * @param creditNum the creditNum to set
     */
    public void setCreditNum(int creditNum) {
        this.creditNum = creditNum;
    }
    
}
