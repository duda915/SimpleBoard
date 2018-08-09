package com.mdud.simpleboard.datamodel;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Iterator;
import java.util.List;

public class TopicManager {
    private SessionFactory sessionFactory;

    public TopicManager(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void listTopics() {
        Session session = sessionFactory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            List topicList = session.createQuery("FROM Topic ").list();
            Iterator iterator = topicList.iterator();

            while (iterator.hasNext()) {
                Topic topic = (Topic) iterator.next();
                System.out.println(topic.getId() + "||" + topic.getTitle());
            }

            tx.commit();
        } catch (HibernateException e) {
            if(tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void listTopicPosts(int topicId) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Topic topic = (Topic) session.createQuery("FROM Topic t WHERE t.id = :id")
                    .setParameter("id", topicId)
                    .getSingleResult();
            System.out.println(topic.getTitle() + ":");
            List<Post> posts = topic.getPosts();

            for( Post p : posts) {
                System.out.println(p.getPostContent());
            }

            tx.commit();
        } catch (HibernateException e) {
            if( tx != null ) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public Integer addTopic(Topic newTopic) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        Integer topicId = null;

        try {
            tx = session.beginTransaction();
            topicId = (Integer) session.save(newTopic);
            tx.commit();
        } catch (HibernateException e) {
            if(tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return topicId;
    }

    public void addTopicPost(int topicId, Post post) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Topic topic = (Topic) session.createQuery("FROM Topic t WHERE t.id = :id")
                    .setParameter("id", topicId)
                    .getSingleResult();

            post.setTopic(topic);
            topic.getPosts().add(post);
            session.save(topic);
            tx.commit();
        } catch (HibernateException e) {
            if(tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}

