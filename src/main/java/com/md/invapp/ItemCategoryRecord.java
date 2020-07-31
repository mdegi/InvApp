/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md.invapp;

/**
 *
 * @author user
 */
public class ItemCategoryRecord extends MaintenanceTableRecord {
    
    public ItemCategoryRecord() {
        
    }
    
    public ItemCategoryRecord(int id, String dsc) {        
        setId(id);
        setDsc(dsc);
    }
    
}
