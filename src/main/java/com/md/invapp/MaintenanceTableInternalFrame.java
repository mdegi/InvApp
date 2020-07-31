/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md.invapp;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import stdClasses.ActionToolBar;
import stdClasses.StdFun;

/**
 *
 * @author user
 */
public class MaintenanceTableInternalFrame extends InvAppMaintFrame{

    private MaintenanceTablePanel mntTablePanel;
    
    private MaintenanceTableAO maintTableAO;
    private MaintenanceTableRecord maintTableRec;
    
    
    public MaintenanceTableInternalFrame(String title, RuntimeArgs daArgs) {        
        super(title, false, true, 
             false, false, daArgs);
    }
    
    
    protected void initVarsAndDisplay(MaintenanceTableRecord mntTableRec, MaintenanceTableAO mntTableAO, Dimension dimension) {
        this.maintTableRec = mntTableRec;
        this.maintTableAO = mntTableAO;
        
        initComponents();        
        
        setSize(500,200);
        pack();
        
        setLocation(dimension);
        
        showMessage("Enter new / amend or delete existing records . . . .");
        setVisible(true); 
        
    }
    
    private void initComponents()  {        
        mntTablePanel = new MaintenanceTablePanel(maintTableRec);

        initListItems();
                
        setButtonEnabled(ActionToolBar.SAVE_BUTTON, false);
        setButtonEnabled(ActionToolBar.CANCEL_BUTTON, false);
        setButtonEnabled(ActionToolBar.EDIT_BUTTON, false);
        setButtonEnabled(ActionToolBar.DELETE_BUTTON, false);
        
        mntTablePanel.addListSelectionListener((ActionEvent e) -> {
            setButtonEnabled(ActionToolBar.NEW_BUTTON, true);
            setButtonEnabled(ActionToolBar.SAVE_BUTTON, false);
            setButtonEnabled(ActionToolBar.CANCEL_BUTTON, false);
            
            if (mntTablePanel.getSelectedIndex() == 0) {
                setButtonEnabled(ActionToolBar.EDIT_BUTTON, false);
                setButtonEnabled(ActionToolBar.DELETE_BUTTON, false);
            } else {
                setButtonEnabled(ActionToolBar.EDIT_BUTTON, true);
                setButtonEnabled(ActionToolBar.DELETE_BUTTON, true);
            }
        });
        
        addFramePanel(mntTablePanel);
        
    }
    
    public void initListItems() {
        
        try {
            mntTablePanel.initListItems(maintTableAO.getAllMaintRecords());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(this),
                "SQLException retreiving records:\n" + e.getMessage(),StdFun.SYSTEM_TITLE ,JOptionPane.ERROR_MESSAGE) ;            
        }
    }

    @Override
    public void delRecord() {
        
        if (mntTablePanel.getSelectedIndex() != 0) {
            mntTablePanel.fillRecord();
            try {
                maintTableAO.deleteRecord(maintTableRec.getId());
                
                initListItems();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(this),
                    "SQLException deleting record:\n" + e.getMessage(),StdFun.SYSTEM_TITLE ,JOptionPane.ERROR_MESSAGE);                
            }
        }
        
        cancelRecord();
    }

    @Override
    public void newRecord() {
        
        cancelRecord();
        
        setButtonEnabled(ActionToolBar.NEW_BUTTON, false);
        setButtonEnabled(ActionToolBar.EDIT_BUTTON, false);
        setButtonEnabled(ActionToolBar.DELETE_BUTTON, false);
        
        setButtonEnabled(ActionToolBar.SAVE_BUTTON, true);
        setButtonEnabled(ActionToolBar.CANCEL_BUTTON, true);

        maintTableRec.setId(MaintenanceTableRecord.NEW_RECORD);
        mntTablePanel.setEditable(true);        
        mntTablePanel.setEnableSearchList(false);
        
    }

    @Override
    public void editRecord() {
        
        if (mntTablePanel.getSelectedIndex() != 0) {
            mntTablePanel.setEditable(true);       

            setButtonEnabled(ActionToolBar.NEW_BUTTON, false);
            setButtonEnabled(ActionToolBar.EDIT_BUTTON, false);
            setButtonEnabled(ActionToolBar.DELETE_BUTTON, false);

            setButtonEnabled(ActionToolBar.SAVE_BUTTON, true);
            setButtonEnabled(ActionToolBar.CANCEL_BUTTON, true);
            mntTablePanel.setEnableSearchList(false);
        }
    }

    @Override
    public void cancelRecord() {
    
        mntTablePanel.clearForm();        
        maintTableRec.initVars();
        
        setButtonEnabled(ActionToolBar.NEW_BUTTON, true);
        setButtonEnabled(ActionToolBar.EDIT_BUTTON, false);
        setButtonEnabled(ActionToolBar.DELETE_BUTTON, false);
        setButtonEnabled(ActionToolBar.SAVE_BUTTON, false);
      
        mntTablePanel.setEditable(false);
        mntTablePanel.setEnableSearchList(true);
    }

    @Override
    public void saveRecord() {
                
        mntTablePanel.fillRecord();
        
        if ((maintTableRec.getDsc() != null) && (!maintTableRec.getDsc().equals(""))) {
            try {
                if (maintTableRec.getId() == MaintenanceTableRecord.NEW_RECORD) {
                    maintTableAO.saveRecord();
                } else if (maintTableRec.getId() != MaintenanceTableRecord.CANCELLED_RECORD) { 
                    maintTableAO.updateRecord(maintTableRec.getId());
                }

                cancelRecord();            
                initListItems();

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(this),
                    "SQLException saving record:\n" + e.getMessage(),StdFun.SYSTEM_TITLE ,JOptionPane.ERROR_MESSAGE);                
            }        
        }
    }
    
}
