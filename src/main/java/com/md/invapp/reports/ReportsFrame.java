/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md.invapp.reports;

import com.md.invapp.RuntimeArgs;
import java.awt.Dimension;

/**
 *
 * @author user
 */
public abstract class ReportsFrame extends javax.swing.JInternalFrame {
    
    private RuntimeArgs daArgs;
    
    public ReportsFrame(RuntimeArgs daArgs) {
        
        this.daArgs = daArgs;
    }
            
    public void setLocation(Dimension d) {
        
        daArgs.incrementOpenFrameCount();
                
        int initX = 0;
        int xOffset = 30;
        int yOffset = 30;

        int posX = (xOffset * daArgs.getOpenFrameCount())+(initX * xOffset);
        int posY = (yOffset * daArgs.getOpenFrameCount());

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
            daArgs.resetOpenFrameCount();
            if ((xOffset + 30)  > d.getWidth()) {
                initX = 0;
            }
            else {
                initX++;
            }

        }
    }

}
