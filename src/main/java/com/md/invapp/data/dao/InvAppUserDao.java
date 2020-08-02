/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md.invapp.data.dao;

import com.md.invapp.data.entities.InvAppUserEntity;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Martin.Degiorgio
 */
public class InvAppUserDao {
    
    private final Session SESSION;
    
    public InvAppUserDao(Session session) {
        this.SESSION = session;
    }
    
    public InvAppUserEntity getUser(String userCode) {
        Criteria criteria = SESSION.createCriteria(InvAppUserEntity.class);
        criteria.add(Restrictions.eq("userCode", userCode));

        InvAppUserEntity userEntity = (InvAppUserEntity)criteria.uniqueResult();
        return userEntity;        
    }
    
    public void saveUser(InvAppUserEntity userRecord) {
        SESSION.beginTransaction();
        SESSION.save(userRecord);
        SESSION.getTransaction().commit();
    }
}
