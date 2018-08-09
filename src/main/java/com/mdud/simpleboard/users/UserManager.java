package com.mdud.simpleboard.users;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class UserManager {
    private SessionFactory sessionFactory;

    public UserManager(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Integer addUser(User user) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        Integer userId = null;

        try {
            tx = session.beginTransaction();
            userId = (Integer) session.save(user);
            tx.commit();
        } catch (HibernateException e) {
            if( tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return userId;
    }

    public void deleteUser(User user) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        try {
            tx = session.beginTransaction();
            User deleteUser = (User) session.createQuery("FROM User u WHERE " +
                    "u.userName = :username AND u.password = :password")
                    .setParameter("username", user.getUserName())
                    .setParameter("password", user.getPassword())
                    .getSingleResult();
            if(deleteUser != null) {
                session.delete(deleteUser);
            }

            tx.commit();
        } catch (HibernateException e) {
            if(tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public User getUser(User user) {
        //for login purposes
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        User loginUser = null;

        try {
            tx = session.beginTransaction();
            loginUser = (User) session.createQuery("FROM User u WHERE " +
                    "u.userName = :username AND u.password = :password")
                    .setParameter("username", user.getUserName())
                    .setParameter("password", user.getPassword())
                    .getSingleResult();
            tx.commit();
        } catch (HibernateException e) {
            if(tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return loginUser;
    }
}
