package com.mdud.simpleboard.datamodel;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TopicManager {
    private SessionFactory sessionFactory;
    private String IdExceptionErrorString = "Wrong ID";

    public TopicManager(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Topic getTopic(int topicId) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        Topic topic = null;

        try {
            tx = session.beginTransaction();
            topic = session.get(Topic.class, topicId);
            tx.commit();
        } catch (HibernateException e) {
            if(tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return topic;
    }

    public List<Topic> getTopicList() {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        List<Topic> topicList = null;

        try {
            tx = session.beginTransaction();
            topicList = session.createQuery("FROM Topic ").list();
            tx.commit();
        } catch (HibernateException e) {
            if(tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return topicList;
    }

    public void listTopics() {
        List<Topic> topicList = getTopicList();

        for(Topic t : topicList) {
            System.out.println(t.getId() + " || " + t.getTitle());
        }
    }

    public Post getPost(int postId) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        Post post = null;

        try {
            tx = session.beginTransaction();
            post = session.get(Post.class, postId);
            tx.commit();
        } catch (HibernateException e) {
            if( tx != null ) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return post;
    }

    public List<Post> getTopicPosts(int topicId) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        List<Post> posts = null;

        try {
            tx = session.beginTransaction();
            Topic topic = session.get(Topic.class, topicId);
            posts = new ArrayList<>(topic.getPosts());
            tx.commit();
        } catch (HibernateException e) {
            if( tx != null ) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return posts;
    }

    public void listTopicPosts(int topicId) {
        List<Post> posts = getTopicPosts(topicId);

        for(Post p : posts) {
            System.out.println(p.getId() + " || " + p.getPostAuthor() + " || " + p.getPostContent());
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

    public Integer addTopicPost(int topicId, Post post) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        Integer postId = null;

        try {
            tx = session.beginTransaction();

            Topic topic = session.get(Topic.class, topicId);
            post.setTopic(topic);
            postId = (Integer) session.save(post);
            tx.commit();
        } catch (HibernateException e) {
            if(tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return postId;
    }

    public void editTopic(int topicId, String newTitle) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Topic topic = session.get(Topic.class, topicId);
            topic.setTitle(newTitle);
            session.save(topic);
            tx.commit();
        } catch (HibernateException e) {
            if(tx != null) tx.rollback();
            e.printStackTrace();
        } catch (NullPointerException e) {
            if(tx != null) tx.rollback();
            System.out.println(IdExceptionErrorString);
        } finally {
            session.close();
        }
    }

    public void editPost(int postId, Post newPost) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Post post = session.get(Post.class, postId);
            post.setPostAuthor(newPost.getPostAuthor());
            post.setPostContent(newPost.getPostContent());
            session.save(post);
            tx.commit();
        } catch (HibernateException e) {
            if(tx != null) tx.rollback();
            e.printStackTrace();
        } catch(NullPointerException e) {
            if(tx != null) tx.rollback();
            System.out.println(IdExceptionErrorString);
        } finally {
            session.close();
        }
    }

    public void deleteTopic(int topicId) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Topic topic = session.get(Topic.class, topicId);
            session.delete(topic);
            tx.commit();
        } catch (HibernateException e) {
            if(tx != null) tx.rollback();
            e.printStackTrace();
        } catch (NullPointerException e) {
            if(tx != null) tx.rollback();
            System.out.println(IdExceptionErrorString);
        } finally {
            session.close();
        }
    }

    public void deletePost(int postId) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Post post = session.get(Post.class, postId);
            session.delete(post);
            tx.commit();
        } catch (HibernateException e) {
            if(tx != null) tx.rollback();
            e.printStackTrace();
        } catch (NullPointerException e) {
            if(tx != null) tx.rollback();
            System.out.println(IdExceptionErrorString);
        } finally {
            session.close();
        }
    }
}

