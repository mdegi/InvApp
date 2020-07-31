/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md.invapp;

import com.md.invapp.enums.PropsFields;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;
import stdClasses.StdFun;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author user
 */
@Getter
@Setter
@NoArgsConstructor
public class RuntimeArgs {

    public static final String LABEL = "runtimeArgs";
    
    private String backupDrive;
        
    private InvAppConfig invAppConfig;
    private InvAppUserRec userRec;
    private InvAppDBConn invAppDbConn;
                    
    private int openFrameCount;
    
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

