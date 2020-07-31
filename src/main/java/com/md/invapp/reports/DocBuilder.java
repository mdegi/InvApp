/*
 * BuildDocuments.java
 *
 * Created on January 10, 2005, 3:30 PM
 */

package com.md.invapp.reports;

import com.md.invapp.RuntimeArgs;
import java.awt.Image;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.print.JRPrinterAWT;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class DocBuilder {

    JasperReport jasperReport=null;
    JasperPrint jasperPrint = new JasperPrint();
    
    private boolean isLandscape=false;
    private int noOfPages=0;

    RuntimeArgs runtimeArguments=null;

    public DocBuilder(String reportFileName, RuntimeArgs runtimeArguments) throws JRException  {
        
        this.runtimeArguments = runtimeArguments;
        //InputStream reportFile=getClass().getResourceAsStream("/nlaPayroll/reports/"+reportFileName);
        InputStream reportFile=getClass().getResourceAsStream("/dealerapp/reports/"+reportFileName);
        jasperReport = (JasperReport)JRLoader.loadObject(reportFile);
        isLandscape=false;
    }
    
    public void setArguments(RuntimeArgs r) {
        runtimeArguments=r;
    }
    public boolean isLandscape() {
        return(isLandscape);
    }
    public JasperReport getJasperReport() {
        return jasperReport;
    }
    
    public void fill(HashMap<String, Object> reportParameters) throws JRException {
        Connection conn = runtimeArguments.getDbConn();
        
        jasperPrint = JasperFillManager.fillReport(jasperReport, reportParameters, 
            runtimeArguments.getDbConn());
        noOfPages=jasperPrint.getPages().size();
    }
    
    public void fill(HashMap<String, Object> reportParameters, Connection conn) throws JRException {
        jasperPrint = JasperFillManager.fillReport(jasperReport, reportParameters, conn);
        noOfPages=jasperPrint.getPages().size();
    }
    
    public void fill(HashMap<String, Object> reportParameters, JRDataSource dataSource) throws JRException {
        jasperPrint = JasperFillManager.fillReport(jasperReport, reportParameters, dataSource);
        noOfPages=jasperPrint.getPages().size();
    }
    
    public Image getReportImage(int pageNo, float zoom) throws JRException {
        Image reportImage=null;
        if (pageNo<noOfPages) {
            reportImage=JRPrinterAWT.printPageToImage(jasperPrint, pageNo, zoom);
        }
        else {
            reportImage=null;
        }      //signal end of document
        
        return(reportImage);
    }
    
    public void view() throws JRException {
        JasperViewer.viewReport(jasperPrint, false);
    }
    
    public void print(boolean showPrinterSetup) throws JRException {
        JasperPrintManager.printReport(jasperPrint, showPrinterSetup);
    }

    public void savePdfFile(String filename) throws JRException {
        JasperExportManager.exportReportToPdfFile(jasperPrint, filename);
    }

    public byte[] getPdfByteArray(HashMap<String, Object> reportParameters) throws JRException {
        //This produces pdf on the fly directly without having to fill a JasperPrint object
        byte[] pdfBytes;
        pdfBytes=JasperRunManager.runReportToPdf(jasperReport, reportParameters, 
            ((RuntimeArgs)reportParameters.get(RuntimeArgs.LABEL)).getDbConn());
        return(pdfBytes);
    }
    
    public byte[] getPdfByteArray(HashMap<String, Object> reportParameters, Connection conn) throws JRException {
        //This produces pdf on the fly directly without having to fill a JasperPrint object
        byte[] pdfBytes;
        pdfBytes=JasperRunManager.runReportToPdf(jasperReport, reportParameters, conn);
        return(pdfBytes);
    }
}