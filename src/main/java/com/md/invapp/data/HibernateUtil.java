/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md.invapp.data;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
/**
 *
 * @author Martin.Degiorgio
 */
public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();
      
    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            URL res = HibernateUtil.class.getClassLoader().getResource("conf/hibernate.cfg.xml");
            SessionFactory session = new Configuration().configure(Paths.get(res.toURI()).toFile()).buildSessionFactory();
            return session;
        }
        catch (HibernateException ex) {
            throw new ExceptionInInitializerError(ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(HibernateUtil.class.getName()).log(Level.SEVERE, null, ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
  
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
      
    public static void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }   
}
