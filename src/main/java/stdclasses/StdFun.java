/*
 * StdFun.java
 *
 * Created on August 29, 2006, 2:04 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package stdClasses;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;


/**
 * Standard methods, constants and variables to be used through the whole
 * application to be defined here
 * @author user
 */
public final class StdFun {
  
    public static final String SYSTEM_TITLE = "Inv App";

    public static final String D_NAME = "misc";
    
    public static final String RES_PATH = "/";
    //-------------------------------------------------------------
    //Define standard colors used throughout the system -----------
    //-------------------------------------------------------------
    public static final Color LIGHT_YELLOW = new Color(255,255,204);
    public static final Color READ_ONLY_COLOR = new Color(0,128,192);
    public static final Color LIGHT_GREY = new Color(238,238,238);
    
    public static final Color SOLD_CAR = new Color(170,244,157);
    public static final Color CLOSED_SHIPMENT = new Color(211,206,206);
    //-------------------------------------------------------------

    public static final String STANDARD_DATE_FORMAT = "dd/MM/yyyy";
    public static final String SQL_DATE_FORMAT = "yyyy-MM-dd";
    
    public static final String INT_FORMAT = "#,##0";
    public static final String DECIMAL_FORMAT = "#,###,##0.00";
    public static final String CURRENCY_ROE_FORMAT = "##0.00000";
    
    public static final String eurSign = "\u20AC";    
    public static final String yenSign = "\u00A5";    
    public static final String stgSign = "\u00A3";    

    public static final String OK_BTN_CMD = "OK_COMMAND";
    public static final String QUIT_BTN_CMD = "QUIT_COMMAND";

    public static final FileNameExtensionFilter IMG_EXTENSION_FILTER = new FileNameExtensionFilter("Image Files", "png", "jpg", "gif");    
    public static final FileNameExtensionFilter PDF_EXTENSION_FILTER = new FileNameExtensionFilter("PDF Files", "pdf");
    
    public static final String PROPS_FILE_NAME = "INVAPP.PROPERTIES";
    
    //Do not allow anyone create an instance of this class
    private StdFun() {}
    
    
    /**
     * Initialise location of this JFrame in centre of calling frame
     * 
     * 
     * @param parentPos top left corner of calling JFrame
     * @param parentDimension of calling JFrame
     * @param childDimension
     * 
     * @return top left corner x & y co-ordinates
     */
    public static Point centreWindow(Point parentPos, Dimension parentDimension,
            Dimension childDimension) {
      
        int xPos, yPos;
        
        xPos = ((int)(parentDimension.getWidth() - childDimension.getWidth())/2) + 
                (int)parentPos.getX();
        yPos = ((int)(parentDimension.getHeight() - childDimension.getHeight())/2) + 
                (int)parentPos.getY();
        
        return new Point(xPos,yPos);
    }
    
    /**
     * Initialise location of this JInternalFrame in centre of calling frame
     * Here the Point position of the Container frame is not important
     * 
     * 
     * @param parentDimension of calling JFrame
     * @param childDimension
     * 
     * @return top left corner x & y co-ordinates
     */
    public static Point centreWindow(Dimension parentDimension,
            Dimension childDimension) {
      
        int xPos, yPos;
        
        xPos = ((int)(parentDimension.getWidth() - childDimension.getWidth())/2);
        yPos = ((int)(parentDimension.getHeight() - childDimension.getHeight())/2) ;
        
        return new Point(xPos,yPos);
    }

    
    /**
     * Converts a given String 1st char to uppercase. This is done by converting the
     * parameter string into lower case (in case if it is passed all in uppercase) 
     * and then converts the 1st char to uppercase
     * 
     * @param toConvert
     * @return convertedStr
     */
    public static String upper1stChar(String toConvert) {
        String lCaseStr  = toConvert.toLowerCase();
        String convertedStr = lCaseStr.substring(0, 1).toUpperCase() + lCaseStr.substring(1);
        
        return convertedStr;
    }
    
    /**
     * Return Eur sign symbol (ascii value of 1113) as a String
     * 
     * @return String
     */
    public static String getEuroSign() {
        return "\u20AC";
    }


    /**
     * Converts a date String in dd/MM/yyyy format to SQL format
     * yyyy-MM-dd
     * 
     * @param usrDate String dd/MM/yyyy
     * @return String date in SQL format yyyy-MM-dd
     */
    public static String getSQLDate(String usrDate) { 
        String sqlDate = "";
        
        if ((usrDate != null) && (!usrDate.equals(""))) {
           
            try {
                Date date = new SimpleDateFormat(StdFun.STANDARD_DATE_FORMAT).parse(usrDate);
                sqlDate = new SimpleDateFormat(StdFun.SQL_DATE_FORMAT).format(date);
            } catch (ParseException ex) {
                Logger.getLogger(StdFun.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return sqlDate;
    }
    
    /**
     * Converts a java.util.Date to SQL format: yyyy-MM-dd
     *
     * @param date Date object
     * @return sqlDate - String yyyy-MM-dd
     */
    public static String getSQLDate(Date date) {
        String sqlDate = "";
        if (date != null) {
            sqlDate = new SimpleDateFormat(StdFun.SQL_DATE_FORMAT).format(date);
        }
        return sqlDate;
    }
    

    /**
     * Converts a date String in yyyy-mm-dd format to standard known format
     * dd/mm/yyyy
     * 
     * @param sqlDate
     * @return String
     */
    public static String getStandardDate(String sqlDate) {
        String stdDate = "";
        
        if ((sqlDate != null) && (!sqlDate.equals(""))) {
            try {
                Date date = new SimpleDateFormat(StdFun.SQL_DATE_FORMAT).parse(sqlDate);
                stdDate = new SimpleDateFormat(StdFun.STANDARD_DATE_FORMAT).format(date);
            } catch (ParseException ex) {
                //Logger.getLogger(StdFun.class.getName()).log(Level.SEVERE, null, ex);
            }            
        }   
        return stdDate;
    }

    /**
     * Converts a date Date to a String in standard known format dd/MM/yyyy
     * 
     * @param date
     * @return String
     */
    public static String getStandardDate(Date date) {
        String stdDate = "";

        if (date != null) {
            
            stdDate = new SimpleDateFormat(StdFun.STANDARD_DATE_FORMAT).format(date);
            
        } 
        return stdDate;
    }
    
    /**
     * Converts a String representation of a Date in format (dd/MM/yyyy) to an
     * actual Date() object 
     * <p> A null value is returned if the String usrDate is in an incorrect
     * date format rather than dd/MM/yyyy or if the date is invalid
     * 
     * @param usrDate
     * @return Date
     */
    public static Date convStandardDate(String usrDate) {        
        Date date = null; 
        if ((usrDate != null) && (!usrDate.equals(""))) {
            try {                
                date = new SimpleDateFormat(StdFun.STANDARD_DATE_FORMAT).parse(usrDate);
                
                String newDateStr = new SimpleDateFormat(StdFun.STANDARD_DATE_FORMAT).format(date);
                if (!newDateStr.equals(usrDate)) {
                    date = null;
                } // set date to null if not valid                                       
            } catch (ParseException ex) {                
                //Logger.getLogger(StdFun.class.getName()).log(Level.SEVERE, null, ex);
            }           
        }
        
        return date;
    }

    /**
     * Checks date entered is valid or not by converting the given String
     * to a <code>Date</code> object and format it into standard date dd/MM/yyyy 
     * and back to <code>String</code>. If the 2 Strings are equal then the date is valid
     * <br/>This problem arose because when converting a date with extra chars 
     * after the year digits the <code>convStandardDate(String usrDate)</code> 
     * method above was not giving any exceptions or errors which was misleading
     * 
     * @param userDate
     * @return valid or not
     */
    public static boolean checkStandardDate(String userDate) {
        boolean validDate = false;

        if ((StdFun.convStandardDate(userDate) != null) & (StdFun.STANDARD_DATE_FORMAT != null)) { // ?? sometimes it gave a nullpinter excpetion here !!
            String newDateStr = new SimpleDateFormat(StdFun.STANDARD_DATE_FORMAT).
                    format(StdFun.convStandardDate(userDate));

            if (newDateStr.equals(userDate)) {
                validDate = true;
            }
        }
        return validDate;
    }
    
    /**
     * Returns todays date 
     * 
     * @return today's date
     */
    public static Date getToday() {
        return new Date();
    }

    
    /**
     * Gat day of the week for a particular date in user format (dd/MM/yyyy)
     * 
     * @param standardDate (dd/MM/yyyy)
     * 
     * @return int day of the week
     * 
     */
    public static int getDayOfTheWeek(String standardDate) {
        Calendar date = Calendar.getInstance();        
        date.setTime(convStandardDate(standardDate));
        
        return date.get(Calendar.DAY_OF_WEEK);
    }
    
    /**
     * Get the name of a Calendar day of the week
     * @param calDay Calendar weekDay
     * @return dayOfTheWeek name
     */
    public static String getDayOfTheWeekName(int calDay) {
        String dayOfTheWeek = null;
        
        switch (calDay) {
            case Calendar.MONDAY:
                dayOfTheWeek = "Monday";
                break;
            case Calendar.TUESDAY:
                dayOfTheWeek = "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                dayOfTheWeek = "Wednesday";
                break;
            case Calendar.THURSDAY:
                dayOfTheWeek = "Thursday";
                break;
            case Calendar.FRIDAY:
                dayOfTheWeek = "Friday";
                break;
            case Calendar.SATURDAY:
                dayOfTheWeek = "Saturday";
                break;
            case Calendar.SUNDAY:
                dayOfTheWeek = "Sunday";
                break;                   
        }
        return dayOfTheWeek;
    }

    /**
     * Get month as int from given Date
     * 
     * @param dt
     * @return int
     */
    public static int getMonth(Date dt) {
        Calendar date = Calendar.getInstance();
        date.setTime(dt);
        
        //January starts from 0 for Java
        return (date.get(Calendar.MONTH) + 1);

    }

    /**
     * Get year as int from given Date
     *
     * @param dt
     * @return int
     */
    public static int getYear(Date dt) {
        Calendar date = Calendar.getInstance();
        date.setTime(dt);

        return date.get(Calendar.YEAR);
    }

    /**
     *
     * Convert text in a given <code>JTextField</code> to a date String format dd/MM/yyyy
     * if entered in a range of shortcuts. <br/>For example:<br/>
     * <table border = "1">
     * <tr><td><b>Text</b></td><td><b>Converts</b></td></tr>
     * <tr><td>010170</td><td>01/01/1970</td></tr>
     * <tr><td>010101</td><td>01/01/2001</td></tr>
     * <tr><td>01012001</td><td>01/01/2001</td></tr>
     * <tr><td>01.01.70</td><td>01/01/1970</td></tr>
     * <tr><td>01.01.01</td><td>01/01/2001</td></tr>
     * <tr><td>01.01.2001</td><td>01/01/2001</td></tr>
     * <tr><td>01 01 01</td><td>01/01/2001</td></tr>
     * <tr><td>01 01 2001</td><td>01/01/2001</td></tr>     
     * <tr><td>01/01/01</td><td>01/01/2001</td></tr>
     * <tr><td>0101</td><td>01/01/currentYear</td></tr>
     * <tr><td>01/01</td><td>01/01/currentYear</td></tr>
     * <tr><td>01.1.15</td><td>01/01/2015</td></tr>
     * <tr><td>01/1/15</td><td>01/01/2015</td></tr>
     * <tr><td>01 1 15</td><td>01/01/2015</td></tr>
     * <tr><td>1.01.15</td><td>01/01/2015</td></tr>
     * <tr><td>1/01/15</td><td>01/01/2015</td></tr>
     * <tr><td>1 01 15</td><td>01/01/2015</td></tr>
     * <tr><td>1.1.15</td><td>01/01/2015</td></tr>
     * <tr><td>1/1/15</td><td>01/01/2015</td></tr>
     * <tr><td>1 1 15</td><td>01/01/2015</td></tr>
     * </table>
     *
     *
     * @see #easyDate(java.lang.String) 
     * @param jtf JTextField
     * @param showNow true if called from a <code>JTable</code> and false if called from a <code>JTextField</code>
     */
    public static void easyDate (final JTextField jtf, boolean showNow) {
        final String tmpStr = easyDate(jtf.getText());
        
        if (showNow) {
            jtf.setText(tmpStr);
        } else {
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    jtf.setText(tmpStr);
                }
            });
        }
    }

    /**
     *
     * Convert text in a given <code>String</code> to a date String format dd/MM/yyyy
     * if entered in a range of shortcuts. <br/>For example:<br/>
     * <table border = "1">
     * <tr><td><b>Text</b></td><td><b>Converts</b></td></tr>
     * <tr><td>010170</td><td>01/01/1970</td></tr>
     * <tr><td>010101</td><td>01/01/2001</td></tr>
     * <tr><td>01012001</td><td>01/01/2001</td></tr>
     * <tr><td>01.01.70</td><td>01/01/1970</td></tr>
     * <tr><td>01.01.01</td><td>01/01/2001</td></tr>
     * <tr><td>01.01.2001</td><td>01/01/2001</td></tr>
     * <tr><td>01 01 01</td><td>01/01/2001</td></tr>
     * <tr><td>01 01 2001</td><td>01/01/2001</td></tr>     
     * <tr><td>01/01/01</td><td>01/01/2001</td></tr>
     * <tr><td>0101</td><td>01/01/currentYear</td></tr>
     * <tr><td>01/01</td><td>01/01/currentYear</td></tr>
     * <tr><td>01.1.15</td><td>01/01/2015</td></tr>
     * <tr><td>01/1/15</td><td>01/01/2015</td></tr>
     * <tr><td>01 1 15</td><td>01/01/2015</td></tr>
     * <tr><td>1.01.15</td><td>01/01/2015</td></tr>
     * <tr><td>1/01/15</td><td>01/01/2015</td></tr>
     * <tr><td>1 01 15</td><td>01/01/2015</td></tr>
     * <tr><td>1.1.15</td><td>01/01/2015</td></tr>
     * <tr><td>1/1/15</td><td>01/01/2015</td></tr>
     * <tr><td>1 1 15</td><td>01/01/2015</td></tr>
     * 
     * </table>
     * @param dateStr
     * @return String
     */
    public static String easyDate (String dateStr) {
        Date date = null;
        int tmpNum = 0;

        try {
            Calendar calDate = Calendar.getInstance();
            calDate.setTime(new Date());

            if (dateStr.length() == 4) {
                tmpNum = Integer.parseInt(dateStr);
                dateStr = dateStr.concat(calDate.get(Calendar.YEAR) + "");
            }
            if (dateStr.length() == 5) {
                if (dateStr.charAt(2) == '/') {
                    dateStr = (dateStr.concat("/").concat(calDate.get(Calendar.YEAR) + ""));
                } 
            }
            if (dateStr.length() == 6) {
                if ((dateStr.charAt(1) == '.') & (dateStr.charAt(3) == '.')) {
                    date = new SimpleDateFormat("dd.MM.yy").parse(dateStr);
                } 
                else if ((dateStr.charAt(1) == '/') & (dateStr.charAt(3) == '/')) {
                    date = new SimpleDateFormat("dd/MM/yy").parse(dateStr);
                } 
                else if ((dateStr.charAt(1) == ' ') & (dateStr.charAt(3) == ' ')) {
                    date = new SimpleDateFormat("dd MM yy").parse(dateStr);
                }
                else {
                    tmpNum = Integer.parseInt(dateStr.substring(dateStr.length() -2)); // check if digits entered are all numbers
                    if (tmpNum > 69) {
                        dateStr = dateStr.substring(0,dateStr.length() -2) + "19" + dateStr.substring(dateStr.length() -2);
                    } else {
                        dateStr = dateStr.substring(0,dateStr.length() -2) + "20" + dateStr.substring(dateStr.length() -2);
                    }
                }
            }
            if (dateStr.length() == 7) {
                if ((dateStr.charAt(2) == '.') & (dateStr.charAt(4) == '.')) {
                    date = new SimpleDateFormat("dd.MM.yy").parse(dateStr);
                } else if ((dateStr.charAt(2) == '/') & (dateStr.charAt(4) == '/')) {
                    date = new SimpleDateFormat("dd/MM/yy").parse(dateStr);
                } else if ((dateStr.charAt(2) == ' ') & (dateStr.charAt(4) == ' ')) {                    
                    date = new SimpleDateFormat("dd MM yy").parse(dateStr);
                } else if ((dateStr.charAt(1) == '.') & (dateStr.charAt(4) == '.')) {
                    date = new SimpleDateFormat("dd.MM.yy").parse(dateStr);
                } else if ((dateStr.charAt(1) == '/') & (dateStr.charAt(4) == '/')) {
                    date = new SimpleDateFormat("dd/MM/yy").parse(dateStr);
                } else if ((dateStr.charAt(1) == ' ') & (dateStr.charAt(4) == ' ')) {
                    date = new SimpleDateFormat("dd MM yy").parse(dateStr);
                }
            }
            if (dateStr.length() == 8) {
                if ((dateStr.charAt(2) == '.') & (dateStr.charAt(5) == '.')) {
                    date = new SimpleDateFormat("dd.MM.yy").parse(dateStr);
                }
                else if((dateStr.charAt(2) == '/') & (dateStr.charAt(5) == '/')) {
                    date = new SimpleDateFormat("dd/MM/yy").parse(dateStr);
                }
                else if((dateStr.charAt(2) == ' ') & (dateStr.charAt(5) == ' ')) {
                    date = new SimpleDateFormat("dd MM yy").parse(dateStr);
                }
                else {
                    date = new SimpleDateFormat("ddMMyyyy").parse(dateStr);
                }
            }
            if (dateStr.length() == 10) { //If date entered is in format "01.01.2011'
                if ((dateStr.charAt(2) == '.') & (dateStr.charAt(5) == '.')) {
                    date = new SimpleDateFormat("dd.MM.yyyy").parse(dateStr);
                }
                if ((dateStr.charAt(2) == '/') & (dateStr.charAt(5) == '/')) {
                    date = new SimpleDateFormat("dd/MM/yyyy").parse(dateStr);
                }
                if ((dateStr.charAt(2) == ' ') & (dateStr.charAt(5) == ' ')) {
                    date = new SimpleDateFormat("dd MM yyyy").parse(dateStr);
                }
            }

            dateStr = getStandardDate(date);
        } catch (ParseException | NumberFormatException e) {
            dateStr = "";
        }

        return dateStr;
    }
  
    /**
     * Round a number <code>numToRound</code> to the nearest <code>roundTo</code> number.
     * <br/>For example: 1824.50 rounded to the nearest 0.05 becomes 1825.00
     *
     * @param numToRound
     * @param roundTo
     * @return roundedNum
     */
    public static float roundNumber(float numToRound, float roundTo) {
        float roundedNum;

        if (roundTo > 0) {
            boolean validRange = false, decimalRounding = false;
            int exponantial = 1;
            float multFactor = 1f;

            if (roundTo < 1) {
                decimalRounding = true;
            }

            if (decimalRounding) {
                numToRound = numToRound * 100f;
                roundTo = roundTo * 100f;
            }


            while (!validRange) {
                multFactor = (float)Math.pow(10, exponantial);
                if (roundTo < multFactor) {
                    break;
                }

                exponantial++;
            }

            float portionToRound = roundVal((numToRound % multFactor),2);
            float toAddOrDeduct;

            if ((portionToRound % roundTo) >= (roundTo / 2f)) {
                toAddOrDeduct = (roundTo - (portionToRound % roundTo));
            } else {
                toAddOrDeduct = ((portionToRound % roundTo) * -1f);
            }

            roundedNum = (numToRound + toAddOrDeduct);

            if (decimalRounding) {
                roundedNum = roundedNum / 100f;
            }
        } else { // if roundTo = 0, leave number as it is do not return 0 or some exception
            roundedNum = numToRound;
        }

        return roundedNum;
    }

    /**
     * Return a float rounded number as per required decimal places
     *
     * @param value - float to be rounded
     * @param decPlaces - <code>int</code> precision
     *
     * @return roundedVal
     */
    public static float roundVal(float value, int decPlaces) {
        float roundedVal = Math.round((float)(value * Math.pow(10,decPlaces)));
        roundedVal = (float)(roundedVal / Math.pow(10,decPlaces));

        return roundedVal;
    }

    /**
     * Get a float value from a given <code>String</code> in the form of
     * <code>DECIMAL_FORMAT</code> as initialised above.<br/>
     * <b>This method returns 0f if the value String cannot be converted into
     * a float value</b>
     *
     * @param value String to convert to float
     * @return floatVal - float
     */
    public static float getFloatValue(String value) {
        Float floatVal;
        try {
            if (value != null) {
                floatVal = (new Float(value.replaceAll(",","")));
            }
            else {
                floatVal = 0f;
            }
        }
        catch (NumberFormatException e) {
            floatVal = 0f;
        }
        return floatVal;
    }

    /**
     * Get an int value from a given <code>String</code> in the form of
     * <code>INT_FORMAT</code> as initialised above.<br/>
     * <b>This method returns 0 if the value String cannot be converted into
     * a float value</b>
     *
     * @param value String to convert to float
     * @return floatVal - float
     */
    public static int getIntValue(Object value) {
        Integer intVal = 0;
 
        if (value != null) {
            if (value instanceof String) {
                String strVal = (String)value;
                try {
                    intVal = (new Integer(strVal.replaceAll(",","")));
                }
                catch (NumberFormatException e) {
                    intVal = 0;
                }
            }
            if (value instanceof Integer) {
                intVal = (Integer)value;
            }
        } 

        return intVal;
    }

    
    public static void switchContainer(Container containerToSwitch,String onOff) {
        Component[] componentsInContainer = containerToSwitch.getComponents();
        for (Component componentsInContainer1 : componentsInContainer) {
            componentsInContainer1.setEnabled(onOff.equals("on"));
            //To check wheter component is a Container and also switch its contents recursively
            Container secondTier = (Container) componentsInContainer1;
            Component[] secondTier1 = secondTier.getComponents();
            if (secondTier1.length > 0) switchContainer(secondTier,onOff);
        }
    }



    /**
     * Creates a zero padded String representation for a given int. <br/>
     * For example. <code>123</code> becomes <code>00123</code> if the following
     * command is executed: <p><code>
     * Stdfun.charFill(123,5,"0");
     * </code></p>
     * 
     * @param toCharFill number to format with leading <code>charToFill</code>
     * @param strLength total length of formatted String including padded characters
     * @param charToFill fill with what ? - example: "0"
     * @return String
     */
    public static String charFill(int toCharFill, int strLength, String charToFill) {

        StringBuilder formattedStr = new StringBuilder();

        String strToFormat = toCharFill + "";

        if (strLength > strToFormat.length()) {
            for (int i = 0; i < (strLength - strToFormat.length()); i ++) {
                formattedStr.append(charToFill);
            }
        }
        formattedStr.append(toCharFill);

        return formattedStr.toString();
    }
    public static String charFill(String toCharFill, int strLength, String charToFill) {

        StringBuilder formattedStr = new StringBuilder();

        String strToFormat = toCharFill;

        if (strLength > strToFormat.length()) {
            for (int i = 0; i < (strLength - strToFormat.length()); i ++) {
                formattedStr.append(charToFill);
            }
        }
        formattedStr.append(toCharFill);

        return formattedStr.toString();
    }

    public static boolean osIsWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }
    
    public static boolean osIsLinux() {
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }

    public static void executeOSCommand(String commandStr) throws Exception {
        String os = System.getProperty("os.name").toLowerCase();
        if (osIsWindows()) {
            String cmd;
            if ( (os.contains("98")) || (os.contains("95")) ) {
                cmd = "command.com /c start ";
            }
            else {
                cmd = "cmd.exe /c start ";
            }
            cmd = cmd + commandStr;
            Runtime.getRuntime().exec(cmd);
        }
    }
    
    /**
     * Copies any file (binary or otherwise) from one place to another
     * 
     * @param sourceFilename  - Copy file from here
     * @param destinationFilename  - To here
     * @throws java.lang.Exception
     * 
     */
    public static void copyFile(String sourceFilename, String destinationFilename) throws Exception {
        java.io.File localFile = new java.io.File(sourceFilename);
        FileInputStream in = new FileInputStream(localFile);

        FileOutputStream outStream;
        BufferedOutputStream bufout;
        try (BufferedInputStream bufin = new BufferedInputStream(in)) {
            outStream = new FileOutputStream(destinationFilename);
            bufout = new BufferedOutputStream(outStream);
            byte[] buffer = new byte[3] ;
            while ( true ) {
                int bytes_read = bufin.read(buffer) ;
                if (bytes_read == -1) break ;
                if (buffer[0] == 'x' && buffer[1] == ' ' && buffer[2] == 'M') {
                    buffer[0]='1';
                    buffer[1]='1';
                    buffer[2]='1';
                    bufout.write(buffer);
                }
                bufout.write(buffer);
            }
        }

        bufout.close();
        outStream.close();
    }

    /**
     * Writes an array of bytes to a sequential text file
     * 
     * @param fileName  - The text file filename
     * @param byteArray - The array of bytes
     * @param replace   - Replace file (true/false)
     * @throws java.lang.Exception
     * 
     */
    public static void writeFile(String fileName, byte[] byteArray, boolean replace) throws Exception {
        java.io.File localFile = new java.io.File(fileName);
            if (!localFile.exists() || replace) {
                BufferedOutputStream bufout;
            try (FileOutputStream outStream = new FileOutputStream(fileName)) {
                bufout = new BufferedOutputStream(outStream);
                bufout.write(byteArray);
                bufout.close();
            }
            //bufout=null;
        }
    }

    /**
     * Reads a sequential text file into a String
     * 
     * @param fileName  - The text file filename
     * 
     * @return String containing file content
     * @throws java.lang.Exception
     */
    public static String readFile(String fileName) throws Exception {
        StringBuilder contentBuffer=new StringBuilder();
        java.io.BufferedReader br;
        br = new java.io.BufferedReader(new java.io.FileReader(fileName));
        String line="";
        while (line!=null) {
            line = br.readLine();
            if (line!=null) {
                contentBuffer.append(line).append("\n");
            }
        }
        return(contentBuffer.toString());
    }

    public static String replaceAll(String replaceStr, String replaceWhat, String replaceWith) {
        //This was done to replace String.replaceAll since regex control characters like $ { and / have to be preceded by \\
        StringBuilder replaceBuf=new StringBuilder();
        int strLength=replaceStr.length();
        int whatLength=replaceWhat.length();
//        int withLength=replaceWith.length();
        for (int charPos=0; charPos<strLength; charPos++) {
            if ((charPos+whatLength) <= strLength) {
                if (replaceStr.substring(charPos, charPos+whatLength).equals(replaceWhat)) {
                    replaceBuf.append(replaceWith);
                    charPos=charPos+whatLength-1;
                } else {
                    replaceBuf.append(replaceStr.charAt(charPos));
                }
            }
            else {
                replaceBuf.append(replaceStr.charAt(charPos));
            }
        }
        return(replaceBuf.toString());
    }

    /**
     * Format a String with added spaces to the right enough to be of size as defined
     * in the parameter
     *
     * @param str String to format
     * @param size Total size of formatted String
     * @return paddedStr
     */
    public static String pad(String str, int size) {

        String paddedStr = str;

        if (str.length() < size) {
            for (int i = str.length(); i < size; i++) {
                paddedStr += " ";
            }
        }

        return paddedStr;

    }

    /**
     * This method removes any leading 0's from a String - useful to trim a
     * bank account number for example from 00123456 to 123456
     * 
     * @param str
     * @return String without leading 0's
     */
    public static String removeLeadingZeros(String str) {
        String formattedStr = str;

        if ((str != null) && (!str.equals(""))) {
            while (formattedStr.substring(0, 1).equals("0")) {
                formattedStr = formattedStr.substring(1);
            }
        }
        return formattedStr.trim();
    }

    public static void chooseDate(JTextField jtf, Container parent) {

        if (jtf.isEditable()) {

            final JTextField dateTextField = jtf;
            final PopupWindow popup = new PopupWindow(parent);

            CalendarPanel mCalendarPanel = new CalendarPanel((Date date) -> {
                popup.hide();
                String dateChosen = new SimpleDateFormat(STANDARD_DATE_FORMAT).format(date);
                if (!dateChosen.equals("")) {
                    dateTextField.setText(dateChosen);
                }
            });
            popup.add(mCalendarPanel);
            popup.show(dateTextField, 0, 0);
        }
    }

    
    /**
     * Get the number of days between a date from and a date to. The number of days
     * include both the date from and the date to. For example if the dateFrom is
     * 01/01/2008 and dateTo is 02/01/2008 the daysDiff is 2 and not 1.
     * 
     * @param from Date from dd/MM/yyyy
     * @param to Date to dd/MM/yyyy
     * @return daysDiff - int
     */
    public static int getDaysDiff(String from, String to) {
               
        Calendar dateFrom = Calendar.getInstance();
        dateFrom.setTime(convStandardDate(from));
        
        Calendar dateTo = Calendar.getInstance();
        dateTo.setTime(convStandardDate(to));


        return subtractDatesAsDays(dateFrom, dateTo);

    }

    /**
     * Get the number of days between a <code>Calendar</code> dateFrom and a <code>Calendar</code> dateTo. 
     * The number of days include both the date from and the date to. For example if the dateFrom is
     * 01/01/2008 and dateTo is 02/01/2008 the daysDiff is 2 and not 1.
     * 
     * @param dateFrom
     * @param dateTo
     * @return
     */
    private static int subtractDatesAsDays(Calendar dateFrom, Calendar dateTo) {
        int daysDiff = 0;

        dateTo.add(Calendar.DATE,1);
        do {
            daysDiff++;
            dateFrom.add(Calendar.DATE, 1);

        } while (dateFrom.before(dateTo)) ;

        return daysDiff;
    }
    
    /**
     * Add a number of days to a specific date String and return a Date 
     * object with the result. One can either add or deduct days by providing
     * a -ve days amount in the appropriate parameter to deduct and +ve to add.
     * This method returns <code>null</code> if the parameter dt passed is not in a valid
     * format which has to be dd/MM/yyyy
     * 
     * @param dt String representation of initial date (dd/MM/yyyy)
     * @param days to deduct if -ve or add if +ve
     * 
     * @return setDate - a Date object with final result
     */
    public static Date addDays(String dt, int days) {
        Date setDate = null;
        
        Calendar date = Calendar.getInstance();        
        
        if (convStandardDate(dt) != null) {
            date.setTime(convStandardDate(dt));
            date.add(Calendar.DATE, days);
            
            setDate = date.getTime();
        }

        return setDate;
    }

    /**
     * Play a beep sound
     */
    public static synchronized void playBeep() {
        new Thread(new Runnable() { // the wrapper thread is unnecessary, unless it blocks on the Clip finishing, see comments
          @Override
          public void run() {
            try {
              Clip clip = AudioSystem.getClip();
              AudioInputStream inputStream = AudioSystem.getAudioInputStream(getClass().getResource(RES_PATH + "beep.wav"));
              clip.open(inputStream);
              clip.start(); 
            } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
              // no big deal sound not played !!
            }
          }
        }).start();
    } 

    /**
     * Get an ActionListener with displaying busy cursor while processing
     * 
     * @param component
     * @param mainActionListener
     * @return 
     */
    public static ActionListener createListener(final Component component, final ActionListener mainActionListener) {
        ActionListener actionListener = new ActionListener() {
            
            final Cursor busyCursor = new Cursor(Cursor.WAIT_CURSOR);
            final Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);

            @Override
            public void actionPerformed(final ActionEvent ae) {
                
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        component.setCursor(busyCursor);
                    }
                };
                Timer timer = new Timer(); 
                
                try {   
                    timer.schedule(timerTask, 300);
                    mainActionListener.actionPerformed(ae);
                } finally {
                    timer.cancel();
                    component.setCursor(defaultCursor);
                }
            }
        };
        return actionListener;
    }


    /**
     * Get an <code>ArrayList</code> of <code>File</code> objects with folder contents together with if any files in its sub folders
     * 
     * @param folder
     * @param fileList to store <code>Fille</code> objects
     * @return <code>ArrayList</code> of <code>File</code> objects
     * @throws IOException 
     */
    public static ArrayList<File> fillDirectoryContents(File folder, ArrayList<File> fileList) throws IOException {
                
        File[] folderContents = folder.listFiles();
        for (File file : folderContents) {
            if (!file.isDirectory()) {                    
                fileList.add(new File(file.getCanonicalPath()));
            } else {
                fillDirectoryContents(file, fileList);
            }
        }
        
        return fileList;
    }

    public static char osFs() {
        return(System.getProperty("file.separator").charAt(0));
    }
}