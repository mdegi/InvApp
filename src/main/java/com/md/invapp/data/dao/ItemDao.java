/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md.invapp.data.dao;

import com.md.invapp.data.entities.ItemEntity;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

/**
 *
 * @author Martin.Degiorgio
 */
public class ItemDao {
    private final SessionFactory SESSION_FACTORY;    
    
    public ItemDao(SessionFactory sessionFactory) {
        this.SESSION_FACTORY = sessionFactory;
    }
    
    public ItemEntity getItem(String itemCode) {
        ItemEntity itemEntity;
        try (Session dbSession = SESSION_FACTORY.openSession()) {
            Criteria criteria = dbSession.createCriteria(ItemEntity.class);
            criteria.add(Restrictions.eq("itemCode", itemCode));
            itemEntity = (ItemEntity)criteria.uniqueResult();
        }
        return itemEntity;        
    }

    public ItemEntity getItem(String searchField, String searchCode) {
        ItemEntity itemEntity;
        try (Session dbSession = SESSION_FACTORY.openSession()) {
            itemEntity = findByCriteria(dbSession, searchField, searchCode);
        }
        return itemEntity;        
    }

    private ItemEntity findByCriteria(Session dbSession, String searchField, String searchCode) {
            Criteria criteria = dbSession.createCriteria(ItemEntity.class);
            criteria.add(Restrictions.eq(searchField, searchCode));
            ItemEntity gitemEntity = (ItemEntity)criteria.uniqueResult();            
            return gitemEntity;
    }

    public void saveRecord(ItemEntity itemRecord) {
        try (Session dbSession = SESSION_FACTORY.openSession()) {
            dbSession.beginTransaction();
            dbSession.save((ItemEntity)itemRecord);
            dbSession.getTransaction().commit();
        }
    }

    public List<ItemEntity> getAllRecords() {
        List items;
        try (Session dbSession = SESSION_FACTORY.openSession()) {
            Criteria criteria = dbSession.createCriteria(ItemEntity.class);
            items = criteria.list();
        }
        return items;
    }
    
    public void updateRecord(ItemEntity itemEntity) {
        try (Session dbSession = SESSION_FACTORY.openSession()) {
            dbSession.beginTransaction();
            ItemEntity existingEntity = findByCriteria(dbSession, "itemCode", itemEntity.getItemCode());
            existingEntity.setDescription(itemEntity.getDescription());
            existingEntity.setBarCode(itemEntity.getBarCode());
            existingEntity.setComments(itemEntity.getComments());
            existingEntity.setCategoryId(itemEntity.getCategoryId());
            existingEntity.setGroupId(itemEntity.getGroupId());
            existingEntity.setCostPrice(itemEntity.getCostPrice());
            existingEntity.setSellPrice_1(itemEntity.getSellPrice_1());
            existingEntity.setSellPrice_2(itemEntity.getSellPrice_2());
            existingEntity.setQtyInStock(itemEntity.getQtyInStock());                        
            dbSession.update(existingEntity);
            dbSession.getTransaction().commit();
        }
    }

    public void deleteRecord(String itemCode) {
        try (Session dbSession = SESSION_FACTORY.openSession()) {
            dbSession.beginTransaction();
            ItemEntity existingEntity = findByCriteria(dbSession, "itemCode", itemCode);
            dbSession.delete(existingEntity);
            dbSession.getTransaction().commit();
        }        
    }    

    public List<ItemEntity> getBasicItemDetailsList() throws NoSuchFieldException, SecurityException{        
        try (Session dbSession = SESSION_FACTORY.openSession()) {
            Criteria  cr = dbSession.createCriteria(ItemEntity.class);
            
            cr.setProjection(Projections.projectionList()            
                    .add(Projections.property(ItemEntity.ITEM_CODE),ItemEntity.ITEM_CODE)
                    .add(Projections.property(ItemEntity.ITEM_DESCRIPTION),ItemEntity.ITEM_DESCRIPTION))
                    .setResultTransformer(Transformers.aliasToBean(ItemEntity.class));
            
            List<ItemEntity> items =  cr.list();
            return items;
        }                
    }
    
    public ArrayList<ArrayList> getItemsDisplayList() throws NoSuchFieldException, SecurityException{
        ArrayList<ArrayList> displayList = new ArrayList<>();
        
        ArrayList<String> columnNames = new ArrayList<>();
        ArrayList<Integer> columnSizes = new ArrayList<>();
        ArrayList<ArrayList<String>> rowData = new ArrayList<>();
                
        Field itemCodeField = ItemEntity.class.getDeclaredField(ItemEntity.ITEM_CODE);
        Field itemDscField = ItemEntity.class.getDeclaredField(ItemEntity.ITEM_DESCRIPTION);

        columnNames.add("Item Code");
        columnNames.add("Item Description");

        Column itemCodeAnnotation = itemCodeField.getAnnotation(Column.class);
        Column itemDscAnnotation = itemDscField.getAnnotation(Column.class);

        columnSizes.add(itemCodeAnnotation.length());
        columnSizes.add(itemDscAnnotation.length());

        List<ItemEntity> itemsList = getBasicItemDetailsList();
        itemsList.forEach(
            i -> {
                ArrayList<String> item = new ArrayList<>();
                item.add(i.getItemCode());
                item.add(i.getDescription());
                rowData.add(item);
            }
        );            
       
        displayList.add(rowData);
        displayList.add(columnNames);
        displayList.add(columnSizes);
        
        return displayList;        
    }
}
