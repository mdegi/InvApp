/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.md.invapp.data;

/**
 * Enum Values for USer Groups
 * 
 * @author Martin Degiorgio <www.nlasystems.com>
 */
public enum UserGroup {

    ADMIN    ("ADMIN"),
    GROUP1   ("GRP1"),
    GROUP2   ("GRP2");

    private final String code;

    private UserGroup(String code) {
        this.code = code;
    }

    public String getCode() {return code;}
    
}
