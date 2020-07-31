/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
 * @author Martin.Degiorgio
 */
public abstract class MaintenanceTableAO {
    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

    private Connection conn = null;
    private MaintenanceTableRecord maintTableRec;

    private Statement qry;

    private StringBuilder sqlStr;

    private String tableName;
    
    private final String ALL_FIELDS =
                "id, " +
                "description ";
    
    protected void initVars (Connection conn, String tableName, MaintenanceTableRecord maintTableRec) throws SQLException {
        this.maintTableRec = maintTableRec;
        this.conn = conn;
        this.tableName = tableName;

        sqlStr = new StringBuilder("");

        try {
            qry = conn.createStatement();
        } catch (SQLException e){
            throw new SQLException( "AO " + tableName + ": " + e.getMessage());
        }
    }
    
    public void saveRecord() throws SQLException {
        
        if (getRecCount(maintTableRec.getDsc()) == 0) {
            sqlStr.setLength(0);

            sqlStr.append("INSERT INTO ").append(tableName).append(" values (");
            sqlStr.append("?,?");
            sqlStr.append(")");

            try (PreparedStatement psmnt = conn.prepareStatement(sqlStr.toString())) {
                psmnt.setInt(1, maintTableRec.getId());
                psmnt.setString(2, maintTableRec.getDsc());
                
                int s = psmnt.executeUpdate();
                if(s <=0 ) {
                    throw new SQLException("SQLException saving new record . . .");
                }
            }
        } else {
            throw new SQLException("Duplicate entry found - record not saved");
        }

    }

    private int getRecCount(String dsc) throws SQLException {
            
        int recsCount = 0;
        
        sqlStr.setLength(0);
        sqlStr.append("SELECT COUNT(*) ").append(ALL_FIELDS);
        sqlStr.append("FROM ").append(tableName).append(" WHERE description = '").append(dsc).append("'");

        qry.execute(sqlStr.toString());
        ResultSet rs = qry.getResultSet();
        if (rs.next()) {
            recsCount = rs.getInt(1);
        }
        
        return recsCount;
        
    }

    public void fillRecord(int id) throws SQLException {
        maintTableRec.initVars();
        
        sqlStr.setLength(0);
        sqlStr.append("SELECT ").append(ALL_FIELDS);
        sqlStr.append("FROM ").append(tableName).append(" WHERE id = ").append(id).append(" ");

        qry.execute(sqlStr.toString());
        ResultSet rs = qry.getResultSet();

        if (rs.next()) {            
            populateVars(rs, maintTableRec);
        }
        
    }
    
    public void fillRecord(String dsc) throws SQLException {
                
        maintTableRec.initVars();
        
        if (getRecCount(dsc) == 1) {        
            sqlStr.setLength(0);
            sqlStr.append("SELECT ").append(ALL_FIELDS);
            sqlStr.append("FROM ").append(tableName).append(" WHERE description = '").append(dsc).append("'");

            qry.execute(sqlStr.toString());
            ResultSet rs = qry.getResultSet();

            if (rs.next()) {            
                populateVars(rs, maintTableRec);
            }
        } else if (getRecCount(dsc) > 1) {
            throw new SQLException("Duplicate entry found - record not populated");
        }
    }
        
    public ArrayList<MaintenanceTableRecord> getAllMaintRecords() throws SQLException {
        
        ArrayList<MaintenanceTableRecord> list = new ArrayList<>();

        sqlStr.setLength(0);
        sqlStr.append("SELECT ").append(ALL_FIELDS);
        sqlStr.append("FROM ").append(tableName).append(" ORDER BY description ");
        
        qry.execute(sqlStr.toString());
        ResultSet rs = qry.getResultSet();

        while (rs.next()) {
            MaintenanceTableRecord maintRec = new MaintenanceTableRecord();            
            populateVars(rs, maintRec);
            list.add(maintRec);
        }

        return list;
    }
            
    private void populateVars(ResultSet rs, MaintenanceTableRecord tableRecord )throws SQLException {
            
        tableRecord.setId(rs.getInt("id"));
        tableRecord.setDsc(rs.getString("description"));   
    }

    public void deleteRecord(int id) throws SQLException {

        sqlStr.setLength(0);
        sqlStr.append("DELETE FROM ").append(tableName).append(" WHERE id = ").append(id);
        
        qry.execute(sqlStr.toString());        
    }
    
    public void updateRecord(int id) throws SQLException {
        
        int field = 1;
        sqlStr.setLength(0);
        
        sqlStr.append("UPDATE ").append(tableName).append(" SET ");
        sqlStr.append("description = ? ");
                
        sqlStr.append("WHERE id = ? ");

        try (PreparedStatement psmnt = conn.prepareStatement(sqlStr.toString())) {
            psmnt.setString(field++, maintTableRec.getDsc());
            psmnt.setInt(field++, maintTableRec.getId());
            
            int s = psmnt.executeUpdate();
            if(s <= 0) {
                throw new SQLException("SQLException updating record . . .");
            }
        }

    }
    
}
