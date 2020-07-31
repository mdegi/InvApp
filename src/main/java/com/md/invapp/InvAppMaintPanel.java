/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md.invapp;

import javax.swing.JPanel;
import javax.swing.JTextField;
import stdClasses.StatusBar;
import stdClasses.StdFun;

/**
 *
 * @author user
 */
public abstract class InvAppMaintPanel extends JPanel{
    
    private StatusBar statusBar;

    public abstract void fillRecord();
    
    public abstract void clearForm();
    
    public abstract void setEditable(boolean editable);
    
    public void setEnableSearchList(boolean mode) { 
        // override this method to implement
    };    

    /**
     * Show a message in the status bar
     * 
     * @param message - String
     */
    public void showMessage(String message) {
        if (statusBar != null) {
            statusBar.setText(message);
        }
    }
    
    /**
     * Set the status bar of this JPanel 
     * 
     * @param statusBar - StatusBar
     */
    public void setStatusBar(StatusBar statusBar){
        this.statusBar = statusBar;
    }
    
    /**
     * Returns the current status bar used in thisJPanel
     * 
     * @return StatusBar
     */
    public StatusBar getStatusBar () {
        return statusBar;
    }

    
    public void initColorIfApplicable(InvAppScanListener scanListener,
            JTextField jTextField1, JTextField jTextField2) {

        if (scanListener != null) {

            if (jTextField1.isEditable()) {
                jTextField1.setBackground(StdFun.LIGHT_YELLOW);
            } else {
                jTextField1.setBackground(StdFun.LIGHT_GREY);
            }

            if (jTextField2.isEditable()) {
                jTextField2.setBackground(StdFun.LIGHT_YELLOW);
            } else {
                jTextField2.setBackground(StdFun.LIGHT_GREY);
            }
        }

    }

    public void setColorIfApplicable(InvAppScanListener scanListener,
            JTextField jTextField1, JTextField jTextField2, boolean editRec) {

        if (scanListener != null) {
            if (editRec) {
                jTextField1.setBackground(StdFun.LIGHT_YELLOW);
                jTextField2.setBackground(StdFun.LIGHT_YELLOW);

                jTextField1.setToolTipText("Double click to scan by code / Required field");
                jTextField2.setToolTipText("Double click to scan by description");
            } else {
                jTextField1.setBackground(StdFun.LIGHT_GREY);
                jTextField2.setBackground(StdFun.LIGHT_GREY);

                jTextField1.setToolTipText(null);
                jTextField2.setToolTipText(null);
            }
        }

    }

    
}
