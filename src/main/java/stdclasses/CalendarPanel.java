/*
 * CalendarPanel.java
 *
 * Created on December 7, 2005, 5:14 PM
 */

package stdClasses;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class CalendarPanel extends JPanel implements ActionListener {
    static final int YEARSINCOMBO=10;    //Should be an even number
    static String[] sYears= new String[YEARSINCOMBO];
    final static String[] sMonths= {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
    final Color THIS_DAY_COLOR = new Color(255,163,70);
    
    static int[] sDays= { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
    static String[] sDotw= { "M", "T", "W", "T", "F", "S", "S" };
    
    protected Calendar mCalendar= new GregorianCalendar();
    protected JButton mDateButton;
    protected Color mBackground;
    protected JPanel mDayPanel;

    private Font mFont= new Font("SansSerif", Font.PLAIN, 10);
    private Font mItalicFont= new Font("SansSerif", Font.ITALIC, 10);
    private Font mBoldFont= new Font("SansSerif", Font.BOLD, 12);
    private CalendarPanelListener mListener;
    private static final JComboBox mMonthsCombo=new JComboBox(sMonths);
        
    private static final JList lMonthsList=new JList(sMonths);
    private static final JList lYearsList=new JList(sYears);

    private JButton prevMonth, nextMonth, prevYear, nextYear;
    private JButton janDayOne, decDayLast, thisMonthDayLast, thisMonthDayOne;

    private JTextField monthField, yearField;
    private Font buttonFont = new Font("SansSerif",Font.BOLD,8);
    private int currSelYear, currSelMonth;
   
    private JComboBox mYearsCombo;
    
    private final String PREV_YEAR_CMD = "PREV_YEAR";
    private final String NEXT_YEAR_CMD = "NEXT_YEAR";
    private final String PREV_MONTH_CMD = "PREV_MONTH";
    private final String NEXT_MONTH_CMD = "NEXT_MONTH";
    private final String JAN_1ST_CMD = "JAN_1ST";
    private final String DEC_31ST_CMD = "DEC_31ST";
    private final String START_MONTH_CMD = "START_OF_MOTH";
    private final String END_MONTH_CMD = "END_OF_MONTH";

    public CalendarPanel(CalendarPanelListener listener) {
        mListener= listener;
        
        setLayout(new BorderLayout());

        JPanel navigatePanel=new JPanel(new BorderLayout());

        //********************************** NLA
        JPanel monthPanel = new JPanel(new BorderLayout());
        Dimension navButtonsDimension = new Dimension(40,20);

        prevMonth = new JButton("<");
        prevMonth.setFont(buttonFont);
        prevMonth.addActionListener(this);
        prevMonth.setActionCommand(PREV_MONTH_CMD);
        prevMonth.setPreferredSize(navButtonsDimension);

        nextMonth = new JButton(">");
        nextMonth.setFont(buttonFont);
        nextMonth.addActionListener(this);
        nextMonth.setActionCommand(NEXT_MONTH_CMD);
        nextMonth.setPreferredSize(navButtonsDimension);

        currSelMonth = mCalendar.get(Calendar.MONTH);

        monthField = new JTextField();
        monthField.setPreferredSize(new java.awt.Dimension(75,20));
        monthField.setEditable(false);
        monthField.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        monthField.setText(String.valueOf(sMonths[currSelMonth]));

        monthPanel.add(prevMonth, BorderLayout.WEST);
        monthPanel.add(monthField, BorderLayout.CENTER);
        monthPanel.add(nextMonth, BorderLayout.EAST);

        //Year Panel --------------------------------------
        currSelYear = mCalendar.get(Calendar.YEAR);

        JPanel yearPanel = new JPanel(new BorderLayout());        
        
        prevYear = new JButton("<");
        prevYear.setFont(buttonFont);
        prevYear.addActionListener(this);
        prevYear.setActionCommand(PREV_YEAR_CMD);
        prevYear.setPreferredSize(navButtonsDimension);

        nextYear = new JButton(">");
        nextYear.setFont(buttonFont);
        nextYear.addActionListener(this);
        nextYear.setActionCommand(NEXT_YEAR_CMD);
        nextYear.setPreferredSize(navButtonsDimension);

        yearField = new JTextField();
        yearField.setEditable(false);
        yearField.setText(String.valueOf(currSelYear));
        //yearField.setPreferredSize(new java.awt.Dimension(40,20));
        yearField.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        yearPanel.add(prevYear, BorderLayout.WEST);
        yearPanel.add(yearField, BorderLayout.CENTER);
        yearPanel.add(nextYear, BorderLayout.EAST);

        JPanel navPanel = new JPanel(new FlowLayout());
        navPanel.add(monthPanel);
        navPanel.add(yearPanel);

        //ShortCutsPanel -----------------------------------------------
        JPanel shortCutsPanel = new JPanel(new FlowLayout());
        shortCutsPanel.setBorder(BorderFactory.createEtchedBorder());
        
        Dimension shortCutsButtonsDimension = new Dimension(65,20);

        janDayOne = new JButton("01 Jan");
        janDayOne.setFont(buttonFont);
        janDayOne.addActionListener(this);
        janDayOne.setActionCommand(JAN_1ST_CMD);
        janDayOne.setPreferredSize(shortCutsButtonsDimension);

        decDayLast = new JButton("31 Dec");
        decDayLast.setFont(buttonFont);
        decDayLast.addActionListener(this);
        decDayLast.setActionCommand(DEC_31ST_CMD);
        decDayLast.setPreferredSize(shortCutsButtonsDimension);

        thisMonthDayLast = new JButton(getEndOfMonthDay() + " " + monthField.getText().substring(0,3));
        thisMonthDayLast.setFont(buttonFont);
        thisMonthDayLast.addActionListener(this);
        thisMonthDayLast.setActionCommand(END_MONTH_CMD);
        thisMonthDayLast.setPreferredSize(shortCutsButtonsDimension);

        thisMonthDayOne = new JButton("01 " + monthField.getText().substring(0,3));
        thisMonthDayOne.setFont(buttonFont);
        thisMonthDayOne.addActionListener(this);
        thisMonthDayOne.setActionCommand(START_MONTH_CMD);
        thisMonthDayOne.setPreferredSize(shortCutsButtonsDimension);

        shortCutsPanel.add(janDayOne);
        shortCutsPanel.add(decDayLast);
        shortCutsPanel.add(thisMonthDayOne);
        shortCutsPanel.add(thisMonthDayLast);
        //**********************************

        mMonthsCombo.setSelectedIndex(mCalendar.get(Calendar.MONTH));
        mMonthsCombo.setFont(mFont);        
        
        int currentYear=mCalendar.get(Calendar.YEAR);
        int firstYear=currentYear-(YEARSINCOMBO/2);
        for (int y=0; y < YEARSINCOMBO; y++) sYears[y]=String.valueOf(firstYear+y);

        mYearsCombo= new JComboBox(sYears);
        mYearsCombo.setSelectedItem(String.valueOf(currentYear));
        mYearsCombo.setFont(mFont);

        navigatePanel.add(mMonthsCombo, BorderLayout.WEST);
        navigatePanel.add(mYearsCombo, BorderLayout.EAST);
              
        mMonthsCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setMonth(mMonthsCombo.getSelectedIndex());
            }
        }); 
        
        
        mYearsCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setYear(Integer.parseInt((String)mYearsCombo.getSelectedItem()));
            }
        });
        

        lMonthsList.setSelectedIndex(mCalendar.get(Calendar.MONTH));
        lYearsList.setSelectedValue(String.valueOf(currentYear),true); 
        
        lMonthsList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                setMonth(lMonthsList.getSelectedIndex());
            }
        });
        
        lYearsList.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                setYear(Integer.parseInt(sYears[lYearsList.getSelectedIndex()]));
            }
        });
        
        mDayPanel= getDayPanel();
        
        this.setBorder(new EmptyBorder(2,4,2,4));
        
        navigatePanel.setSize(getWidth(), 300);

        
        this.add(navPanel, BorderLayout.NORTH);
        this.add(mDayPanel, BorderLayout.CENTER);
        //do not need this 1st of month and year & end of month & year shortcuts for this app
        //this.add(shortCutsPanel, BorderLayout.SOUTH);

        this.requestFocusInWindow();
    }
    
    private void setYear(int year) {
        mCalendar.set(Calendar.YEAR, year);
        switchDayPanel();
    }
    
    private void setMonth(int month) {
        mCalendar.set(Calendar.MONTH, month);
        switchDayPanel();
    }
    
    private void switchDayPanel() {
        JPanel dayPanel= getDayPanel();
        this.remove(mDayPanel);
        this.add(dayPanel, BorderLayout.CENTER);
        mDayPanel=dayPanel;
        // nasty, but update() does not work 8(
//        this.setVisible(false);
//        this.setVisible(true);
        this.revalidate();
        this.repaint();
    }
    
    private JPanel getDayPanel() {
        JPanel dayPanel= new JPanel(new GridLayout(0,7));
        
        int day= 1;
        int todayDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        int todayMonth = mCalendar.get(Calendar.MONTH);
        int todayYear = mCalendar.get(Calendar.YEAR);
        
        int days= sDays[mCalendar.get(Calendar.MONTH)];
        mCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int start= mCalendar.get(Calendar.DAY_OF_WEEK) -2; // SUNDAY == 1
        mCalendar.set(Calendar.DAY_OF_MONTH, todayDay);
        
        //****nla added ***
        int year=mCalendar.get(Calendar.YEAR);
        if (((year%4)==0) && mCalendar.get(Calendar.MONTH)==1) days++;
        //**********
        
        if (start < 0) {
            start= 6;
        }        // sunday
        
        EmptyBorder border= new EmptyBorder(2,2,2,2);
        ActionListener actionListener= new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton btn=(JButton)e.getSource();
                mCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(btn.getText()));
                //btn.setBackground(Color.yellow);
                //mDateButton.setBackground(mBackground);
                mDateButton=btn;
                mListener.select(mCalendar.getTime());
            }
        };
        
        for (int i= 0; i< 7; i++) {
            JLabel label= new JLabel(sDotw[i]);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setFont(mBoldFont);
            dayPanel.add(label);
        }
        
        for (int row= 0; row < 6; row++) {
            int firstCol= (row == 0 ? start : 0);
            for (int col= 0; col< firstCol; col++) dayPanel.add(new JLabel());
            for (int col= firstCol; col< 7; col++) {
                if (day <= days) {
                    JButton btn= new JButton("" +(day));
                    if (mBackground == null) {
                        mBackground= btn.getBackground();
                    }
            
                    StringBuilder calDayStr = new StringBuilder("");
                    if (day < 10) {
                        calDayStr.append("0");
                    }
                    calDayStr.append(day);
                    calDayStr.append("/");
                    
                    if (currSelMonth < 9) {
                        calDayStr.append("0");
                    }
                    calDayStr.append(currSelMonth + 1);
                    calDayStr.append("/");
                    calDayStr.append(currSelYear);

                    btn.setBorder(border);
                    btn.setFont(mFont);

                    if (calDayStr.toString().equals(StdFun.getStandardDate(StdFun.getToday()))) {
                        btn.setBackground(THIS_DAY_COLOR);
                    }
                    /*if (day == todayDay) {
                        if (currSelMonth == todayMonth) {
                            if (currSelYear == todayYear) {
                                mDateButton= btn;       
                                
                            }
                        }
                    } */
                    btn.addActionListener(actionListener);
                    dayPanel.add(btn);
                    day++;
                } else {
                    dayPanel.add(new JLabel());
                }
            }
        }

        if (mDateButton != null) {
            mDateButton.setBackground(THIS_DAY_COLOR);
        }
        
        return dayPanel;
    }

    private void setMonthShortCutButtons() {
        thisMonthDayOne.setText("01 " + monthField.getText().substring(0,3));
        thisMonthDayLast.setText(getEndOfMonthDay() + " " + monthField.getText().substring(0,3));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand().equals(PREV_MONTH_CMD)) {
            deductMonth();
            monthField.setText(sMonths[currSelMonth]);
            setMonth(currSelMonth);

            setMonthShortCutButtons();

        }
        if (e.getActionCommand().equals(NEXT_MONTH_CMD)) {
            addMonth();
            monthField.setText(sMonths[currSelMonth]);
            setMonth(currSelMonth);

            setMonthShortCutButtons();

        }
        if (e.getActionCommand().equals(PREV_YEAR_CMD)) {
            currSelYear--;
            yearField.setText(String.valueOf(currSelYear));
            setYear(currSelYear);
            setMonthShortCutButtons();
        }
        if (e.getActionCommand().equals(NEXT_YEAR_CMD)) {
            currSelYear++;
            yearField.setText(String.valueOf(currSelYear));
            setYear(currSelYear);
            setMonthShortCutButtons();
        }

        if (e.getActionCommand().equals(JAN_1ST_CMD)) {
            mCalendar.set(Calendar.DAY_OF_MONTH, 1);
            mCalendar.set(Calendar.MONTH, 0);
            mCalendar.set(Calendar.YEAR,Integer.parseInt(yearField.getText()));

            mListener.select(mCalendar.getTime());
        }
        if (e.getActionCommand().equals(DEC_31ST_CMD)) {
            mCalendar.set(Calendar.DAY_OF_MONTH, 31);
            mCalendar.set(Calendar.MONTH, 11);
            mCalendar.set(Calendar.YEAR,Integer.parseInt(yearField.getText()));
            
            mListener.select(mCalendar.getTime());
        }
        if (e.getActionCommand().equals(START_MONTH_CMD)) {
            mCalendar.set(Calendar.DAY_OF_MONTH, 1);
            mCalendar.set(Calendar.MONTH, currSelMonth);
            mCalendar.set(Calendar.YEAR,Integer.parseInt(yearField.getText()));

            mListener.select(mCalendar.getTime());
        }
        if (e.getActionCommand().equals(END_MONTH_CMD)) {

            mCalendar.set(Calendar.DAY_OF_MONTH, 1);
            mCalendar.set(Calendar.MONTH, currSelMonth);
            mCalendar.set(Calendar.YEAR,Integer.parseInt(yearField.getText()));

            mListener.select(getEndOfMonthDate(mCalendar));

        }
    }

    private Date getEndOfMonthDate(Calendar dt){

        Calendar date = Calendar.getInstance();

        date.setTime(dt.getTime());

        int currentMonth = date.get(Calendar.MONTH);

        do {
             date.add(Calendar.DATE, 1);
        } while (date.get(Calendar.MONTH) == currentMonth) ;


        date.add(Calendar.DATE, -1); // because counter ended in 1st day of next month
        return date.getTime();

    }

    private int getEndOfMonthDay() {

        Calendar date = Calendar.getInstance();
        date.set(Calendar.DAY_OF_MONTH, 1);
        date.set(Calendar.MONTH, currSelMonth);
        date.set(Calendar.YEAR,Integer.parseInt(yearField.getText()));

        date.setTime(getEndOfMonthDate(date));
        return date.get(Calendar.DAY_OF_MONTH);

    }


    private void deductMonth() {

        currSelMonth--;
        if (currSelMonth < 0) {
            currSelMonth = 11;
        }

    }

    private void addMonth() {
        currSelMonth++;
        if (currSelMonth > 11) {
            currSelMonth = 0;
        }
    }


    public interface CalendarPanelListener {
        public void select(Date date);
    }
}
