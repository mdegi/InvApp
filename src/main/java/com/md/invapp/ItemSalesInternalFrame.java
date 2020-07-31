/*
 * Created on August 11, 2006, 2:22 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.md.invapp;


import com.md.invapp.enums.TransType;

import java.awt.Dimension;
import java.awt.HeadlessException;
 
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import stdClasses.ActionToolBar;
import stdClasses.ScanWindow;

import stdClasses.StdFun;


/**
 * Creates an Item Frame containing all panels needed to maintain Items
 * 
 * @author nla
 */
public class ItemSalesInternalFrame extends InvAppMaintFrame 
        implements ListSelectionListener, ItemSalesHeaderInterface, ItemSalesTransInterface, ItemSalesMainFrameInterface {

    private ItemSalesPanel itemSalesPanel = null;
    private final InvAppDBConn db;
    
    private TransRefNumAO transRefNumAO;
    
    private ItemTransObjectAO transObjectAO;
    private ItemTransObjectRecord transObjectRecord;
    
    private ItemAO itemAO = null;
    private ItemRecord itemRecord;    
    
    private final RuntimeArgs runTimeArgs;   

    private ScanWindow scanWindow;
        
    
    // Creates a new instance of ItemInternalFrame
    public ItemSalesInternalFrame(Dimension d, RuntimeArgs runTimeArgs) {

        super("Sales Transaction" , 
              true, //resizable
              true, //closable
              true, //maximizable
              true,
              runTimeArgs);//iconifiable

        this.runTimeArgs = runTimeArgs;
        
        db = runTimeArgs.getInvAppDbConn(); //db should be of type InvAppDBConn in RuntimeArgs
        
        init(new Dimension(450,130));
    }

    private void init(Dimension dimension) {
        initVars();
        initComponents();
        
        setSize(dimension);
        pack();
    
        setLocation(dimension);
        
        showMessage("Sales of Stock Items . . . .");
        setVisible(true);
    }
    
        
    private void initVars() {          
        
        itemRecord = new ItemRecord();
        transObjectRecord = new ItemTransObjectRecord();
                
        try {
            itemAO = new ItemAO(runTimeArgs.getDbConn(), itemRecord);
            transObjectAO = new ItemTransObjectAO(runTimeArgs.getDbConn(), transObjectRecord);  
            
            transRefNumAO = new TransRefNumAO(runTimeArgs.getDbConn());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(this),
                "Error initialising Access Object:\n" + e.getMessage(),
                StdFun.SYSTEM_TITLE,JOptionPane.ERROR_MESSAGE) ;
        }                
    }
    
    private void initComponents()  {        
                       
        itemSalesPanel = new ItemSalesPanel(transObjectRecord, itemRecord, this, this, runTimeArgs);
        itemSalesPanel.addSalesMainFrameInterface(this);
        
        itemSalesPanel.addItemCodeSanListener(new ItemScanListener() {
            @Override
            public void initScanList() {
                scanItemCode();
            }

            @Override
            public void itemCodeFocusLost() {
                scanItemCodeFocusLost();
            }
        });
        
        setButtonEnabled(ActionToolBar.SAVE_BUTTON, false);
        setButtonEnabled(ActionToolBar.CANCEL_BUTTON, false);
        setButtonEnabled(ActionToolBar.EDIT_BUTTON, false);
        setButtonEnabled(ActionToolBar.DELETE_BUTTON, false);
        
        addFramePanel(itemSalesPanel);        
    }

    private void scanItemCode() {

        try {
            scanWindow = new ScanWindow(InvAppDBConn.getScanEntries(itemAO.fillListSQL(), runTimeArgs.getDbConn()), "Scan Items",
                    getLocation(), (String selCode) -> {
                        String selectedCode = selCode.trim();
                        
                        try {
                            itemAO.fillRecord(selectedCode);
                            //itemAO.getItemDsc(selectedCode);
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(null,
                                    "InvalidRecordException display selected row:\n" + e.getMessage(),
                                    StdFun.SYSTEM_TITLE,JOptionPane.ERROR_MESSAGE) ;
                        }
            });

            scanWindow.showListWindow();
            scanWindow.initSearchPanel(JOptionPane.getFrameForComponent(this),"Search Item");

        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(this),
                    "Error Scan Item Code:-\n" + e.getMessage(),
                    StdFun.SYSTEM_TITLE,JOptionPane.ERROR_MESSAGE) ;
        }
    }

    public void scanItemCodeFocusLost() {
    
        if ((itemRecord.getItemCode() != null) && (!itemRecord.getItemCode().equals(""))){    
            try {
                if (itemAO.checkIfCodeExists(itemRecord.getItemCode()) == true) {
                   itemAO.fillRecord(itemRecord.getItemCode());
                } else {
                    itemRecord.initVars();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null,
                    "SQLException Scan item code focus lost:\n" + e.getMessage(),
                    StdFun.SYSTEM_TITLE,JOptionPane.ERROR_MESSAGE) ;
            }
            
        } else {
            itemRecord.initVars();
        }    
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

        itemSalesPanel.initNewTrans();
        
        itemSalesPanel.setHeaderFooterEditable(true);
        itemSalesPanel.setTransPanelEditable(false);
        itemSalesPanel.setEnableSearchList(false);  
        
    }

    /** 
     * Clear record details showing in items panel and sets item panel not editable
     */
    @Override
    public void cancelRecord() {
        
        itemSalesPanel.clearForm();   

        itemRecord.initVars();

        setButtonEnabled(ActionToolBar.NEW_BUTTON, true);
        setButtonEnabled(ActionToolBar.EDIT_BUTTON, false);
        setButtonEnabled(ActionToolBar.DELETE_BUTTON, false);
        setButtonEnabled(ActionToolBar.SAVE_BUTTON, false);
        setButtonEnabled(ActionToolBar.CANCEL_BUTTON, false);
      
        itemSalesPanel.setEditable(false);
        itemSalesPanel.setEnableSearchList(true);
                
        if ((itemRecord.getItemCode() != null) && (!itemRecord.getItemCode().equals(""))) {
            //itemSalesPanel.setSelectedIndex(itemRecord.getItemCode());
        }

    }

    /**
     * Edit selected record
     */
    @Override
    public void editRecord() {

        itemSalesPanel.setEditable(true);       

        setButtonEnabled(ActionToolBar.NEW_BUTTON, false);
        setButtonEnabled(ActionToolBar.EDIT_BUTTON, false);
        setButtonEnabled(ActionToolBar.DELETE_BUTTON, false);

        setButtonEnabled(ActionToolBar.SAVE_BUTTON, true);
        setButtonEnabled(ActionToolBar.CANCEL_BUTTON, true);
        
    }
    
    /**
     * Save edited or new record
     */
    @Override
    public void saveRecord() {
        
        //fill itemSalesHeaderRec here .....
        if (entriesComplete()) {            
            int confirm = JOptionPane.showOptionDialog(null, 
                    "Confirm\nSave changes ?", "Save Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (confirm == 0) {
                
                itemSalesPanel.fillRecord();
                
                try {
                    //check transaction commit not working
                    //check to clear row not only when presseing button but also when pressing f10
                    //clear table after ready to get ready fro next trabsaction
                    //getItemRecord before saving to save group Id and category ID
                    
                    db.getConnection().setAutoCommit(false); // save when all records are updated

                    HashMap<String,String[]> tables = new HashMap<>();
                    tables.put(ItemTransHeaderAO.TABLE_NAME ,new String[]{"",InvAppDBConn.WRITE_LOCK});
                    tables.put(ItemTransAO.TABLE_NAME       ,new String[]{"",InvAppDBConn.WRITE_LOCK});
                    tables.put(ItemAO.TABLE_NAME            ,new String[]{"",InvAppDBConn.WRITE_LOCK});
                    tables.put(TransRefNumAO.TABLE_NAME     ,new String[]{"",InvAppDBConn.WRITE_LOCK});                    
                    db.lockTables(tables);
                    
                    transObjectAO.saveRecord();
                    transRefNumAO.saveRecord(transObjectRecord.getHeaderRecord().getTransType(), getNextTransNumber(transObjectRecord.getHeaderRecord().getTransType()));
                    
                    db.getConnection().commit();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(this),
                        "SQLException saving record: item table\n" + e.getMessage(),StdFun.SYSTEM_TITLE ,JOptionPane.ERROR_MESSAGE);                
                } 
                finally {
                    try {
                        db.getConnection().setAutoCommit(true);
                        db.unLockTables();
                    }
                    catch (SQLException ex) {
                        JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(this),
                            "System error setting auto commit to tue\n"+ ex.getMessage(),
                            StdFun.SYSTEM_TITLE,JOptionPane.ERROR_MESSAGE) ;
                    }            
                }
            }
        } else {
            JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(this),
                "Please make sure that all details have been entered before saving:\n"
                        + "Transaction Type has been selected\n"
                        + "A valid Date is entered\n"
                        + "Item transactions posting is complete\n"
                        + "Transaction total is calculated",StdFun.SYSTEM_TITLE ,JOptionPane.ERROR_MESSAGE);                            
        }
    }
    
    @Override
    public boolean entriesComplete() {
        return itemSalesPanel.entriesComplete();
    }
    
    
    @Override
    public void valueChanged(ListSelectionEvent e) {

        if (e.getValueIsAdjusting()) { // if multiple values selected
            return;
        }
        
        ListSelectionModel lsm = (ListSelectionModel)e.getSource();
        if (!lsm.isSelectionEmpty()) {            
            //processPopulateRec();            
        }
    }

    @Override
    public String getTodayDate() {
        return StdFun.getStandardDate( StdFun.getToday());
    }

    @Override
    public int getNextTransNumber(TransType transType) {
        int transNum = 1;        
        
        try  {
            transNum = transRefNumAO.getNextTransNum(transType);            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(this),
                "Error get next Sale TransNumber:\n" + e.getMessage(),
                StdFun.SYSTEM_TITLE,JOptionPane.ERROR_MESSAGE) ;            
        }

        if (transNum == 0) {
            transNum = 1;
        }
        return transNum;
        
    }

    @Override
    public void closeTransaction() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void showMessageinDalog(String message, int errorType) {
        JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(null),
            "Entries incomplete - Canot Save",StdFun.SYSTEM_TITLE,JOptionPane.ERROR_MESSAGE) ;
   
    }

    @Override
    public void setEditable(boolean editable) {
        setButtonEnabled(ActionToolBar.SAVE_BUTTON, editable);
        setButtonEnabled(ActionToolBar.CANCEL_BUTTON, editable);
    }
}

