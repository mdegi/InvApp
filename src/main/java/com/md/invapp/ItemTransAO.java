/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md.invapp;

import com.md.invapp.enums.TransType;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import stdClasses.InvAppException;

/**
 *
 * @author Martin.Degiorgio
 */
public class ItemTransAO {

    public static final String TABLE_NAME = "itemTrans";
    
    private StringBuilder sqlStr;

    private Statement qry;

    private final String ALL_FIELDS = 
            "transType, " +
            "reference, " +    
            "itemCode, " +
            "itemDsc, " +
            "categoryId, " +
            "groupId, " +
            "costPrice, " +
            "sellingPrice, " +
            "vatRate, " +
            "sellDiscount, " +
            "transQty, " +
            "comments ";
    
    public ItemTransAO(Connection conn) throws SQLException {

        sqlStr = new StringBuilder("");

        try {
            qry = conn.createStatement();
        } catch (SQLException e){
            throw new SQLException( "itemTransAO: " + e.getMessage());
        }
    }
 
    public void saveRecord(ItemTransRecord itemTransRec) throws SQLException {

        sqlStr.setLength(0);

        sqlStr.append("INSERT INTO ").append(TABLE_NAME).append(" values (");
        sqlStr.append("'").append(itemTransRec.getTransType().toString());
        sqlStr.append("', ").append(itemTransRec.getReference());
        sqlStr.append(", '").append(itemTransRec.getItemCode());
        sqlStr.append("', '").append(itemTransRec.getItemDsc());
        sqlStr.append("', ").append(itemTransRec.getCategoryId());
        sqlStr.append(", ").append(itemTransRec.getGroupId());
        sqlStr.append(", ").append(itemTransRec.getCostPrice());
        sqlStr.append(", ").append(itemTransRec.getSellingPrice());
        sqlStr.append(", ").append(itemTransRec.getVatRate());
        sqlStr.append(", ").append(itemTransRec.getSellDiscount());
        sqlStr.append(", ").append(itemTransRec.getTransQty());
        sqlStr.append(", '").append(itemTransRec.getComments()).append("' ");
        sqlStr.append(")");

        qry.execute(sqlStr.toString());                            
    }

    
    public void saveRecord(List<ItemTransRecord> itemTransRecList, TransType trType, int referenceNum) throws RuntimeException {
        itemTransRecList.stream()
            .forEach(t -> {
                try {
                    t.setTransType(trType);
                    t.setReference(referenceNum);
                    t.setTransQty(t.getTransQty() * trType.getMultiplyingFactor());
                    
                    t.setCategoryId(1);
                    t.setGroupId(1);
                    saveRecord(t);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    
    public List<ItemTransRecord> getItemTransRecords(TransType transType, int reference) throws SQLException, InvAppException {
        
        List<ItemTransRecord> itemTrans = new ArrayList();
        
        sqlStr.setLength(0);
        sqlStr.append("SELECT ");
        sqlStr.append(ALL_FIELDS);
        sqlStr.append("FROM ").append(TABLE_NAME).append(" WHERE transType = '").append(transType).append("' ");
        sqlStr.append("AND reference = ").append(reference);

        qry.execute(sqlStr.toString());
        ResultSet rs = qry.getResultSet();        
        
        while (rs.next()) {
            ItemTransRecord transRecord = new ItemTransRecord();
            transRecord.setTransType(rs.getString("transType"));
            transRecord.setReference(rs.getInt("reference"));
            transRecord.setItemCode(rs.getString("itemCode"));
            transRecord.setItemDsc(rs.getString("itemDsc"));
            transRecord.setCategoryId(rs.getInt("categoryId"));
            transRecord.setGroupId(rs.getInt("groupId"));
            transRecord.setCostPrice(rs.getDouble("costPrice"));
            transRecord.setSellingPrice(rs.getDouble("sellingPrice"));
            transRecord.setVatRate(rs.getDouble("vatRate"));
            transRecord.setSellDiscount(rs.getDouble("sellDiscount"));
            transRecord.setTransQty(rs.getInt("transQty"));
            transRecord.setComments(rs.getString("comments"));
            
            itemTrans.add(transRecord);            
        }      
        return itemTrans;
    }    
        
}
