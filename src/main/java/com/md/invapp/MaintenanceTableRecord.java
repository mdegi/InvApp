/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md.invapp;

/**
 *
 * @author user
 */
public class MaintenanceTableRecord {

    private int id;
    
    private String dsc;
        
    public final static int NEW_RECORD = 0;
    
    public final static int CANCELLED_RECORD = -1;
            
    protected void initVars() {
        
        setId(CANCELLED_RECORD);
        setDsc(null);
    }
    
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the dsc
     */
    public String getDsc() {
        return dsc;
    }

    /**
     * @param dsc the dsc to set
     */
    public void setDsc(String dsc) {
        this.dsc = dsc;
    }

    
}
