/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md.invapp;

/**
 *
 * @author user
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameAdapter;
import stdClasses.ActionToolBar;
import stdClasses.StatusBar;

public abstract class InvAppMaintFrame extends JInternalFrame 
        implements ActionListener, MouseListener {

    private ActionToolBar actionToolBar = null;
    private DesktopInterface desktopInterface = null;
    
    private StatusBar statusBar;    
    private final RuntimeArgs INV_APP_ARGS;
    private JPanel controlPanel;
    
    public InvAppMaintFrame(String title, boolean resizable, boolean closable, 
            boolean maximizable, boolean iconifiable, RuntimeArgs invAppArgs) {
        
        super(title , 
              resizable,
              closable,
              maximizable,
              iconifiable);
        
        this.INV_APP_ARGS=invAppArgs;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);                

        invAppArgs.incrementOpenFrameCount();

        actionToolBar = new ActionToolBar();
        actionToolBar.addActionListener(this);

        statusBar = new StatusBar();
 
        setLayout(new BorderLayout());
        add(actionToolBar,BorderLayout.NORTH);        
        add(statusBar,BorderLayout.SOUTH);
        if (invAppArgs.getUserRec().isReadOnly()) {
            actionToolBar.removeButton(ActionToolBar.NEW_BUTTON);
            actionToolBar.removeButton(ActionToolBar.EDIT_BUTTON);
            actionToolBar.removeButton(ActionToolBar.SAVE_BUTTON);
            actionToolBar.removeButton(ActionToolBar.DELETE_BUTTON);
        }
        
        addInternalFrameListener(new InternalFrameAdapter() {
                    @Override
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt)  {        }
            
            @Override
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }
        });        
    }
    
    public void addFramePanel(JPanel panel) {
        
        add(panel,BorderLayout.CENTER);
        pack();
    }
            

    /**
     * Get a memory reference to the ActinPanel object which holds the control
     * buttons - New / Save / Edit / Cancel
     *
     * @return actionToolBar
     */
    protected ActionToolBar getActionToolBar() {
        return actionToolBar;
    }
    
    /**
     * The controlPanel is a BorderLayut type panel containing the status bar object
     * and the ActionToolBar object holding the control buttons
     *
     * @return contolPanel
     */
    protected JPanel getControlPanel() {
        return controlPanel;
    }
    
    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
        INV_APP_ARGS.decrementOpenFrameCount();
    }

    public void setButtonEnabled(String button, boolean enabled) {
        actionToolBar.setButtonEnabled(button, enabled);
    }

    public void removeButton(String button) {
        actionToolBar.removeButton(button);
    }

    /**
    * Controls ActionEvent fired from action button at bottom of screen
    *
    * @param e ActionEvent
    */
    @Override
    public void actionPerformed(ActionEvent e) {
       
        String actionCommand = e.getActionCommand();

        if (actionCommand.equals(ActionToolBar.DELETE_BUTTON_COMMAND)) {
            delRecord();
        }
        if (actionCommand.equals(ActionToolBar.NEW_BUTTON_COMMAND))    {
            newRecord();
        }
        if (actionCommand.equals(ActionToolBar.EDIT_BUTTON_COMMAND))   {
            editRecord();
        }
        if (actionCommand.equals(ActionToolBar.SAVE_BUTTON_COMMAND))   {
            saveRecord();
        }
        if (actionCommand.equals(ActionToolBar.CANCEL_BUTTON_COMMAND)) {
            cancelRecord();
        }
    }
     
    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {}
    
    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {}
    
    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {}
    
    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {}
    
    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        if (e.getClickCount()==2) {
            editRecord();
        }
    }


    protected abstract void delRecord();

    protected abstract void newRecord() ;
    
    protected abstract void editRecord();
    
    protected abstract void cancelRecord();
    
    protected abstract void saveRecord();
    
    /**
     * Adds a DesktopInterface to this class to be available for all inheriting sub classes
     * 
     * @param desktopInterface
     */
    public void addDesktopInterface(DesktopInterface desktopInterface) {
        this.desktopInterface = desktopInterface;       
    }

    /**
     * Get desktopInterface set for this class
     *
     * @return desktopInterface
     */
        public DesktopInterface getDesktopInterface() {
        return desktopInterface;
    }
    

    /**
     * Gat all open <code>JInternalFrame</code> objects 
     * 
     * @return an array of <code>JInternalFrame</code> objects
     */
    public JInternalFrame[] getAllFrames() {
        return desktopInterface.getAllFrames();
    }
    
    /**
     * Display message onto StatusBar message board
     * 
     * @param message
     */
    public void showMessage (String message) {        
        statusBar.setText(message);
        statusBar.repaint();
    }

    /**
     * Get a memory reference to StatusBar object
     * 
     * @return StatusBar object
     */
    public StatusBar getStatusBar() {
        return statusBar;
    }
    
    
    public void setLocation(Dimension d) {
        
        int initX = 0;
        int xOffset = 30;
        int yOffset = 30;

        int posX = (xOffset * INV_APP_ARGS.getOpenFrameCount())+(initX * xOffset);
        int posY = (yOffset * INV_APP_ARGS.getOpenFrameCount());

        if (posX < 0) {
            posX = 0;
        }
        if (posY < 0) {
            posY = 0;
        }

        setLocation(posX, posY);

        //Next Frame to open restart from top if bottom reached
        if ((getSize().getHeight() + getLocation().getY()) > (d.getHeight() - yOffset) ||
            (getSize().getWidth() + getLocation().getX()) > (d.getWidth())
                ) {
            INV_APP_ARGS.resetOpenFrameCount();

            if ((xOffset + 30)  > d.getWidth()) {
                initX = 0;
            }
            else {
                initX++;
            }

        }
    }
}
