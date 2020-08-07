/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md.invapp.data.dao;

import com.md.invapp.MaintenanceTableRecord;
import java.util.List;

/**
 *
 * @author Martin.Degiorgio
 */
public interface MaintenanceTableDao {

    public void saveRecord(MaintenanceTableRecord recordt);
    
    public void updateRecord(MaintenanceTableRecord tableRecord);
    
    public void deleteRecord(int id);
    
    public List<MaintenanceTableRecord> getAllRecords();

}
