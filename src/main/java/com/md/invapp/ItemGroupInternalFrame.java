/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md.invapp;

import java.awt.Dimension;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import stdClasses.StdFun;

/**
 *
 * @author user
 */
public class ItemGroupInternalFrame extends MaintenanceTableInternalFrame {

    private ItemGroupAO itemGroupAO;
    private ItemGroupRecord itemGroupRec;
    
    private final RuntimeArgs daArgs;

    public ItemGroupInternalFrame(Dimension dimension, RuntimeArgs daArgs) {
        
        super("Maintain Item Groups", daArgs);
        this.daArgs = daArgs;
        initVars(dimension);
        
    }
    
    private void initVars(Dimension dimension) {        
        itemGroupRec = new ItemGroupRecord();
        
        try {
            itemGroupAO = new ItemGroupAO(daArgs.getDbConn(), itemGroupRec);            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(this),
                "SQLException: itemGroup table\n" + e.getMessage(),StdFun.SYSTEM_TITLE ,JOptionPane.ERROR_MESSAGE) ;
        }                
        
        super.initVarsAndDisplay(itemGroupRec, itemGroupAO, dimension);
    }
        
}
