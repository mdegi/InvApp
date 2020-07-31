/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md.invapp;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;


/**
 *
 * @author user
 */
public class ItemCategoryAO extends MaintenanceTableAO {
    
    public static final String TABLE_NAME = "itemCategory";
        
    public ItemCategoryAO(Connection conn, ItemCategoryRecord itemCategoryRec) throws SQLException {
        initVars(conn, TABLE_NAME, itemCategoryRec);
    }
    
    public ArrayList<ItemCategoryRecord> getAllRecords() throws SQLException {               
        return getAllMaintRecords()
                .stream()
                .map(rec -> new ItemCategoryRecord(rec.getId(), rec.getDsc()))
                .collect(Collectors.toCollection(ArrayList<ItemCategoryRecord>::new));
                
    }
}