/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md.invapp;

import java.io.FileOutputStream;
import java.io.IOException;
import stdClasses.InvAppException;
import stdClasses.StdFun;
import stdClasses.SyncPipe;

/**
 *
 * @author user
 */
public class DBUtils {
 
    public static final String ERR_FILE_EXT = ".err";
    public static final String OUT_FILE_EXT = ".out";
    
    private RuntimeArgs runtimeArgs;

    public DBUtils (RuntimeArgs runtimeArgs) {
    
        this.runtimeArgs = runtimeArgs;
    }
    
    /**
     * Perform backup command by producing an SQL dump to output files as passed in parameter<br/>
     * Note that this method produces a backup also for the common database besides the one
     * being passed in parameter <code>outputFile</code><p>
     *
     * Remember that the user logged in must have DB permissions to perform backup as in mySQL
     * the <code>mysqldump</code> command locks tables while performing backup and by default,
     * users created do not have this privilege. <br/>
     * mySQL requires a list of privileges to be assigned to the user which syntax as follows:
     * <br/><code>
     * GRANT LOCK TABLES ON *.* TO user 'im_nla'@'%'; <br/>
     * GRANT CREATE ON *.* TO user 'im_nla'@'%'; <br/>
     * GRANT ALTER TABLES ON *.* TO user 'im_nla'@'%'; <br/>
     * </code></p>
     *
     * <p>Or preferably only user root performs restore date procedure</p>
     *
     * @param userCode User to perform backup (preferably root)
     * @param pwd User password
     * @param outputFile where the backup output is to be stored
     * @throws java.io.IOException
     */

    public synchronized int performBackup(String userCode, String pwd, String outputPath) 
            throws IOException, InterruptedException, InvAppException {

        StringBuilder backupStr = new StringBuilder("");

        int exitVal = -1;

        backupStr.append("mysqldump --skip-set-charset --skip-comments --triggers=false ");
        backupStr.append("-u ").append(userCode).append(" ");

        if ((pwd != null) && (pwd.length() > 0)) {
            backupStr.append("-p").append(pwd).append(" ");
        }
        backupStr.append("--result-file=").append(outputPath).append(StdFun.osFs()) ;

        String outFile = outputPath + StdFun.osFs() + InvAppDBConn.DB_NAME + OUT_FILE_EXT;
        String errFile = outputPath + StdFun.osFs() + InvAppDBConn.DB_NAME + ERR_FILE_EXT;

        //Backup company database and common database
        exitVal = performDBBackup(backupStr, InvAppDBConn.DB_NAME, outFile, errFile, false);
        if (exitVal != 0) {
            throw new InvAppException("Exception backing company database - Exit value: " + exitVal);
        }

        return exitVal;
    }

    private synchronized int performDBBackup(StringBuilder backupStr, String dbName, String outFile, String errFile, boolean triggersOnly)
            throws IOException, InterruptedException {

        Process process;
        int exitVal = -1;

        StringBuilder backupCommand = new StringBuilder(backupStr);

        if (!triggersOnly) {
            backupCommand.append(dbName).append(".sql");
        } else {
            backupCommand.append(dbName).append("trg.sql");
        }
        backupCommand.append(" ").append(dbName);

        FileOutputStream outputStream = new FileOutputStream(outFile);
        FileOutputStream errStream = new FileOutputStream(errFile);

        process = Runtime.getRuntime().exec(backupCommand.toString());
    
        // any error message?
        SyncPipe errorFile = new
            SyncPipe(process.getErrorStream(), "ERROR", errStream);

        // any output?
        SyncPipe outputFile = new
            SyncPipe(process.getInputStream(), "OUTPUT", outputStream);

        // kick them off
        errorFile.start();
        outputFile.start();

        // any error???
        exitVal = process.waitFor();

        outputStream.flush();
        outputStream.close();
       
        return exitVal;

    }

}
