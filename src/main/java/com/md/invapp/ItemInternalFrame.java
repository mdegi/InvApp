/*
 * Created on August 11, 2006, 2:22 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.md.invapp;

import com.md.invapp.data.HibernateUtil;
import com.md.invapp.data.dao.ItemCategoryDao;
import com.md.invapp.data.dao.ItemDao;
import com.md.invapp.data.dao.ItemGroupDao;
import com.md.invapp.data.entities.ItemCategoryEntity;
import com.md.invapp.data.entities.ItemEntity;
import com.md.invapp.data.entities.ItemGroupEntity;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

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
    private ItemDao itemDao = null;
    private ItemEntity itemRecord;
    
    private ItemCategoryEntity itemCategoryRecord;
    private ItemCategoryDao itemCategoryDao;
    
    private ItemGroupEntity itemGroupRecord;
    private ItemGroupDao itemGroupDao;
    
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
        
        itemRecord = new ItemEntity();
        itemCategoryRecord = new ItemCategoryEntity();
        itemGroupRecord = new ItemGroupEntity();
        
        itemCategoryDao = new ItemCategoryDao(HibernateUtil.getSessionFactory());           
        itemGroupDao = new ItemGroupDao(HibernateUtil.getSessionFactory());           
        itemDao = new ItemDao(HibernateUtil.getSessionFactory());
    }
    
    private void initComponents()  {        
        ArrayList<ArrayList> listDet = null;        
        
        ArrayList<ItemCategoryEntity> categoriesList = null;
        ArrayList<ItemGroupEntity> groupsList = null;

        try {
            categoriesList = itemCategoryDao.getAllRecords()
                    .stream()
                    .map(cat -> (ItemCategoryEntity)cat)
                    .collect(Collectors.toCollection(ArrayList::new));

            groupsList = itemGroupDao.getAllRecords()
                    .stream()
                    .map(grp -> (ItemGroupEntity)grp)
                    .collect(Collectors.toCollection(ArrayList::new));
            
            listDet = itemDao.getItemsDisplayList();                        
        } catch (NoSuchFieldException e) {
            JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(this),
                "NoSuchFieldException: init lists\n" + e.getMessage(),StdFun.SYSTEM_TITLE ,JOptionPane.ERROR_MESSAGE) ;
        } catch (SecurityException e) {
            JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(this),
                "SecurityException: init lists\n" + e.getMessage(),StdFun.SYSTEM_TITLE ,JOptionPane.ERROR_MESSAGE) ;
            
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
                        
            itemDao.deleteRecord(itemRecord.getItemCode());
            itemPanel.deleteSelectedEntry();
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

        //itemRecord.initVars();

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
            if (newRec) {
                if ((itemRecord.getItemCode() != null ) && (!itemRecord.getItemCode().equals(""))) {
                    itemDao.saveRecord(itemRecord);
                    
                    itemRecord = itemDao.getItem(itemRecord.getItemCode());
                    itemPanel.addEntry(new Object[]
                        {itemRecord.getItemCode(),itemRecord.getDescription()});                        
                    cancelRecord();
                }
            }
            else { 
                itemDao.updateRecord(itemRecord);
                itemRecord = itemDao.getItem(itemRecord.getItemCode());
                itemPanel.updateEntry(new Object[]
                        {itemRecord.getItemCode(),itemRecord.getDescription()});
                cancelRecord(); 
            }
            newRec = true;
        }    
    }
    
    private boolean entriesComplete() {
        boolean entriesComplete = true;
                
        boolean validEnt = true;
        if (validEnt) {
            itemCategoryRecord = itemCategoryDao.getCategory((String)itemPanel.getSelectedItem(ItemPanel.CATEGORY_COMBO));
            itemGroupRecord = itemGroupDao.getGroup((String)itemPanel.getSelectedItem(ItemPanel.GROUP_COMBO));

            itemRecord.setCategoryId(itemCategoryRecord.getId());
            itemRecord.setGroupId(itemGroupRecord.getId());
        }

        //check if required fields have been entered correctlyhere
        
        return entriesComplete;
    }
    
    public void initListItems() {           
        try {
            itemPanel.initScanPanel(itemDao.getItemsDisplayList());
        } catch (NoSuchFieldException e) {
            JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(this),
                "NoSuchFieldException retreiving records: item table\n" + e.getMessage(),StdFun.SYSTEM_TITLE ,JOptionPane.ERROR_MESSAGE) ;            
        }        
    }

    public void processListSelection(int selectedIndex, String itemCode) {             
        itemRecord = itemDao.getItem(itemCode);

        itemCategoryRecord = itemCategoryDao.getCategory(itemRecord.getCategoryId());
        itemGroupRecord = itemGroupDao.getGroup(itemRecord.getGroupId());
        combosValues.clear();

        combosValues.put(ItemPanel.CATEGORY_COMBO, itemCategoryRecord.getDescription());
        combosValues.put(ItemPanel.GROUP_COMBO, itemGroupRecord.getDescription());

        itemPanel.fillPanel(combosValues);

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
        itemRecord = itemDao.getItem(itemPanel.getSelectedItem(ItemPanel.ITEM_TABLE));
        itemCategoryRecord = itemCategoryDao.getCategory(itemRecord.getCategoryId());
        itemGroupRecord = itemGroupDao.getGroup(itemRecord.getGroupId());

        combosValues.clear();

        combosValues.put(ItemPanel.CATEGORY_COMBO, itemCategoryRecord.getDescription());
        combosValues.put(ItemPanel.GROUP_COMBO, itemGroupRecord.getDescription());

        itemPanel.fillPanel(combosValues);

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

