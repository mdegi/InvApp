/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md.invapp;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.md.invapp.enums.TransType;

/**
 *
 * @author user
 */
public class TransRefNumAO {
    
    private Connection conn = null;

    private Statement qry;

    private StringBuilder sqlStr;

    public static final String TABLE_NAME = "transRefNum";
    
    public static final int INVALID_REF_NUM = -1;
    
    private final String ALL_FIELDS =
                "id, " +
                "saleNum, " +    
                "creditNum, " +
                "quoteNum ";

            
    public TransRefNumAO(Connection conn) throws SQLException {
        this.conn = conn;

        sqlStr = new StringBuilder("");

        try {
            qry = conn.createStatement();
        } catch (SQLException e){
            throw new SQLException( "TransRefNumAO: " + e.getMessage());
        }
    }
    
    public void saveRecord(TransType transType, int num) throws SQLException {        

        sqlStr.setLength(0);
        sqlStr.append("UPDATE ").append(TABLE_NAME);
        sqlStr.append(" SET ");
        
        switch (transType) {
            case INVOICE:
            case CASH_SALE:
               sqlStr.append(" saleNum ");
               break;
            case CREDIT_NOTE:
            case CASH_SALE_RETURN:
               sqlStr.append(" creditNum ");
               break;
            case QUOTATION:
               sqlStr.append(" quoteNum ");
               break;
        }
        sqlStr.append(" = ").append(num);
        qry.execute(sqlStr.toString());
        
    }

    public int getNextTransNum(TransType transType) throws SQLException {
        int nextTransNum = INVALID_REF_NUM ;
        
        sqlStr.setLength(0);
        sqlStr.append("SELECT ");
        
        switch (transType) {
            case INVOICE:
            case CASH_SALE:
               sqlStr.append(" saleNum ");
               break;
            case CREDIT_NOTE:
            case CASH_SALE_RETURN:
               sqlStr.append(" creditNum ");
               break;
            case QUOTATION:
               sqlStr.append(" quoteNum ");
               break;
        }                                      
        sqlStr.append("FROM ").append(TABLE_NAME);

        qry.execute(sqlStr.toString());
        ResultSet rs = qry.getResultSet();

        if (rs.next()) {            
            nextTransNum = rs.getInt(1) + 1;
        }
        
        return nextTransNum;
    }
            
    
}