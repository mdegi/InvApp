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
public class InvAppUserAO {
    
    private Connection conn = null;
    private InvAppUserRec userRec;

    private Statement qry;

    private StringBuilder sqlStr;

    public static final String TABLE_NAME = "invAppUser";
    
    private final String ALL_FIELDS =
                "id, " +
                "userCode, " +
                "userPass, " +
                "userGrp, " +
                "readOnly " ;
    
        
    public InvAppUserAO(Connection conn, InvAppUserRec userRec) throws SQLException {
                
        this.userRec = userRec;
        this.conn = conn;

        sqlStr = new StringBuilder("");

        try {
            qry = conn.createStatement();
        } catch (SQLException e){
            throw new SQLException( "InvAppUserAO: " + e.getMessage());
        }
    }
    
    public void saveRecord() throws SQLException {

        sqlStr.setLength(0);

        sqlStr.append("INSERT INTO ").append(TABLE_NAME).append(" values (");
        sqlStr.append("?,?,?,?,?");
        sqlStr.append(")");
        
        PreparedStatement psmnt = conn.prepareStatement(sqlStr.toString());
        psmnt.setInt(1, userRec.getId());
        psmnt.setString(2, userRec.getUserCode());
        psmnt.setString(3, userRec.getUserPass());
        psmnt.setString(4, userRec.getUserGrp());
        psmnt.setInt(5, getIntIndicator(userRec.isReadOnly()));

        int s = psmnt.executeUpdate();
        if(s<=0) {
            throw new SQLException("SQLException saving new record . . .");
        }

        psmnt.close();

    }

    private int getRecCount(String dsc) throws SQLException {
            
        int recsCount = 0;
        
        sqlStr.setLength(0);
        sqlStr.append("SELECT COUNT(*) ").append(ALL_FIELDS);        
        sqlStr.append("FROM ").append(TABLE_NAME).append(" WHERE userCode = '").append(dsc).append("' ");

        qry.execute(sqlStr.toString());
        ResultSet rs = qry.getResultSet();
        if (rs.next()) {
            recsCount = rs.getInt(1);
        }
        
        return recsCount;
        
    }
    
    public void fillRecord(int id) throws SQLException {
        
        userRec.initVars();        

        sqlStr.setLength(0);
        sqlStr.append("SELECT ").append(ALL_FIELDS);
        sqlStr.append("FROM ").append(TABLE_NAME).append(" WHERE id = ").append(id).append(" ");

        qry.execute(sqlStr.toString());
        ResultSet rs = qry.getResultSet();

        if (rs.next()) {
            populateVars(rs, userRec);
        }

    }
    
    public void fillRecord(String dsc) throws SQLException {
        
        userRec.initVars();        
        if (getRecCount(dsc) == 1) {
            sqlStr.setLength(0);
            sqlStr.append("SELECT ").append(ALL_FIELDS);
            sqlStr.append("FROM ").append(TABLE_NAME).append(" WHERE userCode = '").append(dsc).append("' ");

            qry.execute(sqlStr.toString());
            ResultSet rs = qry.getResultSet();

            if (rs.next()) {
                populateVars(rs, userRec);

            }
        }
        else if (getRecCount(dsc) > 1) {
            throw new SQLException("Duplicate " + TABLE_NAME + " found - Record not populated");
            
        }
    }

    private void populateVars(ResultSet rs, InvAppUserRec usRec) throws SQLException {

        usRec.setId(rs.getInt("id"));
        usRec.setUserCode(rs.getString("userCode"));
        usRec.setUserPass(rs.getString("userPass"));
        usRec.setUserGrp(rs.getString("userGrp"));
        usRec.setReadOnly(rs.getBoolean("readOnly"));

    }

    public ArrayList<InvAppUserRec> getRecord(String dsc) throws SQLException {
        
        ArrayList<InvAppUserRec> list = new ArrayList<>();

        
        qry.execute(sqlStr.toString());
        ResultSet rs = qry.getResultSet();

        while (rs.next()) {
            InvAppUserRec usRec = new InvAppUserRec();
            
            populateVars(rs, usRec);
            
            list.add(usRec);
            break; // 1 record only equals empCode
        }

        return list;
    }
    
    public ArrayList<InvAppUserRec> getAllRecords() throws SQLException {
        
        ArrayList<InvAppUserRec> list = new ArrayList<>();

        sqlStr.setLength(0);
        sqlStr.append("SELECT ").append(ALL_FIELDS);
        sqlStr.append("FROM ").append(TABLE_NAME).append(" ORDER BY userCode ");
        
        qry.execute(sqlStr.toString());
        ResultSet rs = qry.getResultSet();

        while (rs.next()) {
            InvAppUserRec usRec = new InvAppUserRec();
            
            usRec.setId(rs.getInt("id"));
            usRec.setUserCode(rs.getString("userCode"));
            usRec.setUserPass(rs.getString("userPass"));
            usRec.setUserGrp(rs.getString("userGrp"));
            usRec.setReadOnly(rs.getBoolean("readOnly"));
        
            list.add(usRec);
        }

        return list;
    }
            
    public void deleteRecord(int id) throws SQLException {

        sqlStr.setLength(0);
        sqlStr.append("DELETE FROM ").append(TABLE_NAME).append(" WHERE id = ").append(id);
        
        qry.execute(sqlStr.toString());        
    }
    
    public void updateRecord(int id) throws SQLException {
        
        int field = 1;
        sqlStr.setLength(0);
        
        sqlStr.append("UPDATE ").append(TABLE_NAME).append(" SET ");
        sqlStr.append("userCode = ?, ");
        sqlStr.append("userPass = ?, ");
        sqlStr.append("userGrp = ?, ");
        sqlStr.append("readOnly = ? ");
        
        sqlStr.append("WHERE id = ? ");

        PreparedStatement psmnt = conn.prepareStatement(sqlStr.toString());

        psmnt.setString(field++, userRec.getUserCode());
        psmnt.setString(field++, userRec.getUserPass());
        psmnt.setString(field++, userRec.getUserGrp());
        psmnt.setInt(field++, getIntIndicator(userRec.isReadOnly()));
        
        psmnt.setInt(field++, userRec.getId());
        
        int s = psmnt.executeUpdate();
        if(s <= 0) {
            throw new SQLException("SQLException updating record . . .");
        }

        psmnt.close();

    }
 
    private int getIntIndicator(boolean boolState) {

        int intIndicator;
        if (boolState) {
            intIndicator = 1;
        } else {
            intIndicator = 0;
        }

        return intIndicator;
    }
    
}