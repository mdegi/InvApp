/*
 * ScanWindow.java
 *
 * Created on October 4, 2006, 2:39 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package stdClasses;

import java.awt.event.MouseEvent;
import java.util.Vector;

import java.awt.Point;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Event;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.AbstractAction;
import javax.swing.JButton;

import javax.swing.JTable;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
//import javax.swing.table.TableColumn;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author nla
 */
public class ScanWindow extends JDialog implements MouseListener {

    private final Vector rowData;
    
    private Vector headerData, colWidth;
    private Vector<String> markedRows;

    private JTable scanTable;
    private JScrollPane jScrollPane;

    private String selectedCode, toolTipText, markedCellsToolTip;
    private int dialogWidth;
    private final MouseListener mouseListener;
    private final Point pos;
    private final ScanWindowListener swListener;

    private JPanel scanPanel;

    private int searchArrayPos = -1;
    private ArrayList<Integer> searchedRowsPos;

    private String searchFieldText = "";
    private JPanel searchPanel;
    private JTextField searchField;

    private JButton findButton;
    private JButton nextButton;
    private JButton prevButton;

    private Color backGroundColor, markedColor;

    private final String FIND_BTN_CMD = "FIND_BUTTON_CMD";
    private final String NEXT_BTN_CMD = "NEXT_BUTTON_CMD";
    private final String PREV_BTN_CMD = "PREV_BUTTON_CMD";



    /** Creates a new instance of ScanWindow
     * @param listDet
     * @param windowTitle
     * @param pos
     * @param swListener */
    public ScanWindow(Vector listDet, String windowTitle, Point pos, ScanWindowListener swListener) {


        setTitle(windowTitle);

        this.rowData = (Vector)listDet.elementAt(0);
        this.headerData = (Vector)listDet.elementAt(1);
        this.colWidth = (Vector)listDet.elementAt(2);
        this.mouseListener = this;
        this.swListener = swListener;
        this.pos = pos;
        selectedCode = null;
    }
     
    /**
     * Show the window containing entries list for the user to select from
     * 
     */
    public void showListWindow() {
     
        if (rowData.size() > 0) { 
            initComponents();
            getContentPane().add(scanPanel);
            setLocation((int)pos.getX(),(int)(pos.getY() + 75));
            //setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null,"No Data Match Your Selection Criteria",StdFun.SYSTEM_TITLE,JOptionPane.OK_OPTION) ;
        }
    }
    
    /**
     * Initialise required components
     *
     */
    public void initComponents() {

        scanPanel = new JPanel(new BorderLayout()); 

        backGroundColor = Color.WHITE;

        DefaultTableModel defaultModel = new DefaultTableModel(rowData,headerData) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        TableSorter sorter = new TableSorter(defaultModel);
        scanTable = new JTable(sorter);
        sorter.setTableHeader(scanTable.getTableHeader()); //ADDED THIS
        
        scanTable.setRowSelectionAllowed(true);
        scanTable.setColumnSelectionAllowed(false);
        scanTable.getTableHeader().setReorderingAllowed(false);
        scanTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scanTable.addMouseListener(mouseListener);

        scanTable.getInputMap().put(KeyStroke.getKeyStroke(
            KeyEvent.VK_F, Event.CTRL_MASK),
            "ctrl-f");

        scanTable.setToolTipText("Double click to select . . . .");
        scanTable.getTableHeader().setToolTipText("Click on header columns to sort");

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


        int tmpInt = 0;
        dialogWidth = 0;
        for (int x= 0; x < colWidth.size(); x ++) {
            tmpInt = ((Integer)colWidth.elementAt(x)) * 6 ; // this sets col size to column defined size * 6 since 1 pixel
                                                                       // on screen is much less in size than 1 char defined in mySQL
            scanTable.getColumnModel().getColumn(x).setPreferredWidth(tmpInt);
            dialogWidth += tmpInt;
        }
        
        jScrollPane = new JScrollPane(scanTable);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(new Dimension(dialogWidth, 400));
        setResizable(false);
        setModal(true);

        scanPanel.add(jScrollPane, BorderLayout.CENTER);
        
    }

    /**
     * Define rows to be marked in a different color than normal rows.<br/>
     * For example one can define terminated employees to be shown in the list in a different color than the active ones
     *
     * @param markedRows Vector containing unique code (1st column of table) of those rows to be marked
     * @param markedColor
     *
     */
    public void setMarkedRows (Vector<String> markedRows, Color markedColor, String markedCellsToolTip) {
        this.markedRows = markedRows;
        this.markedColor = markedColor;
        this.markedCellsToolTip = markedCellsToolTip;
    }

    private boolean toBeMarked(String colValue) {
        boolean markCell = false;

        for (String iterValue : markedRows) {
            if (iterValue.equals(colValue)) {
                markCell = true;
                break;
            }
        }
        return markCell;
    }

    public void initSearchPanel(final Frame parentFrame, String title) {
        initSearchComponents();

        final JPanel outPanel = scanPanel;

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
                        if (!Scrolling.isVerticallyVisible(scanTable, getRowBounds(searchedRowsPos.get(0)))) {
                            makeRowVisible(searchedRowsPos.get(0));
                        }

                        searchArrayPos++;
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

        scanTable.setToolTipText(scanTable.getToolTipText() + "Use <Ctrl-F> to search");
        setVisible(true);
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

        ArrayList<Integer> rowsPos = new ArrayList<>();

        for (int x = 0; x < scanTable.getRowCount(); x++) {
            if (ignoreCase) {
                if (((String)scanTable.getValueAt(x, col)).toUpperCase().contains(searchStr.toUpperCase())) {
                    rowsPos.add(x);
                }
            } else {
                if (((String)scanTable.getValueAt(x, col)).contains(searchStr)) {
                    rowsPos.add(x);
                }
            }
        }
        return rowsPos;
    }

    /**
     * Set the selected row in this <code>ScanWindowPanel</code> according to the
     * given value
     *
     * @param code - String to be shown as selected
     * @return - -1 if given code does not exist or otherwise if succesful
     */
    public int setSelectedIndex(String code) {
        int selectedIndex = -1;

        for (int x = 0; x < scanTable.getRowCount(); x ++) {
            String entry = (String)scanTable.getValueAt(x, 0);

            if (entry.equals(code)) {
                try {
                    scanTable.setRowSelectionInterval(x, x);
                    break;
                }
                catch(IllegalArgumentException e){
                }
            }
        }
        return selectedIndex;
    }


    /**
     * Set the selected row in this <code>ScanWindowPanel</code> according to the
     * given row position
     *
     * @param rowPos - row position
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

    public void setMultiSelect() {
        scanTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }


    public Object[] getSelectedEntry() {
        
        Object [] selectedEntry = new Object[scanTable.getColumnCount()];
        if (scanTable.getSelectedRow() != -1) {
            for (int x = 0; x < scanTable.getColumnCount(); x++) {
                selectedEntry[x] = scanTable.getValueAt(scanTable.getSelectedRow(), x);
            }
        } 
        return selectedEntry;
    }
    
    public String getSelectedCode() {

        Object selectedRow[] = getSelectedEntry();
        selectedCode = (String)selectedRow[0];
        return selectedCode;
    }



    @Override
    public void mouseClicked(MouseEvent evt) {     
        if (evt.getClickCount() ==2 ) {
            swListener.displaySelectedRow(getSelectedCode());
            dispose();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    /**
     * Force the calling class to implement displaySeletedRow(String selectedCode)
     * method after an entry is selected by the user from the window
     */
    public interface ScanWindowListener {
        public void displaySelectedRow(String selectedCode);
    }
}
