/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md.invapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author user
 */
public class InvAppDBConn {

    private Connection conn;
    
    public final static String DB_USER = "invAppUser";
    public final static String DB_NAME = "invApp";

    public static final String READ_LOCK = "READ";
    public static final String WRITE_LOCK = "WRITE";
    
    private String dbPwd;

    public InvAppDBConn(String dbServer, int dbPort, String pwd) throws ClassNotFoundException, SQLException{
        StringBuilder url = new StringBuilder("jdbc:mysql://");
        url.append(dbServer).append(":").append(dbPort).append("/").append(DB_NAME);
        url.append("?zeroDateTimeBehavior=convertToNull");
        url.append("&characterEncoding=UTF-8");

        if (conn == null) {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url.toString(),
                    DB_USER, pwd);
            
            dbPwd = pwd;
        }    
    }


    public Connection getConnection() {  
        return conn;        
    }
    
    public String getDbPwd() {
        return dbPwd;
    }
    /**
     * Execute SQL command
     * 
     * @param sqlStr
     * @throws java.sql.SQLException
     */
    public void executeSQLcommand(String sqlStr) throws SQLException {

        if ((sqlStr != null) && (!sqlStr.equals(""))) {
            Statement qry = conn.createStatement();
            qry.execute(sqlStr);
        }       
    }

 
    
    /**
     * Returns a Vector containing 3 rows consisting of 3 Vectors being rowData, 
     * columnData and columnSizes in that order. rowData is a Vector of Vectors
     * where each Vector contains the row values separated by columns.
     * columnSizes consists of column sizes as defined in the DBMS
     *
     * <br/>The output of this method is generally used to populate some
     * <code>ScanWindow</code> table or <code>TableSelectionDialog</code>
     *
     * @param sqlStr
     *
     * @throws SQLException
     */
    public static Vector<Vector> getScanEntries(String sqlStr, Connection connection) throws SQLException {

        Vector<Vector> listDet = new Vector<>();
        
        /*if (listDet != null) {
            if (!listDet.isEmpty()) {
                listDet.removeAllElements();
            }
        }*/

        Statement qry;
        ResultSet rs;
        ResultSetMetaData rsmd = null;

        Vector<Vector<Object>> rowData = new Vector<>();
        Vector<String> columnData = new Vector<>();
        Vector<Integer> columnSizes = new Vector<>();

        qry = connection.createStatement();
        rs = qry.executeQuery(sqlStr);
        rsmd = rs.getMetaData();

        for (int x = 1; x <= rsmd.getColumnCount(); x++) {
            columnData.addElement(rsmd.getColumnLabel(x));
            columnSizes.addElement(rsmd.getColumnDisplaySize(x));
        }

        while (rs.next()) {

            Vector<Object> oneRow = new Vector<>();

            for (int x = 1; x <= rsmd.getColumnCount(); x++) { 

                if (rsmd.getColumnTypeName(x).equals("DECIMAL")) {
                    oneRow.addElement(rs.getFloat(x));                    
                } else {
                    oneRow.addElement(rs.getString(x));
                }
            }
            rowData.addElement(oneRow);
        }
        listDet.addElement(rowData);
        listDet.addElement(columnData);
        listDet.addElement(columnSizes);

        return listDet;
    }


    /**
     * Let the current session acquire a <code>WRITE</code> or <code>READ</code> 
     * lock on this selected table(s).<br/>
     * The <code>HashMap</code> key must represent either the table name or the
     * alias for that table.While the value consists of a <code>String</code>
     * array where position [0] of the array should contain the table name
     * (if the key is an alias) or blank if the key is the table name. Position [1]
     * of the array should contain either a <code>READ</code> lock mode or <code>WRITE</code>
     * lock. The lock types can be any value from constants <code>public static final String READ_LOCK</code>
     * or <code>public static final String WRITE_LOCK</code>
     * <p>
     * <b>NOTE: A <code>WRITE</code> lock prevents another session to access the table until the lock is released.<br/>
     * A <code>READ</code> lock prevents locked tables from being updated while can be read from<br/>
     * It is important to release the locks after required updating has been done.</b>
     * </p>
     *
     * @param tables
     *
     * @throws SQLException
     */

    public void lockTables(HashMap<String,String[]> tables) throws SQLException {
        StringBuilder sqlStr = new StringBuilder("LOCK TABLES ");

        int currentPos = 0;
        for (Iterator<String> iter = tables.keySet().iterator(); iter.hasNext(); ) {
            currentPos++;
            String table = iter.next();

            if (tables.get(table)[0].equals("")) {
                sqlStr.append(table);
                sqlStr.append(" ");
            } else {
                sqlStr.append(tables.get(table)[0]);
                sqlStr.append(" ");

                sqlStr.append("AS ");
                sqlStr.append(table);
                sqlStr.append(" ");

            }

            sqlStr.append(tables.get(table)[1]);
            sqlStr.append(" ");

            if (currentPos < tables.size()) {
                sqlStr.append (",");
            }
        }
        executeSQLcommand(sqlStr.toString());
    }


    /**
     * Release any locks acquired on any table
     *
     * @throws SQLException
     */
    public void unLockTables() throws SQLException {
        executeSQLcommand("UNLOCK TABLES");
    }
    

}
