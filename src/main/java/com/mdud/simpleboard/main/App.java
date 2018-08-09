package com.mdud.simpleboard.main;

import com.mdud.simpleboard.datamodel.Post;
import com.mdud.simpleboard.datamodel.Topic;
import com.mdud.simpleboard.datamodel.TopicManager;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * Hello world!
 *
 */
public class App 
{
    private static SessionFactory sessionFactory;
    public static void main( String[] args )
    {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure("hibernate.cfg.xml")
            .build();

        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);
        }

        TopicManager topicManager = new TopicManager(sessionFactory);

        topicManager.listTopics();
        topicManager.listTopicPosts(1);

        sessionFactory.close();

    }
}
