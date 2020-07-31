/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stdClasses;

import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

/**
 *
 * @author user
 */
public class ActionToolBar extends JToolBar {

    public static final String NEW_BUTTON = "NEW_BUTTON";
    public static final String EDIT_BUTTON = "EDIT_BUTTON";
    public static final String SAVE_BUTTON = "SAVE_BUTTON";
    public static final String DELETE_BUTTON = "DELETE_BUTTON";
    public static final String CANCEL_BUTTON = "CANCEL_BUTTON";
    
    public static final String NEW_BUTTON_COMMAND = "NEW_COMMAND";
    public static final String EDIT_BUTTON_COMMAND = "EDIT_COMMAND";
    public static final String SAVE_BUTTON_COMMAND = "SAVE_COMMAND";
    public static final String DELETE_BUTTON_COMMAND = "DELETE_COMMAND";
    public static final String CANCEL_BUTTON_COMMAND = "CANCEL_COMMAND";
    
    private JButton newButton, saveButton, editButton, deleteButton, cancelButton;
    
    public ActionToolBar() {
        
        initComponents();
        
        add(newButton);
        add(editButton);
        add(saveButton);
        add(deleteButton);
        add(cancelButton);
    }
        
    private void initComponents() {
        
        newButton = new JButton();
        
        saveButton = new JButton();
        editButton = new JButton();
        
        deleteButton = new JButton();
        cancelButton = new JButton();
        
        newButton.setIcon(new ImageIcon(getClass().getResource(StdFun.RES_PATH + "new.png")));
        newButton.setActionCommand(NEW_BUTTON_COMMAND);
        newButton.setToolTipText("New");
        newButton.setFocusable(false);
        newButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        newButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        
        saveButton.setIcon(new ImageIcon(getClass().getResource(StdFun.RES_PATH + "save.png")));
        saveButton.setActionCommand(SAVE_BUTTON_COMMAND);
        saveButton.setToolTipText("Save");
        saveButton.setFocusable(false);
        saveButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        saveButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        
        editButton.setIcon(new ImageIcon(getClass().getResource(StdFun.RES_PATH + "edit.png")));
        editButton.setActionCommand(EDIT_BUTTON_COMMAND);
        editButton.setToolTipText("Edit");
        editButton.setFocusable(false);
        editButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        editButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        
        deleteButton.setIcon(new ImageIcon(getClass().getResource(StdFun.RES_PATH + "delete.png")));
        deleteButton.setActionCommand(DELETE_BUTTON_COMMAND);
        deleteButton.setToolTipText("Delete");
        deleteButton.setFocusable(false);
        deleteButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        deleteButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        
        cancelButton.setIcon(new ImageIcon(getClass().getResource(StdFun.RES_PATH + "clear.png")));
        cancelButton.setActionCommand(CANCEL_BUTTON_COMMAND);
        cancelButton.setToolTipText("Cancel");
        cancelButton.setFocusable(false);
        cancelButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cancelButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        
        
        //add(saveButton);
        //add(editButton);
        //add(deleteButton);
        //add(cancelButton);
    }
    
    public void addActionListener(ActionListener listener) {
        newButton.addActionListener(listener);
        saveButton.addActionListener(listener);
        editButton.addActionListener(listener);
        deleteButton.addActionListener(listener);
        cancelButton.addActionListener(listener);        
    }
        
    public void removeButton(String button) {
        
        switch (button) {
            case NEW_BUTTON:
                remove(newButton);
                break;
            case EDIT_BUTTON:
                remove(editButton);
                break;
            case SAVE_BUTTON:
                remove(saveButton);
                break;
            case DELETE_BUTTON:
                remove(deleteButton);
                break;
            case CANCEL_BUTTON:
                remove(cancelButton);
                break;
            default:
                break; // do nothing
        }
    }
    
    public void setButtonEnabled(String button, boolean enabled) {
        
        switch (button) {
            case NEW_BUTTON:
                newButton.setEnabled(enabled);
                break;
            case EDIT_BUTTON:
                editButton.setEnabled(enabled);
                break;
            case SAVE_BUTTON:
                saveButton.setEnabled(enabled);
                break;
            case DELETE_BUTTON:
                deleteButton.setEnabled(enabled);
                break;
            case CANCEL_BUTTON:
                cancelButton.setEnabled(enabled);
                break;
            default:
                break; // do nothing no errors needed
        }
    }    
    
}
