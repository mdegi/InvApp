/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md.invapp;

import com.md.invapp.data.entities.InvAppUserEntity;
import com.md.invapp.enums.PropsFields;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;
import stdClasses.StdFun;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author user
 */
@Getter
@Setter
public class RuntimeArgs {

    public static final String LABEL = "runtimeArgs";
    
    private String backupDrive;
        
    private InvAppConfig invAppConfig;
    private InvAppUserEntity userRec;
    private InvAppDBConn invAppDbConn;
                    
    private int openFrameCount;
    
    private static RuntimeArgs runtimeArgs;
    
    private RuntimeArgs() {  }

    public static synchronized RuntimeArgs getRunTimeArgsInstance() {
        if (runtimeArgs == null) {
            runtimeArgs = new RuntimeArgs();
        } 
        return runtimeArgs;
    }

    public Connection getDbConn() {
        return invAppDbConn.getConnection();
    }
    
    public void incrementOpenFrameCount() {
        openFrameCount++;
    }
    
    public void decrementOpenFrameCount() {
        openFrameCount--;    
    }        

    public void resetOpenFrameCount() {
        openFrameCount = 0;
    }

    public void saveOutpuPath(String path) throws IOException{
        try (FileOutputStream out = new FileOutputStream(StdFun.PROPS_FILE_NAME)) {
            Properties properties = new Properties();
            properties.setProperty(PropsFields.REPORTS_PATH.name(), path);
            properties.store(out, null);
        }                
    }

}

