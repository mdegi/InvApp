/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md.invapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


/**
 *
 * @author user
 */
public class ItemAO {
    
    private Connection conn = null;
    private ItemRecord itemRec;

    private Statement qry;

    private StringBuilder sqlStr;

    public static final String TABLE_NAME = "item";
    
    private final String ALL_FIELDS =
                "itemCode, " +
                "description, " +    
                "serialNum, " +
                "comments, " +
                "categoryId, " +
                "groupId, " +
                "costPrice, " +
                "sellPrice, " +
                "qtyAvailable  ";

            
    public ItemAO(Connection conn, ItemRecord itemRec) throws SQLException {
        this.itemRec = itemRec;
        this.conn = conn;

        sqlStr = new StringBuilder("");

        try {
            qry = conn.createStatement();
        } catch (SQLException e){
            throw new SQLException( "ItemAO: " + e.getMessage());
        }
    }
    
    public void saveRecord() throws SQLException {
        
        if (getRecCount(itemRec.getDsc()) == 0) {
            sqlStr.setLength(0);

            sqlStr.append("INSERT INTO ").append(TABLE_NAME).append(" values (");
            sqlStr.append("?,?,?,?,?,?,?,?,?");
            sqlStr.append(")");

            try (PreparedStatement psmnt = conn.prepareStatement(sqlStr.toString())) {
                psmnt.setString(1, itemRec.getItemCode());
                psmnt.setString(2, itemRec.getDsc());
                psmnt.setString(3, itemRec.getSerialNum());
                psmnt.setString(4, itemRec.getComments());
                psmnt.setInt(5, itemRec.getCategoryId());
                psmnt.setInt(6, itemRec.getGroupId());
                psmnt.setDouble(7, itemRec.getCostPrice());
                psmnt.setDouble(8, itemRec.getSellPrice());
                psmnt.setInt(9, itemRec.getQtyAvailable());
                
                int s = psmnt.executeUpdate();
                if(s <=0 ) {
                    throw new SQLException("SQLException saving new record . . .");
                }
            }
        } else {
            throw new SQLException("Duplicate item found - record not saved");
        }

    }

    private int getRecCount(String dsc) throws SQLException {
            
        int recsCount = 0;
        
        sqlStr.setLength(0);
        sqlStr.append("SELECT COUNT(*) ");
        sqlStr.append("FROM ").append(TABLE_NAME).append(" WHERE description = '").append(dsc).append("'");

        qry.execute(sqlStr.toString());
        ResultSet rs = qry.getResultSet();
        if (rs.next()) {
            recsCount = rs.getInt(1);
        }
        
        return recsCount;
        
    }

    public void fillRecord(String itemCode) throws SQLException {
        itemRec.initVars();
        
        sqlStr.setLength(0);
        sqlStr.append("SELECT ").append(ALL_FIELDS);
        sqlStr.append("FROM ").append(TABLE_NAME).append(" WHERE itemCode = '").append(itemCode).append("' ");

        qry.execute(sqlStr.toString());
        ResultSet rs = qry.getResultSet();

        if (rs.next()) {            
            populateVars(rs, itemRec);
        }
        
    }
            
    public ArrayList<ItemRecord> getAllRecords() throws SQLException {
        
        ArrayList<ItemRecord> list = new ArrayList<>();

        sqlStr.setLength(0);
        sqlStr.append("SELECT ").append(ALL_FIELDS);
        sqlStr.append("FROM ").append(TABLE_NAME).append(" ORDER BY description ");
        
        qry.execute(sqlStr.toString());
        ResultSet rs = qry.getResultSet();

        while (rs.next()) {
            ItemRecord itmRec = new ItemRecord();            
            populateVars(rs, itmRec);
            list.add(itmRec);
        }

        return list;
    }
            
    private void populateVars(ResultSet rs, ItemRecord itmRec) throws SQLException {
            
        itmRec.setItemCode(rs.getString("itemCode"));
        itmRec.setDsc(rs.getString("description"));   
        itmRec.setSerialNum(rs.getString("serialNum"));   
        itmRec.setComments(rs.getString("comments"));   
        
        itmRec.setCategoryId(rs.getInt("categoryId"));
        itmRec.setGroupId(rs.getInt("groupId"));
        
        itmRec.setCostPrice(rs.getDouble("costPrice"));
        itmRec.setSellPrice(rs.getDouble("sellPrice"));

        itmRec.setQtyAvailable(rs.getInt("qtyAvailable"));
    }

    public void deleteRecord(String itemCode) throws SQLException {
        sqlStr.setLength(0);
        sqlStr.append("DELETE FROM ").append(TABLE_NAME).append(" WHERE itemCode = '").append(itemCode).append("' ");
        
        qry.execute(sqlStr.toString());        
    }
    
    public void updateRecord(String itemCode) throws SQLException {        
        int field = 1;
        sqlStr.setLength(0);
        
        sqlStr.append("UPDATE ").append(TABLE_NAME).append(" SET ");
        sqlStr.append("description = ?, ");
        sqlStr.append("serialNum = ?, ");
        sqlStr.append("comments = ?, ");
        
        sqlStr.append("categoryId = ?, ");
        sqlStr.append("groupId = ?, ");
        
        sqlStr.append("costPrice = ?, ");
        sqlStr.append("sellPrice = ?, ");
        sqlStr.append("qtyAvailable = ? ");
                
        sqlStr.append("WHERE itemCode = ? ");

        try (PreparedStatement psmnt = conn.prepareStatement(sqlStr.toString())) {
            psmnt.setString(field++, itemRec.getDsc());
            psmnt.setString(field++, itemRec.getSerialNum());
            
            psmnt.setString(field++, itemRec.getComments());
            psmnt.setInt(field++, itemRec.getCategoryId());
            psmnt.setInt(field++, itemRec.getGroupId());
            psmnt.setDouble(field++, itemRec.getCostPrice());
            psmnt.setDouble(field++, itemRec.getSellPrice());
            
            psmnt.setInt(field++, itemRec.getQtyAvailable());
            
            psmnt.setString(field++, itemRec.getItemCode());
            int s = psmnt.executeUpdate();
            if(s <= 0) {
                throw new SQLException("SQLException updating record . . .");
            }
        }

    }
 
    public String getItemDsc(String itemCode) throws SQLException {
        String dsc;
        
        sqlStr.setLength(0);
        sqlStr.append("SELECT description FROM ").append(TABLE_NAME).append(" WHERE itemCode = '").append(itemCode).append("' ");
        
        qry.execute(sqlStr.toString());
        ResultSet rs = qry.getResultSet();

        rs.next();
        dsc = rs.getString(1);

        return dsc;        
    }
    
    public boolean checkIfCodeExists(String itemCode) throws SQLException{    
        sqlStr.setLength(0);
        sqlStr.append("SELECT COUNT(*) FROM ").append(TABLE_NAME).append(" WHERE itemCode = '").append(itemCode).append("' ");
        
        qry.execute(sqlStr.toString());
        ResultSet rs = qry.getResultSet();

        rs.next();
        return rs.getInt(1) > 0;        
    }
    
    public String fillListSQL() {
        return("SELECT itemCode AS 'Code', description AS 'Dsc'  FROM item ORDER BY itemCode");
    }

    
}