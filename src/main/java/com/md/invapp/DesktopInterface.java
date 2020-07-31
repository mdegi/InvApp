/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md.invapp;

import java.awt.Dimension;
import javax.swing.JInternalFrame;

/**
 *
 * @author user
 */
public interface DesktopInterface {
    
    public void addToDesktop(JInternalFrame frame, boolean maximize);
    
    public JInternalFrame[] getAllFrames();

    public void moveToFront(JInternalFrame frame);
    
    public Dimension getDesktopDimension();
    
}
