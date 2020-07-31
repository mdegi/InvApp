/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md.invapp;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Martin.Degiorgio
 */
public class ItemTransObjectRecord {

    private final ItemTransHeaderRecord transHeaderRecord;
    private ArrayList<ItemTransRecord> itemTrans;

            
    public final static int NEW_RECORD = 0;
    
    public final static int CANCELLED_RECORD = -1;

    
    public ItemTransObjectRecord() {
    
        transHeaderRecord = new ItemTransHeaderRecord();
        itemTrans = new ArrayList();        
    }
    
    public void initVars() {                
        transHeaderRecord.initVars();
        itemTrans.clear();
    }


    public ItemTransHeaderRecord getHeaderRecord() {
        return transHeaderRecord;
    }
    
    public List<ItemTransRecord> getTransactions() {
        return itemTrans;
    }
    
    public void setTransactions(List<ItemTransRecord> itemTrans) {
        
       this.itemTrans = itemTrans.stream()
            .collect(Collectors.toCollection(ArrayList::new));
    }
    
}
