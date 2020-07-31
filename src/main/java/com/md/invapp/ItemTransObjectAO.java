/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md.invapp;

import com.md.invapp.enums.TransType;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import stdClasses.InvAppException;

/**
 *
 * @author Martin.Degiorgio
 */
public class ItemTransObjectAO {

    private Connection conn = null;
    
    private ItemTransObjectRecord transObjectRec;
    
    private ItemTransHeaderAO itemTransHeaderAO;
    private ItemTransAO itemTransAO;

    private StringBuilder sqlStr;

    private Statement qry;

    
    public ItemTransObjectAO(Connection conn, ItemTransObjectRecord transObjectRec) throws SQLException {
        this.conn = conn;
        this.transObjectRec = transObjectRec;

        itemTransHeaderAO = new ItemTransHeaderAO(conn, transObjectRec.getHeaderRecord());
        itemTransAO = new ItemTransAO(conn);
        
        sqlStr = new StringBuilder("");

        try {
            qry = conn.createStatement();
        } catch (SQLException e){
            throw new SQLException( "itemTransObjectAO: " + e.getMessage());
        }
    }
 
    public void saveRecord() throws SQLException {
        itemTransHeaderAO.saveRecord();   
        itemTransAO.saveRecord(transObjectRec.getTransactions(), transObjectRec.getHeaderRecord().getTransType(), transObjectRec.getHeaderRecord().getReference());
    }

    public List<ItemTransRecord> getItemTransRecords(TransType transType, int reference) throws SQLException, InvAppException {        
        return itemTransAO.getItemTransRecords(transType, reference);        
    
    }    
    
}
