/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md.invapp;

import com.md.invapp.data.HibernateUtil;
import com.md.invapp.data.dao.ItemGroupDao;
import com.md.invapp.data.entities.ItemGroupEntity;
import java.awt.Dimension;

/**
 *
 * @author user
 */
public class ItemGroupInternalFrame extends MaintenanceTableInternalFrame {
    private ItemGroupDao itemGroupDao;
    private ItemGroupEntity itemGroupRec;
    
    public ItemGroupInternalFrame(Dimension dimension, RuntimeArgs daArgs) {        
        super("Maintain Item Groups", daArgs);
        initVars(dimension);        
    }
    
    private void initVars(Dimension dimension) {        
        itemGroupRec = new ItemGroupEntity();
        itemGroupDao = new ItemGroupDao(HibernateUtil.getSessionFactory());
        super.initVarsAndDisplay(itemGroupRec, itemGroupDao, dimension);
    }
        
}
