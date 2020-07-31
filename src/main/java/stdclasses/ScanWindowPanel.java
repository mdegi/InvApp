/*
 * ScanWindow.java
 *
 * Created on October 4, 2006, 2:39 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package stdClasses;


import java.text.DecimalFormat;

import java.util.Vector;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import javax.swing.event.ListSelectionListener;

import java.awt.Color;
import java.awt.Event;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.Rectangle;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;

import java.util.ArrayList;
import java.util.EventListener;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.AbstractAction;

import javax.swing.KeyStroke;
import javax.swing.event.AncestorListener;


/**
 *
 * @author nla
 */
public class ScanWindowPanel extends JPanel {
   
    private Vector<Vector> rowData;

    private Vector headerData, colWidth;
    private Vector<String> markedRows;

    private String toolTipText, markedCellsToolTip;

    private JTable scanTable;
    private JScrollPane jScrollPane;
    private DefaultTableModel defaultModel;
    private ListSelectionModel rsm;
    private TableSorter sorter;
    private DecimalFormat df;

    private Color backGroundColor, markedColor;
    private final Color selectedColor = new Color(184, 207, 229);
   
    public static final Color WHITE_COLOUR = new Color(255,255,255);

    private int searchArrayPos = -1;
    private ArrayList<Integer> searchedRowsPos;

    private String searchFieldText = "";

    private JPanel searchPanel;

    private JTextField searchField;

    private JButton findButton;
    private JButton nextButton;
    private JButton prevButton;

    private final String FIND_BTN_CMD = "FIND_BUTTON_CMD";
    private final String NEXT_BTN_CMD = "NEXT_BUTTON_CMD";
    private final String PREV_BTN_CMD = "PREV_BUTTON_CMD";


    public ScanWindowPanel() {

        this.rowData = null;
        this.headerData = null;
        this.colWidth = null;

        this.backGroundColor = Color.WHITE;
        initComp();        
    }

    public ScanWindowPanel(Color backGroundColor) {

        this.rowData = null;
        this.headerData = null;
        this.colWidth = null;
        this.backGroundColor = backGroundColor;

        initComp();
    }

    /** Creates a new instance of ScanWindow 
     * 
     * @param listDet Vector
     */
    public ScanWindowPanel(Vector<Vector> listDet) {

        initEntries(listDet);
        
//        if (rowData.size() > 0) { 
            initComp();
            //fillData(); // this was not remarked

            setVisible(true);
//        } else JOptionPane.showMessageDialog(null,"No Data Match Your Selection Criteria",StdFun.SYSTEM_TITLE,JOptionPane.OK_OPTION) ;
    }

   /**
    * Initialise required components
    *
    */
    private void initComp() {
        
        defaultModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false ;
            }
        };

        sorter = new TableSorter(defaultModel);

        scanTable = new JTable(sorter);

        toolTipText = "Double click to select . . . .";

        sorter.setTableHeader(scanTable.getTableHeader());

        scanTable.setRowSelectionAllowed(true);
        scanTable.setColumnSelectionAllowed(false);
        scanTable.getTableHeader().setReorderingAllowed(false);        
        scanTable.getTableHeader().setToolTipText("Click on header columns to sort");

        scanTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        scanTable.getInputMap().put(KeyStroke.getKeyStroke(
            KeyEvent.VK_F, Event.CTRL_MASK),
            "ctrl-f");

        jScrollPane = new JScrollPane(scanTable);
  
        setBackground(WHITE_COLOUR);
        setLayout(new BorderLayout());

        add(jScrollPane,BorderLayout.CENTER);

        rsm = scanTable.getSelectionModel();

        df = new DecimalFormat(StdFun.DECIMAL_FORMAT);


    }

    public void setBackGroundColour(Color backGroundColor) {
        this.backGroundColor = backGroundColor;
    }
    
    public void initSearchPanel(final Frame parentFrame, String title) {

        if (searchPanel == null) {
            initSearchComponents();

            final JPanel outPanel = this;

            outPanel.add(searchPanel, BorderLayout.SOUTH);

            searchFieldText = "";

            setNextButton(false);
            setPrevButton(false);


            outPanel.setVisible(false);
            outPanel.setVisible(true);


            scanTable.getActionMap().put("ctrl-f",
                new AbstractAction() {
                    @Override

                    public void actionPerformed(ActionEvent e) {
                        searchField.requestFocus();
                    }
                }
            );

            searchField.getActionMap().put("enterKey",
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        findButton.doClick();
                    }
                }
            );

            AbstractAction escKeyAction = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    exitSearch();
                    scanTable.requestFocus();
                }
            };


            searchField.getActionMap().put("escapeKey",escKeyAction);
            findButton.getActionMap().put("escapeKey",escKeyAction);
            nextButton.getActionMap().put("escapeKey",escKeyAction);
            prevButton.getActionMap().put("escapeKey",escKeyAction);


            ActionListener searchButtonsListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getActionCommand().equals(FIND_BTN_CMD)) {

                        searchArrayPos = -1;

                        searchedRowsPos = getSearchStrPos(searchField.getText(), 1, true);
                        if (searchedRowsPos.size() > 0) {
                            setSelectedIndex(searchedRowsPos.get(0));
                            searchArrayPos++;

                            if (!Scrolling.isVerticallyVisible(scanTable, getRowBounds(searchedRowsPos.get(0)))) {
                                makeRowVisible(searchedRowsPos.get(0));
                            }

                            setPrevButton(false);
                            if (searchedRowsPos.size() > 1) {
                                setNextButton(true);
                                setRequestFocusNextBtn();
                            }
                            searchFieldText = ("1 of " + searchedRowsPos.size());
                            searchField.repaint();
                        } else {
                            searchArrayPos = -1;
                            setPrevButton(false);
                            setNextButton(false);
                            searchFieldText = "";
                            searchField.repaint();

                            JOptionPane.showMessageDialog(parentFrame,
                                "No records found",
                                StdFun.SYSTEM_TITLE,JOptionPane.INFORMATION_MESSAGE) ;
                        }
                    }
                    if (e.getActionCommand().equals(NEXT_BTN_CMD)) {
                        setNextButton(true);
                        setPrevButton(true);
                        if (searchArrayPos < searchedRowsPos.size() -1) {
                            searchArrayPos ++;
                            setRequestFocusNextBtn();
                        }

                        if (searchArrayPos == searchedRowsPos.size() - 1) {
                            setNextButton(false);
                            setRequestFocusPrevBtn();
                        }

                        setSelectedIndex(searchedRowsPos.get(searchArrayPos));
                        if (!Scrolling.isVerticallyVisible(scanTable, getRowBounds(searchedRowsPos.get(searchArrayPos)))) {
                            makeRowVisible(searchedRowsPos.get(searchArrayPos));
                        }

                        searchFieldText = (searchArrayPos + 1) + " of " + searchedRowsPos.size();
                        searchField.repaint();

                    }
                    if (e.getActionCommand().equals(PREV_BTN_CMD)) {

                        setNextButton(true);
                        setPrevButton(true);

                        if (searchArrayPos > 0) {
                            searchArrayPos --;
                            setRequestFocusPrevBtn();
                        }
                        if (searchArrayPos == 0) {
                            setPrevButton(false);
                            setRequestFocusNextBtn();
                        }

                        setSelectedIndex(searchedRowsPos.get(searchArrayPos));
                        if (!Scrolling.isVerticallyVisible(scanTable, getRowBounds(searchedRowsPos.get(searchArrayPos)))) {
                            makeRowVisible(searchedRowsPos.get(searchArrayPos));
                        }

                        searchFieldText = (searchArrayPos + 1) + " of " + searchedRowsPos.size();
                        searchField.repaint();
                    }
                }
            };

            findButton.addActionListener(searchButtonsListener);
            nextButton.addActionListener(searchButtonsListener);
            prevButton.addActionListener(searchButtonsListener);

            toolTipText += "Use <Ctrl-F> to search";
            //mmmscanTable.setToolTipText(scanTable.getToolTipText() + "Use <Ctrl-F> to search");
        }  else {
            searchPanel.setVisible(false);
            searchPanel.setVisible(true);

        }

    }

    public Rectangle getRowBounds(int row) {

        Rectangle result = scanTable.getCellRect(row, -1, true);
        Insets i = scanTable.getInsets();

        result.x = i.left;
        result.width = scanTable.getWidth() - i.left - i.right;

        return result;
    }

    public void makeRowVisible(int row) {
        Scrolling.scrollVertically(scanTable, getRowBounds(row));
    }

    private void exitSearch() {

        searchField.setText("");
        searchArrayPos = -1;

        searchFieldText = ("");
        searchField.setText("");
        searchField.repaint();
        setPrevButton(false);
        setNextButton(false);

    }

    private void initSearchComponents() {

        searchPanel = new JPanel(new BorderLayout());

        final Font searchFont = new Font("Courier", Font.ITALIC ,10);

        searchField = new JTextField(){
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setFont(searchFont);
                g.setColor(Color.GRAY);
                g.drawString(searchFieldText, (getWidth() - (getWidth() / 2)), getHeight() / 2);
            }
        };

        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                exitSearch();
                searchField.requestFocus();
            }
        });


        searchField.getInputMap().put(KeyStroke.getKeyStroke(
                KeyEvent.VK_ENTER, 0),
                "enterKey");
        searchField.getInputMap().put(KeyStroke.getKeyStroke(
                KeyEvent.VK_ESCAPE, 0),
                "escapeKey");

        JPanel buttonPanel = new JPanel(new GridLayout(1,3));

        findButton = new JButton();
        findButton.setToolTipText("Find");
        findButton.setActionCommand(FIND_BTN_CMD);
        findButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(StdFun.RES_PATH + "find.png")));
        findButton.getInputMap().put(KeyStroke.getKeyStroke(
                KeyEvent.VK_ESCAPE, 0),
                "escapeKey");

        nextButton = new JButton();
        nextButton.setToolTipText("Next");
        nextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(StdFun.RES_PATH + "next.png")));
        nextButton.setActionCommand(NEXT_BTN_CMD);
        nextButton.getInputMap().put(KeyStroke.getKeyStroke(
                KeyEvent.VK_ESCAPE, 0),
                "escapeKey");

        prevButton = new JButton();
        prevButton.setToolTipText("Previous");
        prevButton.setActionCommand(PREV_BTN_CMD);
        prevButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(StdFun.RES_PATH + "previous.png")));
        prevButton.getInputMap().put(KeyStroke.getKeyStroke(
                KeyEvent.VK_ESCAPE, 0),
                "escapeKey");


        buttonPanel.add(findButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(prevButton);

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(buttonPanel, BorderLayout.EAST);

    }


    public void setPrevButton(boolean enabled) {
        prevButton.setEnabled(enabled);
    }


    public void setNextButton(boolean enabled) {
        nextButton.setEnabled(enabled);
    }

    public void setRequestFocusNextBtn() {
        nextButton.requestFocusInWindow();
    }

    public void setRequestFocusPrevBtn() {
        prevButton.requestFocusInWindow();
    }



    public void setMultiSelect() {
        scanTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

    /** 
     * Populate table with header and rows accordingly
     *
     */
    public void fillData() {
        
        for (int x = 0 ; x < headerData.size(); x ++) {
            defaultModel.addColumn((String)headerData.elementAt(x));
        }
        for (int x = 0 ; x < rowData.size() ; x++ ) {
            defaultModel.addRow((Vector)rowData.elementAt(x));
        }

    }
    
    public void reSort(int col){
        sorter.setSortingStatus(col, TableSorter.ASCENDING);
    }

    public void reSortDesc(int col){
        sorter.setSortingStatus(col, TableSorter.DESCENDING);
    }
   
    /**
     * Adds a ListSelectionListener to current ListSelectionModel
     *
     * @param listener  ListSelecionListener
     *
     */
    public void addListSelectionListener(ListSelectionListener listener) {
        rsm.addListSelectionListener(listener);    
    }

    @Override
    public void addMouseListener(MouseListener listener) {
        scanTable.addMouseListener(listener);
    }

    
    public <T extends EventListener> T[] geTableListeners(Class<T> listenerType) {     
        return scanTable.getListeners(listenerType);
    }
    
    /**
     * Removes a ListSelectionListener to current ListSelectionModel
     *
     * @param listener ListSelecionListener
     *
     */
    public void removeListSelectionListener(ListSelectionListener listener) {
        rsm.removeListSelectionListener(listener);
    }
    
    @Override
    public void removeMouseListener(MouseListener listener) {
        scanTable.removeMouseListener(listener);
    }

    /**
     * Delete user selected entry
     */
    public void deleteSelectedEntry() {
        defaultModel.removeRow(getSelectedIndex());
    }
    
    /**
     * Delete all entries in table
     */
    public void deleteAllEntries() {
        int rowCount = defaultModel.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            defaultModel.removeRow(i);
        }        
    }

    public void selectAllEntries() {
        scanTable.selectAll();
    }

    public void removeAllCols() {
        defaultModel.setColumnCount(0);
    }
    
   
    /**
     * Adds a row at the end of the table 
     * @param entry Object[]
     */
    public void addListEntry(Object[] entry) {
        defaultModel.addRow(entry);
    }

    /**
     * Return a Vector of objects representing the selected row
     * @return Vector v
     */
    public Vector<Object> getSelectedEntry() {
        
        Vector<Object> v = new Vector<Object>();
        
        for (int x = 0; x < scanTable.getColumnCount(); x++) {
            v.add(scanTable.getValueAt(scanTable.getSelectedRow(), x));
        }
        
        return v;
        
        //return new Object[] {scanTable.getValueAt(getSelectedIndex(),0),scanTable.getValueAt(getSelectedIndex(),1)};
    }

    /**
     * 
     * @return row number selected
     */
    public int getSelectedIndex() {
        return scanTable.getSelectedRow();
    }
    /**
     *
     * @return row numbers selected (when in multi-selection mode)
     */
    public int[] getSelectedIndexes() {
        return scanTable.getSelectedRows();
    }
   
    public int getNextIndex() {

        if (scanTable.getSelectedRow() < (scanTable.getRowCount() -1)) {
            return (getSelectedIndex() + 1);
        }
        else {
            return getSelectedIndex();
        }
    }

    
    public int getPreviousIndex() {

        if (scanTable.getSelectedRow() > 0) {
            return (getSelectedIndex() - 1);
        }
        else {
            return getSelectedIndex();
        }
    }

    /**
     * Gets a String value of the 1st column in the selected row
     *
     * @return String value 
     */
    public String getSelectedCode() {
        return ((String)scanTable.getValueAt(scanTable.getSelectedRow(),0));
    }

    /**
     * Gets a String value of the requested column in the selected row
     *
     * @return String value 
     */
    public String getSelectedValue(int column)  {        
        return ((String)scanTable.getValueAt(scanTable.getSelectedRow(),column));
    }
    
    public String[] getSelectedCodes() {
        int[] rows = scanTable.getSelectedRows();
        String[] selectedCodes = new String[rows.length];
        for (int x=0;x<rows.length; x++) {
            selectedCodes[x] = (String)scanTable.getValueAt(rows[x],0);
        }
        return selectedCodes;
    }
    
    public String getNextCode() { 

        int currentRow = getSelectedIndex();

        if (currentRow  < (scanTable.getRowCount() - 1)) {
            scanTable.setRowSelectionInterval(currentRow + 1, currentRow + 1);
        }
        else {
            scanTable.setRowSelectionInterval(currentRow, currentRow);
        }
        
        return getSelectedCode();

    }
    
    public String getPreviousCode() {

        int currentRow = getSelectedIndex();

        if (currentRow  > 0) {
            scanTable.setRowSelectionInterval(currentRow - 1, currentRow - 1);
        }
        else {
            scanTable.setRowSelectionInterval(currentRow, currentRow);
        }
        
        return getSelectedCode();
       
    }
    
   /**
    * Sets the value of the rowIndex details at position row with value Object[]
    * 
    * @param rowIndex (int)
    * @param colValues (Object[])
    */
    public void setRowDetailsValue(int rowIndex, Object[] colValues) {

        for (int colIndex = 0 ; colIndex < colValues.length; colIndex++) {
            if (colValues[colIndex] instanceof Float) {
                sorter.setValueAt(df.format((Float)colValues[colIndex]),rowIndex,colIndex) ;
            } else {
                sorter.setValueAt(colValues[colIndex],rowIndex,colIndex) ;
            }
        }
    }

   /**
    * Sets ScanWindowPanel Vectors values: rowData, headerData, colWidth. 
    * This method also converts a Float object to String in using DecimalFormat
 as defined in stdClasses.StdFun.DECIMAL_FORMAT and creates a Vector
 decimalCells to store the column numbers of those columns to indent in
 right when defining JTable scanTable
    *
    * @param listDet Vector
    * @see stdClasses.StdFun.DA_DECIMAL_FORMAT
    */
    
    public void initEntries(Vector<Vector> listDet) {

        this.rowData = (Vector<Vector>)listDet.elementAt(0);
        this.headerData = (Vector)listDet.elementAt(1);
        this.colWidth = (Vector)listDet.elementAt(2);
        
        Vector<Integer> decimalCells = new Vector<Integer>();

        int[] colsMaxWidth = new int[headerData.size()];
        
        Iterator<Vector> i = rowData.iterator();
        while (i.hasNext()) {
            Vector<Object> lineData = i.next();

            for (int x = 0; x < lineData.size(); x ++) {

                if (lineData.elementAt(x) != null) {
                    if (colsMaxWidth[x] < lineData.elementAt(x).toString().length()) {
                        colsMaxWidth[x] = lineData.elementAt(x).toString().length();
                    }

                    if (lineData.elementAt(x) instanceof Float) {
                        lineData.setElementAt(df.format((Float)lineData.elementAt(x)), x);
                        decimalCells.addElement(new Integer(x));
                    } 
                } else {
                    //colsMaxWidth[x] = 2;
                }
            }            
        }

        if (decimalCells.size() > 0) { 
            try {
                scanTable.setDefaultRenderer(Class.forName("java.lang.Object"),
                    new CustomTableCellRenderer(decimalCells));
            } catch(ClassNotFoundException e) { }
        } else {
            try {
                scanTable.setDefaultRenderer(Class.forName("java.lang.Object"),
                    new DefaultTableCellRenderer(){
                        @Override
                        public Component getTableCellRendererComponent(JTable table, Object value,
                            boolean isSelected, boolean hasFocus, int row, int column) {

                            String toolTip = toolTipText;
                            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                            if (!isSelected) {
                                cell.setBackground(backGroundColor);
                            } 

                            if ((markedRows != null) && (!markedRows.isEmpty()) && (markedColor != null)) {
                                if (toBeMarked((String)table.getValueAt(row, 0))) {
                                    if (!isSelected) {
                                        cell.setBackground(markedColor);
                                        toolTip = markedCellsToolTip;
                                    }
                                }
                            }

                            setToolTipText(toolTip);
                            return cell;
                        }

                });
            } catch (ClassNotFoundException e) {  }
        }

        fillData();

        //Set columns width proportional to contents ----------------------
        int colsContentsTotWidth = 0;
        for (int x= 0; x < colsMaxWidth.length; x ++) {
            colsContentsTotWidth += colsMaxWidth[x];
        }

        int panelTotWidth = scanTable.getColumnModel().getTotalColumnWidth();

        int tableColWidth;
        for (int x= 0; x < colsMaxWidth.length; x ++) {
            tableColWidth = Math.round((colsMaxWidth[x] * panelTotWidth) / (float)colsContentsTotWidth);
            scanTable.getColumnModel().getColumn(x).setPreferredWidth(tableColWidth);
        }
        //-----------------------------------------------------------------

    }

    /**
     * Define rows to be marked in a different color than normal rows.<br/>
     * For example one can define terminated employees to be shown in the list in a different color than the active ones
     *
     * @param markedRows Vector containing unique code (1st column of table) of those rows to be marked
     * @param markedColor 
     *
     */
    public void setMarkedRows (Vector<String> markedRows, Color markedColor, String markedCellsTooltip) {
        this.markedRows = markedRows;
        this.markedColor = markedColor;
        this.markedCellsToolTip = markedCellsTooltip;
    }


    @Override
    public void setEnabled(boolean selectMode) {


        if (findButton != null) {
            findButton.setEnabled(selectMode);
            searchField.setEditable(selectMode);

            if (selectMode == false) {
                exitSearch();
                scanTable.requestFocus();
            }

        }
        scanTable.setEnabled(selectMode);
   }

    @Override
    public void requestFocus() {
        scanTable.requestFocus();
    }
    
    @Override
    public boolean isEnabled() {
        return scanTable.isEnabled();
   }

    /**
     * Set the selected row in this <code>ScanWindowPanel</code> according to the 
     * given value
     * 
     * @param code - String to be shown as selected
     * @param col - int column where value is to be matched
     * 
     * @return - -1 if given code does not exist or otherwise if succesful
     */
    public void setSelectedIndex(String code, int col) {

        for (int x = 0; x < scanTable.getRowCount(); x ++) {
            String entry = (String)scanTable.getValueAt(x, col);

            if (entry.equals(code)) {
                try {
                    scanTable.setRowSelectionInterval(x, x);
                    break;
                }
                catch(IllegalArgumentException e){
                }
            }
        }


        if (!Scrolling.isVerticallyVisible(scanTable, getRowBounds(scanTable.getSelectedRow()))) {
            makeRowVisible(scanTable.getSelectedRow());
        }

    }


    /**
     * Set the selected row in this <code>ScanWindowPanel</code> according to the
     * given row position
     *
     * @param rowPos - row position
     * @return - -1 if given code does not exist or otherwise if successful
     */
    public void setSelectedIndex(int rowPos) {

        scanTable.setRowSelectionInterval(rowPos, rowPos);

    }

    public void setSelectedIndex(ArrayList<Integer> rowsPos) {

        setMultiSelect();
        ListSelectionModel model = scanTable.getSelectionModel();
        model.clearSelection();

        for (int sel: rowsPos) {
            model.addSelectionInterval(sel, sel);
        }

    }

    public int getRowCount() {

        return scanTable.getRowCount();
    }


    /**
     * Get an <code>ArrayList</code> of rows positions for a given search String 
     * in a particular column 
     * 
     * @param searchStr
     * @param col
     * @param ignoreCase - ignore uppercase and lowercase
     * @return <code>ArrayList</code>
     */
    public ArrayList<Integer> getSearchStrPos(String searchStr, int col, boolean ignoreCase) {

        ArrayList<Integer> rowsPos = new ArrayList<Integer>();

        for (int x = 0; x < scanTable.getRowCount(); x++) {
            if (ignoreCase) {
                if (((String)scanTable.getValueAt(x, col)).toUpperCase().indexOf(searchStr.toUpperCase()) > -1) {
                    rowsPos.add(x);
                }
            } else {
                if (((String)scanTable.getValueAt(x, col)).indexOf(searchStr) > -1) {
                    rowsPos.add(x);
                }
            }
        }

        return rowsPos;

    }

    public int getRowCount(String searchStr, int col) {

        int rowsFound = 0;

        for (int x = 0; x < scanTable.getRowCount(); x++) {

            if (((String)scanTable.getValueAt(x, col)).indexOf(searchStr) > -1) {
                rowsFound++;
            }

        }

        return rowsFound;



    }

    private boolean toBeMarked(String colValue) {

        boolean markCell = false;

        for (Iterator<String> markIter = markedRows.iterator(); markIter.hasNext();) {

            String iterValue = markIter.next();

            if (iterValue.equals(colValue)) {
                markCell = true;
                break;
            }
        }

        return markCell;

    }

    private class CustomTableCellRenderer extends DefaultTableCellRenderer {

        private Vector<Integer> decimalCols;
        
        public CustomTableCellRenderer(Vector<Integer> decimalCols) {
            this.decimalCols = decimalCols;            
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
            boolean isSelected, boolean hasFocus, int row, int column) {

            String toolTip = toolTipText;
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            cell.setBackground(backGroundColor);

            if ((markedRows != null) && (!markedRows.isEmpty()) && (markedColor != null)) {
                if (toBeMarked((String)table.getValueAt(row, 0))) {

                    cell.setBackground(markedColor);
                    toolTip = markedCellsToolTip;
                } 
            }

            
            boolean numericCol = false;
            
            Iterator<Integer> cols = decimalCols.iterator();
            while (cols.hasNext()) {
                if (cols.next().intValue() == column) {
                    numericCol = true;
                    break;
                }
            }
                        
            if (numericCol) {
                setHorizontalAlignment(JLabel.RIGHT);
            }
            else {
                setHorizontalAlignment(JLabel.LEFT);
            }

            setToolTipText(toolTip);
            return cell;
        }

    }

}
