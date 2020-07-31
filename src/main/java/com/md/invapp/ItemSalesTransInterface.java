/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md.invapp;


/**
 *
 * @author Martin.Degiorgio
 */
public interface ItemSalesTransInterface {
    
    public boolean entriesComplete();    
    public void closeTransaction() ;
    public void showMessageinDalog(String message, int errorType);
        
}
