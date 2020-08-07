/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md.invapp;

import com.md.invapp.data.HibernateUtil;
import com.md.invapp.data.dao.ItemCategoryDao;
import com.md.invapp.data.entities.ItemCategoryEntity;
import java.awt.Dimension;

/**
 *
 * @author user
 */
public class ItemCategoryInternalFrame extends MaintenanceTableInternalFrame {

    private ItemCategoryDao itemCategoryDao;
    private ItemCategoryEntity itemCategoryRec;
    
    public ItemCategoryInternalFrame(Dimension dimension, RuntimeArgs daArgs) {        
        super("Maintain Item Categories", daArgs);
        initVars(dimension);
    }
    
    private void initVars(Dimension dimension) {        
        itemCategoryRec = new ItemCategoryEntity();        
        itemCategoryDao = new ItemCategoryDao(HibernateUtil.getSessionFactory());        
        super.initVarsAndDisplay(itemCategoryRec, itemCategoryDao, dimension);
    }
        
}
