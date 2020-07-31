/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md.invapp;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.JTextComponent;
import javax.swing.text.NumberFormatter;
import stdClasses.StdFun;

/**
 *
 * @author Martin.Degiorgio
 */
public class ItemSalesTransPanel extends javax.swing.JPanel {

    /**
     * Creates new form ItemTransPanel
     * @param itemRec
     * @param salesTransInterface
     */
    public ItemSalesTransPanel(ItemRecord itemRec, ItemSalesTransInterface salesTransInterface) {
        this.itemRec = itemRec;
        this.salesTransInterface = salesTransInterface;
        itemTransList = new ArrayList<>();        
        
        initComponents();        
        initJTable();
        
        defaultTableModel = (DefaultTableModel)jTable1.getModel();
    }

    private void initJTable() {
        
        try {
            jTable1.setDefaultRenderer(Class.forName("java.lang.Object"), new CustomTableCellRenderer());
        } catch (ClassNotFoundException e) {}


        jTextField1.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0),"enterKey");
        jTextField1.getActionMap().put("enterKey", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jTable1.requestFocus();
                jTable1.changeSelection(currentEditingRow, 
                    currentEditingColumn < TABLE_LINE_QTY_COLUMN ? TABLE_LINE_QTY_COLUMN : currentEditingColumn, false, false);
            }
        });       

        jTable1.getInputMap(JComponent.WHEN_FOCUSED).
            put(KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0), "saveTransaction");
                
        jTable1.getActionMap().put("saveTransaction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopCellEditing();
                setEditable(false, true);               
                headerInterface.setEditable(true);
                footerInterface.setEditable(true);
                salesFrameInterface.setEditable(true);
                footerInterface.setFocusOnDiscountField();
            }
        });

        jTable1.getInputMap(JComponent.WHEN_FOCUSED).
            put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0),"enterComments");
        jTable1.getActionMap().put("enterComments", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jTextField1.requestFocus();
            }
        });
        

        jTable1.getInputMap(JComponent.WHEN_FOCUSED).
            put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),"selectNextColumnCell");

        jTable1.getActionMap().put("selectNextColumnCell", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reCalcCells();
                
                final int EDITING_COLUMN = jTable1.getSelectedColumn();
                final int EDITING_ROW = jTable1.getSelectedRow();
                
                if (EDITING_COLUMN == TABLE_LINE_QTY_COLUMN | 
                        EDITING_COLUMN == TABLE_LINE_PRICE_COLUMN | 
                        EDITING_COLUMN == TABLE_LINE_DISCOUNT_COLUMN) {

                    float linePrice = getCellValue(EDITING_ROW, TABLE_LINE_PRICE_COLUMN).floatValue();
                    int lineQty = getCellValue(EDITING_ROW, TABLE_LINE_QTY_COLUMN).intValue();

                    if (EDITING_COLUMN == TABLE_LINE_QTY_COLUMN) {
                        if (lineQty > 0) {
                            jTable1.changeSelection(EDITING_ROW, EDITING_COLUMN + 1, false, false);
                        }
                    }
                    if (EDITING_COLUMN == TABLE_LINE_PRICE_COLUMN) {
                        if (linePrice > 0f & lineQty > 0) {
                            jTable1.changeSelection(EDITING_ROW, EDITING_COLUMN + 1, false, false);
                        }
                    }
                    if (EDITING_COLUMN == TABLE_LINE_DISCOUNT_COLUMN) {
                        if ((jTable1.getValueAt(EDITING_ROW, TABLE_ITEM_CODE_COLUMN) == null) ||
                            (jTable1.getValueAt(EDITING_ROW, TABLE_ITEM_CODE_COLUMN)).equals("")) {
                            jTable1.changeSelection(EDITING_ROW, TABLE_ITEM_CODE_COLUMN, false, false);
                        } else if (jTable1.getValueAt(EDITING_ROW, TABLE_LINE_QTY_COLUMN) == null) { 
                            jTable1.changeSelection(EDITING_ROW, TABLE_LINE_QTY_COLUMN, false, false);
                        } else if (jTable1.getValueAt(EDITING_ROW, TABLE_LINE_PRICE_COLUMN) == null) { 
                            jTable1.changeSelection(EDITING_ROW, TABLE_LINE_PRICE_COLUMN, false, false);
                        } else {                            
                            jTable1.changeSelection(EDITING_ROW + 1, TABLE_ITEM_CODE_COLUMN, false, false);
                        }                        
                    }
                }
                else if (EDITING_COLUMN < (TABLE_LINE_QTY_COLUMN)) {                    
                    if ((jTable1.getValueAt(EDITING_ROW, EDITING_COLUMN) != null) && 
                        (!((String)jTable1.getValueAt(EDITING_ROW, EDITING_COLUMN)).equals(""))) {
                        String itemCode = ((String)jTable1.getValueAt(EDITING_ROW, EDITING_COLUMN));
                        if (itemCode.equals(ItemRecord.NON_STOCK_ITEM)) {
                            jTable1.changeSelection(EDITING_ROW, TABLE_ITEM_DSC_COLUMN, false, false);
                        } else {
                            jTable1.changeSelection(EDITING_ROW, TABLE_LINE_QTY_COLUMN, false, false);
                        }                        
                        jTable1.setValueAt("F", EDITING_ROW, TABLE_VAT_COLUMN);
                    }
                } 
                jTable1.requestFocus();
            }
        });
        
        jTable1.setRowSelectionAllowed(false);
        jTable1.setColumnSelectionAllowed(false);
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);     
        
        jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                currentEditingRow = jTable1.getSelectedRow();
                currentEditingColumn = jTable1.getSelectedColumn();                
                processAnyLineComments();
                
                if (currentEditingColumn == TABLE_ITEM_CODE_COLUMN) {                    
                    setTableLineNumber(currentEditingRow);
                }
            }
        });
        
        //------------------------------------------------------------------------------------
        //Define Cell Editors
        //------------------------------------------------------------------------------------
        final JTextField integerField = new JTextField();
        DefaultCellEditor integerEditor = new DefaultCellEditor(integerField) {
            // Ensure value inputted by user is validated
            @Override
            public Object getCellEditorValue() {
                return getValidatedNumber(jTable1.getEditingRow(), jTable1.getEditingColumn(),
                    integerField.getText());
            }
            
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                Component c = super.getTableCellEditorComponent(table, value, isSelected, row, column);
                if (c instanceof JTextComponent) {
                    JTextComponent jtc = (JTextComponent) c;
                    jtc.requestFocus();
                    jtc.selectAll();
                }
                return c;                
            }

        };        
        
        //practically disable integereditor editing and use default table editor 
        //because when integereditor is being edited focusListener in jtable does not work
        //besides all checks in integer editor work just the same in table editor
        integerEditor.setClickCountToStart(8);
        integerEditor.addCellEditorListener(new CellEditorListener() {
                @Override
                public void editingStopped(ChangeEvent e) {            
                    reCalcCells();
                }

                @Override
                public void editingCanceled(ChangeEvent e) {
                    // do nothing
                }
            }
        );

        
        final JFormattedTextField floatField = new JFormattedTextField();
        DefaultCellEditor floatEditor = new DefaultCellEditor(floatField) {
            // Ensure value inputted by user is validated
            @Override
            public Object getCellEditorValue() {
                Number validatedNum = getValidatedNumber(jTable1.getEditingRow(), jTable1.getEditingColumn(),
                    floatField.getText());
                
                Object returnStr;
                if (validatedNum != null) {
                    returnStr = DEC_FORMAT.format(validatedNum);
                } else {
                    if (jTable1.getSelectedColumn() == TABLE_LINE_DISCOUNT_COLUMN) {
                        returnStr = DEC_FORMAT.format(0.00);
                    } else {
                        returnStr = null;
                    }
                    
                }
                return returnStr;
            }
            
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                Component c = super.getTableCellEditorComponent(table, value, isSelected, row, column);
                if (c instanceof JTextComponent) {
                    JTextComponent jtc = (JTextComponent) c;
                    jtc.requestFocus();
                    jtc.selectAll();
                }
                return c;                
            }            
        };
        floatField.setFormatterFactory(new DefaultFormatterFactory(
            new NumberFormatter(new DecimalFormat(StdFun.DECIMAL_FORMAT))));
        //reason of this 8 is same as above
        floatEditor.setClickCountToStart(8);
        floatEditor.addCellEditorListener(new CellEditorListener() {
            @Override
            public void editingStopped(ChangeEvent e) {            
                reCalcCells();
            }

            @Override
            public void editingCanceled(ChangeEvent e) {
                // do nothing
            }
        });

        final JTextField stringField = new JTextField();        
        stringField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (jTable1.getSelectedColumn() == TABLE_ITEM_CODE_COLUMN) {
                    if (e.getClickCount() == 2) {
                        if (jTable1.getValueAt(jTable1.getSelectedRow(), TABLE_LINE_NUM_COLUMN) != null) {
                            if (itemsScanListener != null) {
                                itemRec.initVars();
                                itemRec.setItemCode(stringField.getText());
                                itemsScanListener.initScanList();
                                stringField.setText(itemRec.getItemCode());                            
                            }                            
                        }
                    }
                }
            }            
        });

        DefaultCellEditor stringEditor = new DefaultCellEditor(stringField) {
            // Ensure value inputted by user is validated
            @Override
            public Object getCellEditorValue() {
                String strObject = stringField.getText();
                if (jTable1.getSelectedColumn() == TABLE_ITEM_CODE_COLUMN) {                    
                    if (!strObject.equals("")) {
                        strObject = validateItemCode(jTable1.getSelectedRow(), stringField.getText());
                    }
                }         
                if (jTable1.getSelectedRow() > 0) {
                    if(jTable1.getValueAt(jTable1.getSelectedRow() -1, TABLE_LINE_TOTAL_COLUMN) == null) {
                        strObject = "";
                    }
                }
                
                //validateItemCode(jTable1.getSelectedRow(), strObject);

                return strObject;
            }

            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                Component c = super.getTableCellEditorComponent(table, value, isSelected, row, column);
                if (c instanceof JTextComponent) {
                    JTextComponent jtc = (JTextComponent) c;
                    jtc.requestFocus();
                    jtc.selectAll();
                }
                return c;                
            }

        };
        //reason of this 8 is same as above
        stringEditor.setClickCountToStart(2);
        stringEditor.addCellEditorListener(new CellEditorListener() {
            @Override
            public void editingStopped(ChangeEvent e) {
                stopCellEditing();                
            }

            @Override
            public void editingCanceled(ChangeEvent e) {
            }
        });        
        //------------------------------------------------------------------------------------
        
        //---------------------------------------------------------------               
        jTable1.getColumnModel().getColumn(TABLE_LINE_NUM_COLUMN).setMinWidth(30);
        jTable1.getColumnModel().getColumn(TABLE_LINE_NUM_COLUMN).setMaxWidth(30);
        jTable1.getColumnModel().getColumn(TABLE_LINE_NUM_COLUMN).setPreferredWidth(30);
        
        jTable1.getColumnModel().getColumn(TABLE_ITEM_CODE_COLUMN).setMinWidth(100);
        jTable1.getColumnModel().getColumn(TABLE_ITEM_CODE_COLUMN).setMaxWidth(100);
        jTable1.getColumnModel().getColumn(TABLE_ITEM_CODE_COLUMN).setPreferredWidth(100);
        
        jTable1.getColumnModel().getColumn(TABLE_ITEM_DSC_COLUMN).setMinWidth(350);
        jTable1.getColumnModel().getColumn(TABLE_ITEM_DSC_COLUMN).setMaxWidth(350);
        jTable1.getColumnModel().getColumn(TABLE_ITEM_DSC_COLUMN).setPreferredWidth(350);
        
        jTable1.getColumnModel().getColumn(TABLE_LINE_QTY_COLUMN).setMinWidth(50);
        jTable1.getColumnModel().getColumn(TABLE_LINE_QTY_COLUMN).setMaxWidth(50);
        jTable1.getColumnModel().getColumn(TABLE_LINE_QTY_COLUMN).setPreferredWidth(50);
        
        jTable1.getColumnModel().getColumn(TABLE_ITEM_CODE_COLUMN).setCellEditor(stringEditor);
        jTable1.getColumnModel().getColumn(TABLE_ITEM_DSC_COLUMN).setCellEditor(stringEditor);
        jTable1.getColumnModel().getColumn(TABLE_LINE_QTY_COLUMN).setCellEditor(integerEditor); // Qty
        jTable1.getColumnModel().getColumn(TABLE_LINE_PRICE_COLUMN).setCellEditor(floatEditor); // Price
        jTable1.getColumnModel().getColumn(TABLE_LINE_DISCOUNT_COLUMN).setCellEditor(floatEditor); // Amount
        
        jTable1.setValueAt(1, 0, 0);
                
        currentEditingRow = -1;
        currentEditingColumn = -1;    
    }

    public boolean entriesComplete() {
        boolean entriesComplete = true;        
        if (jTable1.getRowCount() > 0) {
            for (int x = 0 ; x < jTable1.getRowCount(); x++) {
                if (jTable1.getValueAt(x, TABLE_LINE_NUM_COLUMN) != null) {
                    if (jTable1.getValueAt(x, TABLE_ITEM_CODE_COLUMN) != null) {
                        if (jTable1.getValueAt(x, TABLE_LINE_TOTAL_COLUMN) == null) {
                            entriesComplete = false;
                            break;
                        }
                    }
                }                
            }            
        } else {
            entriesComplete = false;
        }
        return entriesComplete;
    }
    
    private void processAnyLineComments() {
        int currentRow = jTable1.getSelectedRow();
        
        if (itemTransList.size() > currentRow) {
            jTextField1.setText(itemTransList.get(currentRow).getComments());
        } else {
            jTextField1.setText("");
        }
    }
    
    private boolean isBlankRow(int row) {
        boolean isBlank = false;
        if ((jTable1.getValueAt(row, TABLE_ITEM_CODE_COLUMN) == null) || ((String)(jTable1.getValueAt(row, TABLE_ITEM_CODE_COLUMN))).equals("")) {
            isBlank = true;
        }
        return isBlank;
    }
    
    private void updateTransList(int currentRow) {
        
        String itemCode = (String)jTable1.getValueAt(currentRow, TABLE_ITEM_CODE_COLUMN);
        if ((itemCode != null) && (!itemCode.equals(""))) {                    
            ItemTransRecord transRec = new ItemTransRecord();
            transRec.setItemCode((String)jTable1.getValueAt(currentRow, TABLE_ITEM_CODE_COLUMN));
            transRec.setItemDsc((String)jTable1.getValueAt(currentRow, TABLE_ITEM_DSC_COLUMN));

            transRec.setSellingPrice(getCellValue(currentRow, TABLE_LINE_PRICE_COLUMN).floatValue());
            transRec.setTransQty(getCellValue(currentRow, TABLE_LINE_QTY_COLUMN).intValue());
            transRec.setSellDiscount(getCellValue(currentRow, TABLE_LINE_DISCOUNT_COLUMN).floatValue());
            transRec.setComments(jTextField1.getText());

            if (itemTransList.size() > currentRow) {
                itemTransList.set(currentRow, transRec);
            } else if (itemTransList.size() == currentRow) {
                itemTransList.add(transRec);                                                
            }
        }
    }
    
    private String validateItemCode (int row, String value) {        
        itemRec.setItemCode(value);
        itemsScanListener.itemCodeFocusLost();

        boolean dupItmCode = false;
        if (itemRec.getItemCode() == null) {    
            value = "";
            clearRow(row);
        } else {            
            if (!value.equals(ItemRecord.NON_STOCK_ITEM)) {
                for (int x = 0; x < jTable1.getRowCount() -1; x++) {
                    if (jTable1.getValueAt(x, TABLE_ITEM_CODE_COLUMN) != null) {
                        String tableItmCode = (String)jTable1.getValueAt(x, TABLE_ITEM_CODE_COLUMN);
                        if (!tableItmCode.equals(ItemRecord.NON_STOCK_ITEM)) {
                            if ((tableItmCode.equals(value)) && (x != row)){
                                dupItmCode = true;
                                value = "";
                                clearRow(row);
                                break;
                            }
                        }
                    }
                }                
            }
        }
        
        if (!dupItmCode) {
            if ((jTable1.getValueAt(row, TABLE_LINE_NUM_COLUMN) == null) || (jTable1.getValueAt(row, TABLE_LINE_NUM_COLUMN).equals(""))) {
                value = "";
                itemRec.initVars();
                clearRow(row);
            } else {
                if (value.isEmpty()) {
                    itemRec.initVars();
                    clearRow(row);
                } else {
                    jTable1.setValueAt(itemRec.getItemCode(),row, TABLE_ITEM_CODE_COLUMN);
                    jTable1.setValueAt(itemRec.getDsc(),row, TABLE_ITEM_DSC_COLUMN);
                    jTable1.setValueAt(DEC_FORMAT.format(itemRec.getSellPrice()),row, TABLE_LINE_PRICE_COLUMN); 
                    jTable1.setValueAt("F", row, TABLE_VAT_COLUMN);                    
                }
            }
        }
        
        return value;        
    }

    private void clearRow(int row) {
        jTable1.setValueAt("",row, TABLE_ITEM_CODE_COLUMN);
        jTable1.setValueAt("",row, TABLE_ITEM_DSC_COLUMN);
        jTable1.setValueAt("",row, TABLE_VAT_COLUMN);
        
        jTable1.setValueAt(DEC_FORMAT.format(0f),row,TABLE_LINE_DISCOUNT_COLUMN);
        jTable1.setValueAt(INT_FORMAT.format(0),row,TABLE_LINE_QTY_COLUMN);
        jTable1.setValueAt(DEC_FORMAT.format(0f),row,TABLE_LINE_PRICE_COLUMN);
        jTable1.setValueAt(DEC_FORMAT.format(0f),row,TABLE_LINE_TOTAL_COLUMN);
        
        jTextField1.setText("");                
    }
    
    private void reCalcCells(){        
        stopCellEditing();
               
        final int EDITING_COLUMN = jTable1.getSelectedColumn();
        final int EDITING_ROW = jTable1.getSelectedRow();

        final DefaultTableModel df = (DefaultTableModel)jTable1.getModel();
        

        if (EDITING_COLUMN == TABLE_LINE_QTY_COLUMN | 
                EDITING_COLUMN == TABLE_LINE_PRICE_COLUMN | 
                EDITING_COLUMN == TABLE_LINE_DISCOUNT_COLUMN) {

            float linePrice = getCellValue(EDITING_ROW, TABLE_LINE_PRICE_COLUMN).floatValue();
            float lineDisc = getCellValue(EDITING_ROW, TABLE_LINE_DISCOUNT_COLUMN).floatValue();
            int lineQty = getCellValue(EDITING_ROW, TABLE_LINE_QTY_COLUMN).intValue();

            if (EDITING_COLUMN == TABLE_LINE_DISCOUNT_COLUMN) {
                updateTransList(EDITING_ROW);
                if (EDITING_ROW == (jTable1.getRowCount() - 1)) {
                    df.addRow(new Integer[df.getColumnCount()]);
                } 
                if ((jTable1.getValueAt(EDITING_ROW, TABLE_ITEM_CODE_COLUMN) != null) && 
                        !(jTable1.getValueAt(EDITING_ROW, TABLE_ITEM_CODE_COLUMN)).equals("")) { 
                }
            }
            if (linePrice > 0f && lineQty > 0) {
                updateTransList(EDITING_ROW);
                if (jTable1.getValueAt(EDITING_ROW, EDITING_COLUMN) == null) {
                    jTable1.setValueAt(DEC_FORMAT.format(0f),EDITING_ROW,EDITING_COLUMN);
                }
                if (jTable1.getValueAt(EDITING_ROW, TABLE_LINE_DISCOUNT_COLUMN) == null) {
                    jTable1.setValueAt(DEC_FORMAT.format(0f),EDITING_ROW,TABLE_LINE_DISCOUNT_COLUMN);
                }
                jTable1.setValueAt(DEC_FORMAT.format((linePrice * lineQty) * (1 - (lineDisc / 100.0f))), EDITING_ROW, TABLE_LINE_TOTAL_COLUMN);
            }
        }
        else if (EDITING_COLUMN < (TABLE_LINE_QTY_COLUMN)) {
            if ((jTable1.getValueAt(EDITING_ROW, EDITING_COLUMN) != null) && !((String)jTable1.getValueAt(EDITING_ROW, EDITING_COLUMN)).equals("")){
                updateTransList(EDITING_ROW);
                jTable1.setValueAt("F", EDITING_ROW, TABLE_VAT_COLUMN);
            }
        } 

        if (footerInterface != null) {
            float transTotal = 0f;
            for (int x = 0; x < jTable1.getRowCount(); x++) {
                transTotal += StdFun.getFloatValue((String)jTable1.getValueAt(x, TABLE_LINE_TOTAL_COLUMN));
            }            
            footerInterface.populateTransTotal(transTotal);
        }
        
        jTable1.requestFocus();
    }

    private void setTableLineNumber(int editingRow) {
        if (editingRow > 0) {
            if ((jTable1.getValueAt(editingRow - 1, TABLE_LINE_TOTAL_COLUMN) != null) && 
                    (jTable1.getValueAt(editingRow -1, TABLE_ITEM_CODE_COLUMN) != null)) {
                if (jTable1.getValueAt(editingRow, TABLE_LINE_NUM_COLUMN) == null) {
                    jTable1.setValueAt(((Integer)jTable1.getValueAt(editingRow - 1, TABLE_LINE_NUM_COLUMN)) + 1, 
                        editingRow,
                        TABLE_LINE_NUM_COLUMN);
                }                
                if (jTable1.getValueAt(editingRow - 1, TABLE_LINE_DISCOUNT_COLUMN) == null) {
                    jTable1.setValueAt(DEC_FORMAT.format(0.00f), editingRow - 1, TABLE_LINE_DISCOUNT_COLUMN);
                }
            }
        }
    }
    
    public void addSalesFooterInterface(ItemSalesFooterInterface footerInterface) {
        this.footerInterface = footerInterface;
    }

    public void addSalesHeaderInterface(ItemSalesHeaderTransInterface headerInterface) {
        this.headerInterface = headerInterface;
    }
    
    public void addSalesMainFrameInterface(ItemSalesMainFrameInterface salesFrameInterface) {
        this.salesFrameInterface = salesFrameInterface;
    }
    
    public void addItemCodeSanListener(ItemScanListener itemsScanListener) {
        this.itemsScanListener = itemsScanListener;
    }

    private class CustomTableCellRenderer extends DefaultTableCellRenderer {
        String toolTipText;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            final int EDITING_COL = jTable1.getEditingColumn();
            switch (EDITING_COL) {
                case TABLE_LINE_NUM_COLUMN:
                case TABLE_LINE_QTY_COLUMN:
                case TABLE_VAT_COLUMN:
                    setHorizontalAlignment(JLabel.CENTER);
                    break;
                case TABLE_LINE_PRICE_COLUMN:
                case TABLE_LINE_DISCOUNT_COLUMN:
                case TABLE_LINE_TOTAL_COLUMN:
                    setHorizontalAlignment(JLabel.RIGHT);
                    break;
                case TABLE_ITEM_CODE_COLUMN:
                case TABLE_ITEM_DSC_COLUMN:
                    setHorizontalAlignment(JLabel.LEFT);
                    break;
            }
            toolTipText = null;
            //cell.setBackground(cellColor);
            //setToolTipText(toolTipText);
            
            return cell;
        }
    }
    
    public void stopCellEditing() {
        if (jTable1.getCellEditor() != null) {
            jTable1.getCellEditor().stopCellEditing() ;
        }
    }

    //fix this according to column as not all columns are integers or floats
    public Number getCellValue(int row, int col) {        
        Number cellValue = 0;

        Object cellContent = jTable1.getValueAt(row, col);
        if (cellContent != null) {            
            if (col == (TABLE_LINE_PRICE_COLUMN) | col == TABLE_LINE_DISCOUNT_COLUMN | col == TABLE_LINE_TOTAL_COLUMN) {            
                cellValue = StdFun.getFloatValue((String)jTable1.getValueAt(row, col));
            }
            else { 
                if (jTable1.getValueAt(row, col) != null) {
                    if (col == TABLE_LINE_QTY_COLUMN) {
                        cellValue = StdFun.getIntValue(jTable1.getValueAt(row, col));
                    }
                }
            } 
        }

        return cellValue;    
    }

    public Number getValidatedNumber (int row, int col, String value) {

        Number numberEntered = 0;        
        boolean intValue = false;
        boolean floatValue = false;
        boolean validNumber = false;
        
        switch (col) {
            case TABLE_LINE_NUM_COLUMN:
            case TABLE_LINE_QTY_COLUMN:
                intValue = true;
                validNumber = true;
                break;
            case TABLE_LINE_PRICE_COLUMN:
            case TABLE_LINE_DISCOUNT_COLUMN:
            case TABLE_LINE_TOTAL_COLUMN:
                floatValue = true;
                validNumber = true;
                break;
        }
                
        if ((value == null) || (value.equals(""))) {
            validNumber = false;
        } else {
            try {
                if (intValue) {
                    numberEntered = StdFun.getIntValue(value);
                } else { 
                    numberEntered = StdFun.getFloatValue(value);                    
                }
            } catch (NumberFormatException e) {
                validNumber = false;
            }
        }
                
        if (validNumber) {
            if (col > 0) {
                if (jTable1.getValueAt(row, col -1) == null) {
                    validNumber = false; 
                }
            } else { // col = 0 
                if (row > 0) {
                    if (jTable1.getValueAt(row -1, TABLE_LINE_TOTAL_COLUMN) == null) {
                        validNumber = false;
                    }             
                }
            }
        }
            
        if (!validNumber) {
            numberEntered = null;
        } else { // validNumber
            if (!intValue) { // round float value to 2 dec places
                numberEntered = StdFun.roundVal(numberEntered.floatValue(), 2);
                if (numberEntered.floatValue() < 0f) {
                    numberEntered = null;
                }
            }
        }
                
        return numberEntered;        
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "#", "Item Code", "Item Dsc", "Qty", "Price", "% Disc", "VAT", "Tot Exc VAT"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true, true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTable1);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("Comments:");

        jTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField1FocusLost(evt);
            }
        });

        jButton1.setText(DISABLED_TRANS_TEXT);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusLost
        updateTransList(jTable1.getSelectedRow());
    }//GEN-LAST:event_jTextField1FocusLost

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (jTable1.isEnabled()) {
            stopCellEditing();
            setEditable(false, true);   
            removeIfAnyInvalidRows();            
            headerInterface.setEditable(true);
            footerInterface.setEditable(true);
            salesFrameInterface.setEditable(true);
        } else {
            setEditable(true, true);            
            headerInterface.setEditable(false);
            footerInterface.setEditable(false);
            salesFrameInterface.setEditable(false);
        }
    }//GEN-LAST:event_jButton1ActionPerformed
    
    private void removeIfAnyInvalidRows() {
        
        if (jTable1.getRowCount() > 0) {
            for (int x = 0 ; x < jTable1.getRowCount(); x++) {
                if (jTable1.getValueAt(x, TABLE_LINE_NUM_COLUMN) != null) {
                    if ((jTable1.getValueAt(x, TABLE_ITEM_CODE_COLUMN) == null) || ((String)jTable1.getValueAt(x, TABLE_ITEM_CODE_COLUMN)).equals("")) {
                        if (jTable1.getValueAt(x, TABLE_LINE_TOTAL_COLUMN) == null) {
                            jTable1.setValueAt(null, x, TABLE_LINE_NUM_COLUMN);
                            jTable1.setValueAt(null, x, TABLE_ITEM_CODE_COLUMN);
                        }
                    }
                }                
            }            
        } 
        
    }
    
    public void clearForm() {
        jTextField1.setText("");        
        
        for (int row = 0; row < jTable1.getRowCount(); row++) {
            for (int col = 0; col < jTable1.getColumnCount(); col++) {
                jTable1.setValueAt(null, row, col);
            }
        }            
        jTable1.setValueAt(1, 0, 0);                
        itemTransList.clear();
    }

    public void setEditable(boolean editable, boolean enableButton) {
        jTextField1.setEditable(editable);
        jTable1.setEnabled(editable);        
                
        jButton1.setEnabled(enableButton);

        if (editable) {
            jButton1.setText(ENABLED_TRANS_TEXT);
        } else {
            jButton1.setText(DISABLED_TRANS_TEXT);
        }        
    }
    
    public void setButtonEnabled(boolean enabled) {
        jButton1.setEnabled(enabled);
    }
    
    public ArrayList<ItemTransRecord> getItemTransactions() {        
        return itemTransList;
    }

    
    private final DefaultTableModel defaultTableModel;
    
    private final ItemSalesTransInterface salesTransInterface;
    
    private int currentEditingRow;
    private int currentEditingColumn;
    
    private ItemScanListener itemsScanListener;
    private ItemSalesFooterInterface footerInterface;
    private ItemSalesHeaderTransInterface headerInterface;
    private ItemSalesMainFrameInterface salesFrameInterface;

    private final ItemRecord itemRec;
    private final ArrayList<ItemTransRecord> itemTransList;
    
    private static final int TABLE_LINE_NUM_COLUMN = 0;
    private static final int TABLE_ITEM_CODE_COLUMN = 1;
    private static final int TABLE_ITEM_DSC_COLUMN = 2;    
    private static final int TABLE_LINE_QTY_COLUMN = 3;
    private static final int TABLE_LINE_PRICE_COLUMN = 4;
    private static final int TABLE_LINE_DISCOUNT_COLUMN = 5;
    private static final int TABLE_VAT_COLUMN = 6;
    private static final int TABLE_LINE_TOTAL_COLUMN = 7;

    private final DecimalFormat DEC_FORMAT = new DecimalFormat(StdFun.DECIMAL_FORMAT);                
    private final DecimalFormat INT_FORMAT = new DecimalFormat(StdFun.INT_FORMAT);                

    private final String ENABLED_TRANS_TEXT = "Close Transs";
    private final String DISABLED_TRANS_TEXT = "Post Trans";
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
