package com.mdud.simpleboard.users;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
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
            User duplicateCheck;

            try {
                duplicateCheck = (User) session.createQuery("FROM User u WHERE " +
                        "u.userName = :username")
                        .setParameter("username", user.getUserName())
                        .getSingleResult();
            } catch (NoResultException e) {
                duplicateCheck = null;
            }
            if(duplicateCheck == null)
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
        Transaction tx = null;

        try {
            User deleteUser;
            tx = session.beginTransaction();
            try {
                deleteUser = (User) session.createQuery("FROM User u WHERE " +
                        "u.userName = :username AND u.password = :password")
                        .setParameter("username", user.getUserName())
                        .setParameter("password", user.getPassword())
                        .getSingleResult();
            } catch (NoResultException e) {
                deleteUser = null;
            }
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
        Transaction tx = null;
        User loginUser = null;

        try {
            tx = session.beginTransaction();

            try {
                loginUser = (User) session.createQuery("FROM User u WHERE " +
                        "u.userName = :username AND u.password = :password")
                        .setParameter("username", user.getUserName())
                        .setParameter("password", user.getPassword())
                        .getSingleResult();
            } catch (NoResultException e) {
                loginUser = null;
            }
            tx.commit();
        } catch (HibernateException e) {
            if(tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return loginUser;
    }

    public void setUserPassword(User user, String newPassword) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        User loginUser = null;

        try {
            tx = session.beginTransaction();
            try {
                loginUser = (User) session.createQuery("FROM User u WHERE " +
                        "u.userName = :username AND u.password = :password")
                        .setParameter("username", user.getUserName())
                        .setParameter("password", user.getPassword())
                        .getSingleResult();
            } catch (NoResultException e) {
                loginUser = null;
            }
            if(loginUser != null) {
                loginUser.setPassword(newPassword);
                session.save(loginUser);
            }
            tx.commit();
        } catch (HibernateException e) {
            if(tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
