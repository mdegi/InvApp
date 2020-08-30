/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md.invapp.data.dao;

import com.md.invapp.MaintenanceTableRecord;
import com.md.invapp.data.entities.ItemCategoryEntity;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Martin.Degiorgio
 */
public class ItemCategoryDao implements MaintenanceTableDao {
    private final SessionFactory SESSION_FACTORY;    
    
    private final String DESCRIPTION_COLUMN = "description";
    
    public ItemCategoryDao(SessionFactory sessionFactory) {
        this.SESSION_FACTORY = sessionFactory;
    }
    
    public ItemCategoryEntity getCategory(String categoryCode) {
        ItemCategoryEntity categoryEntity;
        try (Session dbSession = SESSION_FACTORY.openSession()) {
            Criteria criteria = dbSession.createCriteria(ItemCategoryEntity.class);
            criteria.add(Restrictions.eq(DESCRIPTION_COLUMN, categoryCode));
            categoryEntity = (ItemCategoryEntity)criteria.uniqueResult();
        }
        return categoryEntity;        
    }

    public ItemCategoryEntity getCategory(int id) {
        ItemCategoryEntity categoryEntity;
        try (Session dbSession = SESSION_FACTORY.openSession()) {
            categoryEntity = findByIDCriteria(dbSession, id);
        }
        return categoryEntity;        
    }

    private ItemCategoryEntity findByIDCriteria(Session dbSession, int id) {
            Criteria criteria = dbSession.createCriteria(ItemCategoryEntity.class);
            criteria.add(Restrictions.eq("id", id));
            ItemCategoryEntity catgoryEntity = (ItemCategoryEntity)criteria.uniqueResult();            
            return catgoryEntity;
    }
    
    @Override
    public void saveRecord(MaintenanceTableRecord categoryRecord) {
        try (Session dbSession = SESSION_FACTORY.openSession()) {
            dbSession.beginTransaction();
            dbSession.save((ItemCategoryEntity)categoryRecord);
            dbSession.getTransaction().commit();
        }
    }

    @Override
    public List<MaintenanceTableRecord> getAllRecords() {        
        List categories;
        try (Session dbSession = SESSION_FACTORY.openSession()) {
            Criteria criteria = dbSession.createCriteria(ItemCategoryEntity.class);
            criteria.addOrder(Order.asc(DESCRIPTION_COLUMN));
            categories = criteria.list();
        }
        return categories;
    }


    @Override
    public void updateRecord(MaintenanceTableRecord categoryEntity) {
        try (Session dbSession = SESSION_FACTORY.openSession()) {
            dbSession.beginTransaction();
            ItemCategoryEntity existingEntity = findByIDCriteria(dbSession, categoryEntity.getId());
            existingEntity.setDescription(categoryEntity.getDescription());
            dbSession.update(existingEntity);
            dbSession.getTransaction().commit();
        }
    }

    @Override
    public void deleteRecord(int id) {
        try (Session dbSession = SESSION_FACTORY.openSession()) {
            dbSession.beginTransaction();
            ItemCategoryEntity existingEntity = findByIDCriteria(dbSession, id);
            dbSession.delete(existingEntity);
            dbSession.getTransaction().commit();
        }        
    }
    
    private Session getSession() {
        Session session;        
        session = SESSION_FACTORY.getCurrentSession();
        if (session == null) {
            session = SESSION_FACTORY.openSession();
        }        
        return session;        
    }
    
}
