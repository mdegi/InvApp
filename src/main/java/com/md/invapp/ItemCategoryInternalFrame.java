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
public class ItemCategoryInternalFrame extends MaintenanceTableInternalFrame {

    private ItemCategoryAO itemCategoryAO;
    private ItemCategoryRecord itemCategoryRec;
    
    private final RuntimeArgs daArgs;

    public ItemCategoryInternalFrame(Dimension dimension, RuntimeArgs daArgs) {
        
        super("Maintain Item Categories", daArgs);
        this.daArgs = daArgs;
        initVars(dimension);
        
    }
    
    private void initVars(Dimension dimension) {        
        itemCategoryRec = new ItemCategoryRecord();
        
        try {
            itemCategoryAO = new ItemCategoryAO(daArgs.getDbConn(), itemCategoryRec);
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(this),
                "SQLException: itemCategory table\n" + e.getMessage(),StdFun.SYSTEM_TITLE ,JOptionPane.ERROR_MESSAGE) ;
        }                
        
        super.initVarsAndDisplay(itemCategoryRec, itemCategoryAO, dimension);
    }
        
}
