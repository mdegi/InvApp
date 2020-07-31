/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md.invapp;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author user
 */
@Getter
@Setter
public class InvAppUserRec {
    
    private int id;
    
    private String userCode, userPass, userGrp;

    private boolean readOnly;
    
    public final static int NEW_RECORD = 0;
    
    public final static int CANCELLED_RECORD = -1;
            
    public void initVars() {        
        setId(CANCELLED_RECORD);
        setUserCode(null);
        setUserPass(null);
        setUserGrp(null);
    }
        
}
