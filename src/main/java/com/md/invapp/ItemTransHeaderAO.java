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
import stdClasses.InvAppException;

/**
 *
 * @author Martin.Degiorgio
 */
public class ItemTransHeaderAO {

    public static final String TABLE_NAME = "itemTransHeader";
    
    private Connection conn = null;
    private ItemTransHeaderRecord itemTransHeaderRec;

    private StringBuilder sqlStr;

    private Statement qry;

    private final String ALL_FIELDS =
            "transType, " +
            "reference, " +    
            "transDate, " +
            "accNumber, " +
            "accName, " +
            "accAddLine1, " +
            "accAddLine2, " +
            "accAddLine3, " +
            "accVatNum, " +
            "percentageDisc, " +
            "floatingDisc, " +
            "comments ";

    public ItemTransHeaderAO(Connection conn, ItemTransHeaderRecord itemTransHeaderRec) throws SQLException {

        this.conn = conn;
        this.itemTransHeaderRec = itemTransHeaderRec;

        sqlStr = new StringBuilder("");

        try {
            qry = conn.createStatement();
        } catch (SQLException e){
            throw new SQLException( "itemTransHeaderAO: " + e.getMessage());
        }
    }
 
    public void saveRecord() throws SQLException {

        sqlStr.setLength(0);

        sqlStr.append("INSERT INTO ").append(TABLE_NAME).append(" values ('");
        sqlStr.append(itemTransHeaderRec.getTransType().toString()).append("', ");
        sqlStr.append(itemTransHeaderRec.getReference()).append(", '");
        sqlStr.append(itemTransHeaderRec.getTransDate()).append("', '");        
        sqlStr.append(itemTransHeaderRec.getAccNumber()).append("', '");
        sqlStr.append(itemTransHeaderRec.getAccName()).append("', '");
        sqlStr.append(itemTransHeaderRec.getAccAddLine1()).append("', '");
        sqlStr.append(itemTransHeaderRec.getAccAddLine2()).append("', '");
        sqlStr.append(itemTransHeaderRec.getAccAddLine3()).append("', '");
        sqlStr.append(itemTransHeaderRec.getAccVatNum()).append("', ");
        sqlStr.append(itemTransHeaderRec.getPercentageDisc()).append(", ");
        sqlStr.append(itemTransHeaderRec.getFloatingDisc()).append(", '");
        sqlStr.append(itemTransHeaderRec.getComments()).append("' ");
        sqlStr.append(")");
        qry.execute(sqlStr.toString());                    
    }

    public void fillRecord(TransType transType, int reference) throws SQLException, InvAppException {

        sqlStr.setLength(0);

        sqlStr.append("SELECT ");
        sqlStr.append(ALL_FIELDS);
        sqlStr.append("FROM ").append(TABLE_NAME).append(" WHERE transType = '").append(transType).append("' ");
        sqlStr.append("AND reference = ").append(reference);
        
        qry.execute(sqlStr.toString());
        ResultSet rs = qry.getResultSet();

        while (rs.next()) {
            itemTransHeaderRec.setTransType("transType");
            itemTransHeaderRec.setReference(rs.getInt("reference"));
            itemTransHeaderRec.setTransDate(rs.getString("transDate"));
            itemTransHeaderRec.setAccNumber(rs.getString("accNumber"));
            itemTransHeaderRec.setAccName(rs.getString("accName"));
            itemTransHeaderRec.setAccAddLine1(rs.getString("accAddLine1"));
            itemTransHeaderRec.setAccAddLine2(rs.getString("accAddLine2"));
            itemTransHeaderRec.setAccAddLine3(rs.getString("accAddLine3"));
            itemTransHeaderRec.setAccVatNum(rs.getString("accVatNum"));
            itemTransHeaderRec.setPercentageDisc(rs.getDouble("percentageDisc"));
            itemTransHeaderRec.setPercentageDisc(rs.getDouble("floatingDisc"));
            itemTransHeaderRec.setTransDate(rs.getString("comments"));
        }            
    }

    
}
