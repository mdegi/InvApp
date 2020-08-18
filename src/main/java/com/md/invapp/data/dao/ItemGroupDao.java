/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md.invapp.data.dao;

import com.md.invapp.MaintenanceTableRecord;
import com.md.invapp.data.entities.ItemGroupEntity;
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
public class ItemGroupDao implements MaintenanceTableDao {
    private final SessionFactory SESSION_FACTORY;    
    
    public ItemGroupDao(SessionFactory sessionFactory) {
        this.SESSION_FACTORY = sessionFactory;
    }
    
    public ItemGroupEntity getGroup(String groupCode) {
        ItemGroupEntity groupEntity;
        try (Session dbSession = SESSION_FACTORY.openSession()) {
            Criteria criteria = dbSession.createCriteria(ItemGroupEntity.class);
            criteria.add(Restrictions.eq("description", groupCode));
            groupEntity = (ItemGroupEntity)criteria.uniqueResult();
        }
        return groupEntity;        
    }

    public ItemGroupEntity getGroup(int id) {
        ItemGroupEntity groupEntity;
        try (Session dbSession = SESSION_FACTORY.openSession()) {
            groupEntity = findByIDCriteria(dbSession, id);
        }
        return groupEntity;        
    }

    private ItemGroupEntity findByIDCriteria(Session dbSession, int id) {
            Criteria criteria = dbSession.createCriteria(ItemGroupEntity.class);
            criteria.add(Restrictions.eq("id", id));
            ItemGroupEntity groupEntity = (ItemGroupEntity)criteria.uniqueResult();            
            return groupEntity;
    }

    @Override
    public void saveRecord(MaintenanceTableRecord groupRecord) {
        try (Session dbSession = SESSION_FACTORY.openSession()) {
            dbSession.beginTransaction();
            dbSession.save((ItemGroupEntity)groupRecord);
            dbSession.getTransaction().commit();
        }
    }

    @Override
    public List<MaintenanceTableRecord> getAllRecords() {
        List groups;
        try (Session dbSession = SESSION_FACTORY.openSession()) {
            Criteria criteria = dbSession.createCriteria(ItemGroupEntity.class);
            criteria.addOrder(Order.asc("description"));
            groups = criteria.list();
        }
        return groups;
    }
    
    @Override
    public void updateRecord(MaintenanceTableRecord groupEntity) {
        try (Session dbSession = SESSION_FACTORY.openSession()) {
            dbSession.beginTransaction();
            ItemGroupEntity existingEntity = findByIDCriteria(dbSession, groupEntity.getId());
            existingEntity.setDescription(groupEntity.getDescription());
            dbSession.update(existingEntity);
            dbSession.getTransaction().commit();
        }
    }

    @Override
    public void deleteRecord(int id) {
        try (Session dbSession = SESSION_FACTORY.openSession()) {
            dbSession.beginTransaction();
            ItemGroupEntity existingEntity = findByIDCriteria(dbSession, id);
            dbSession.delete(existingEntity);
            dbSession.getTransaction().commit();
        }        
    }    
}
