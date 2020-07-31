/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package stdClasses;

import java.awt.Color;

import javax.swing.JTextField;
import javax.swing.BorderFactory;

/**
 *
 * @author Martin Degiorgio <www.nlasystems.com>
 */
public class StatusBar extends JTextField {

    public StatusBar() {
        super();        
        initComponenet();
        
    }
    
    public StatusBar(String msg){        
        super (msg);                
        initComponenet();
        
    }
    
    private void initComponenet() {
        
        setEditable(false);
        setFocusable(false);
        
        setBorder(BorderFactory.createLoweredBevelBorder()); // create a lowered textfield effect
        
        setForeground(Color.BLACK);
        setBackground(Color.WHITE);
    }

    /**
     * Keep this JTextField not editable - user shoud not be allowed
     * to edit any text in here
     */
    @Override
    public void setEditable(boolean editable) {
        super.setEditable(false);
    }
 
    
    @Override
    public void setFocusable(boolean focusable) {
        super.setFocusable(false);
    }
    
}
