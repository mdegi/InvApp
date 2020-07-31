/*
 * Created on August 11, 2006, 2:22 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.md.invapp;

import java.util.Vector;

import java.awt.Dimension;
 
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import stdClasses.ActionToolBar;

import stdClasses.StdFun;


/**
 * Creates an Item Frame containing all panels needed to maintain Items
 * 
 * @author nla
 */
public class ItemInternalFrame extends InvAppMaintFrame implements ListSelectionListener {

    private ItemPanel itemPanel = null;
    private final InvAppDBConn db = null;
    private ItemAO itemAO = null;
    private ItemRecord itemRecord;
    
    private ItemCategoryRecord itemCategoryRecord;
    private ItemCategoryAO itemCategoryAO;
    
    private ItemGroupRecord itemGroupRegord;
    private ItemGroupAO itemGroupAO;
    
    private final RuntimeArgs runTimeArgs;

    private HashMap<Integer, String> combosValues;    
    
    private boolean newRec;

    // Creates a new instance of ItemInternalFrame
    public ItemInternalFrame(Dimension d, RuntimeArgs runTimeArgs) {

        super("Maintain Stock Items" , 
              true, //resizable
              true, //closable
              true, //maximizable
              true,
              runTimeArgs);//iconifiable

        this.runTimeArgs = runTimeArgs;
        
        init(new Dimension(450,130));
    }

    private void init(Dimension dimension) {
        initVars();
        initComponents();
        
        setSize(dimension);
        pack();
    
        setLocation(dimension);
        
        showMessage("Use Up & Down arrow keys or mouse to scroll through Items . . . .");
        setVisible(true);
    }
    
        
    private void initVars() {        
    
        combosValues = new HashMap<>();
        
        itemRecord = new ItemRecord();
        itemCategoryRecord = new ItemCategoryRecord();
        itemGroupRegord = new ItemGroupRecord();
        
        try {
            itemAO = new ItemAO(runTimeArgs.getDbConn(), itemRecord);
            itemCategoryAO = new ItemCategoryAO(runTimeArgs.getDbConn(), itemCategoryRecord);
            itemGroupAO = new ItemGroupAO(runTimeArgs.getDbConn(), itemGroupRegord);            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(this),
                "Error initialising Access Object:\n" + e.getMessage(),
                StdFun.SYSTEM_TITLE,JOptionPane.ERROR_MESSAGE) ;
        }                
    }
    
    private void initComponents()  {
        
        Vector<Vector> listDet = null;
        
        ArrayList<ItemCategoryRecord> categoriesList = null;
        ArrayList<ItemGroupRecord> groupsList = null;

        try {
            categoriesList = itemCategoryAO.getAllRecords();
            groupsList = itemGroupAO.getAllRecords();
            
            listDet = InvAppDBConn.getScanEntries(itemAO.fillListSQL(), runTimeArgs.getDbConn());
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(this),
                "SQLException: init lists\n" + e.getMessage(),StdFun.SYSTEM_TITLE ,JOptionPane.ERROR_MESSAGE) ;
        }
               
        itemPanel = new ItemPanel(itemRecord, listDet, categoriesList, groupsList, runTimeArgs);
        itemPanel.addListSelectionListener(this);
        itemPanel.addListListener(this);    
        
        initListItems();
        
        newRec = true;
        
        setButtonEnabled(ActionToolBar.SAVE_BUTTON, false);
        setButtonEnabled(ActionToolBar.CANCEL_BUTTON, false);
        setButtonEnabled(ActionToolBar.EDIT_BUTTON, false);
        setButtonEnabled(ActionToolBar.DELETE_BUTTON, false);
        
        addFramePanel(itemPanel);        
    }

   
  
    /**
     * Deletes selected record
     */
    
    @Override
    public void delRecord() {

        if (JOptionPane.showConfirmDialog(JOptionPane.getFrameForComponent(this),
                "Confirm Deletion ?",
                StdFun.SYSTEM_TITLE,JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        
            try {
                itemAO.deleteRecord(itemRecord.getItemCode());
                itemPanel.deleteSelectedEntry();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(this),
                    "SQLException deleting record: item table\n" + e.getMessage(),StdFun.SYSTEM_TITLE ,JOptionPane.ERROR_MESSAGE);                
            }            
        }
        else {
            JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(this),
                    "Selected Record Not Deleted",
                    StdFun.SYSTEM_TITLE,JOptionPane.INFORMATION_MESSAGE) ;
        }
        
        newRec = false; 
        cancelRecord();
       
    }
    
    /**
     * Adds new record
     */
    @Override
    public void newRecord() {
        
        cancelRecord();        
        
        setButtonEnabled(ActionToolBar.NEW_BUTTON, false);        
        setButtonEnabled(ActionToolBar.DELETE_BUTTON, false);
        
        setButtonEnabled(ActionToolBar.EDIT_BUTTON, false);
        setButtonEnabled(ActionToolBar.SAVE_BUTTON, true);
        setButtonEnabled(ActionToolBar.CANCEL_BUTTON, true);

        itemPanel.setEditable(true);
        itemPanel.setEnableSearchList(false);  
        
        newRec = true;
    }

    /** 
     * Clear record details showing in items panel and sets item panel not editable
     */
    @Override
    public void cancelRecord() {
        
        itemPanel.clearForm();   
        itemPanel.addListListener(this);

        itemRecord.initVars();

        setButtonEnabled(ActionToolBar.NEW_BUTTON, true);
        setButtonEnabled(ActionToolBar.EDIT_BUTTON, false);
        setButtonEnabled(ActionToolBar.DELETE_BUTTON, false);
        setButtonEnabled(ActionToolBar.SAVE_BUTTON, false);
      
        itemPanel.setEditable(false);
        itemPanel.setEnableSearchList(true);
                
        if ((itemRecord.getItemCode() != null) && (!itemRecord.getItemCode().equals(""))) {
            itemPanel.setSelectedIndex(itemRecord.getItemCode());
            processPopulateRec();
            itemPanel.requestFocus(ItemPanel.ITEM_TABLE);            
        }

    }

    /**
     * Edit selected record
     */
    @Override
    public void editRecord() {

        itemPanel.setEditable(true);       

        setButtonEnabled(ActionToolBar.NEW_BUTTON, false);
        setButtonEnabled(ActionToolBar.EDIT_BUTTON, false);
        setButtonEnabled(ActionToolBar.DELETE_BUTTON, false);

        setButtonEnabled(ActionToolBar.SAVE_BUTTON, true);
        setButtonEnabled(ActionToolBar.CANCEL_BUTTON, true);
        itemPanel.setEnableSearchList(false);
        
        newRec = false;
    }
    
    /**
     * Save edited or new record
     */

    @Override
    public void saveRecord() {

        itemPanel.fillRecord();
        
        if (entriesComplete()) {
            try {
                if (newRec) {
                    if ((itemRecord.getItemCode() != null ) && (!itemRecord.getItemCode().equals(""))) {
                        itemAO.saveRecord();
                        itemAO.fillRecord(itemRecord.getItemCode());
                        itemPanel.addEntry(new Object[]
                            {itemRecord.getItemCode(),itemRecord.getDsc()});                        
                        cancelRecord();
                    }
                }
                else { 
                    itemAO.updateRecord(itemRecord.getItemCode());
                    itemAO.fillRecord(itemRecord.getItemCode());
                    itemPanel.updateEntry(new Object[]
                            {itemRecord.getItemCode(),itemRecord.getDsc()});
                    cancelRecord(); 
                }
                
                newRec = true;
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(this),
                    "SQLException saving record: item table\n" + e.getMessage(),StdFun.SYSTEM_TITLE ,JOptionPane.ERROR_MESSAGE);                
            } 
        }    
    }
    
    private boolean entriesComplete() {
        boolean entriesComplete = true;
        
        StringBuilder errorList = new StringBuilder("");
        
        boolean validEnt = true;
        if (validEnt) {
            try {
                itemCategoryAO.fillRecord((String)itemPanel.getSelectedItem(ItemPanel.CATEGORY_COMBO));
                itemGroupAO.fillRecord((String)itemPanel.getSelectedItem(ItemPanel.GROUP_COMBO));
                
                itemRecord.setCategoryId(itemCategoryRecord.getId());
                itemRecord.setGroupId(itemGroupRegord.getId());
            } catch (SQLException e) {
                errorList.append("SQLException retrieving foreign key selections\n");
            }            
        }

        //check if required fields have been entered correctlyhere
        
        return entriesComplete;
    }
    
    public void initListItems() {    
        
        try {
            itemPanel.initScanPanel(InvAppDBConn.getScanEntries(itemAO.fillListSQL(), runTimeArgs.getDbConn()));        
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(this),
                "SQLException retreiving records: item table\n" + e.getMessage(),StdFun.SYSTEM_TITLE ,JOptionPane.ERROR_MESSAGE) ;            
        }        
  }

    public void processListSelection(int selectedIndex, String itemCode) {     
        
        try {
            itemAO.fillRecord(itemCode);
            
            itemCategoryAO.fillRecord(itemRecord.getCategoryId());
            itemGroupAO.fillRecord(itemRecord.getGroupId());
            combosValues.clear();
            
            combosValues.put(ItemPanel.CATEGORY_COMBO, itemCategoryRecord.getDsc());
            combosValues.put(ItemPanel.GROUP_COMBO, itemGroupRegord.getDsc());
            
            itemPanel.fillPanel(combosValues);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(this),
                "SQLException processing selection\n" + e.getMessage() ,StdFun.SYSTEM_TITLE ,JOptionPane.ERROR_MESSAGE);                                                        
        }

        if ((selectedIndex == -1) || (itemCode.equals(""))) {
            setButtonEnabled(ActionToolBar.NEW_BUTTON, true);
            setButtonEnabled(ActionToolBar.EDIT_BUTTON, false);
            setButtonEnabled(ActionToolBar.DELETE_BUTTON, false);
            setButtonEnabled(ActionToolBar.SAVE_BUTTON, false);
            setButtonEnabled(ActionToolBar.CANCEL_BUTTON, false);
        } else {
            setButtonEnabled(ActionToolBar.NEW_BUTTON, true);
            setButtonEnabled(ActionToolBar.EDIT_BUTTON, true);
            setButtonEnabled(ActionToolBar.DELETE_BUTTON, true);
            setButtonEnabled(ActionToolBar.SAVE_BUTTON, false);
            setButtonEnabled(ActionToolBar.CANCEL_BUTTON, false);
        }
        
    }

    
    @Override
    public void valueChanged(ListSelectionEvent e) {

        if (e.getValueIsAdjusting()) { // if multiple values selected
            return;
        }
        
        ListSelectionModel lsm = (ListSelectionModel)e.getSource();
        if (!lsm.isSelectionEmpty()) {            
            processPopulateRec();            
        }
    }

    private void processPopulateRec() {

        try {
            itemAO.fillRecord(itemPanel.getSelectedItem(ItemPanel.ITEM_TABLE));
            itemCategoryAO.fillRecord(itemRecord.getCategoryId());
            itemGroupAO.fillRecord(itemRecord.getGroupId());
            
            combosValues.clear();

            combosValues.put(ItemPanel.CATEGORY_COMBO, itemCategoryRecord.getDsc());
            combosValues.put(ItemPanel.GROUP_COMBO, itemGroupRegord.getDsc());


            itemPanel.fillPanel(combosValues);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(this),
                "SQLException processing selection\n" + ex.getMessage() ,StdFun.SYSTEM_TITLE ,JOptionPane.ERROR_MESSAGE);                                                        
        } 

        if ((itemPanel.getSelectedIndex(ItemPanel.ITEM_TABLE) == -1) 
                || (itemPanel.getSelectedItem(ItemPanel.ITEM_TABLE) == null)
                || (itemPanel.getSelectedItem(ItemPanel.ITEM_TABLE).equals(""))) {
            setButtonEnabled(ActionToolBar.NEW_BUTTON, true);
            setButtonEnabled(ActionToolBar.EDIT_BUTTON, false);
            setButtonEnabled(ActionToolBar.DELETE_BUTTON, false);
            setButtonEnabled(ActionToolBar.SAVE_BUTTON, false);
            setButtonEnabled(ActionToolBar.CANCEL_BUTTON, false);
        } else {
            setButtonEnabled(ActionToolBar.NEW_BUTTON, true);
            setButtonEnabled(ActionToolBar.EDIT_BUTTON, true);
            setButtonEnabled(ActionToolBar.DELETE_BUTTON, true);
            setButtonEnabled(ActionToolBar.SAVE_BUTTON, false);
            setButtonEnabled(ActionToolBar.CANCEL_BUTTON, false);
        }
    }

}

