package com.mdud.simpleboard.datamodel;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TopicManagerTest {
    private SessionFactory sessionFactory;
    private TopicManager topicManager;

    @Before
    public void initialize() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml")
                .build();

        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);
        }

        topicManager = new TopicManager(sessionFactory);

        assertNotNull(sessionFactory);
    }

    @Test
    public void addTopic() {
        Integer topicId = topicManager.addTopic(new Topic("Test3 topic"));
        assertNotNull(topicId);
        Topic topic = topicManager.getTopic(topicId);
        assertEquals("Test3 topic", topic.getTitle());
        assertEquals(topicId.intValue(), topic.getId());
        topicManager.deleteTopic(topicId);
    }

    @Test
    public void addTopicPost() {
        Integer topicId = topicManager.addTopic(new Topic("Test topic"));
        topicManager.addTopicPost(topicId, new Post("Tester", "Test post"));
        Integer postId = topicManager.addTopicPost(topicId, new Post("Tester", "Second test post"));
        Post post = topicManager.getPost(postId);
        assertEquals("Tester", post.getPostAuthor());
        assertEquals("Second test post", post.getPostContent());
        topicManager.deleteTopic(topicId);

        //Not existing topic
        postId = topicManager.addTopicPost(topicId, new Post("Tester", "Test"));
        assertNull(postId);
    }

    @Test()
    public void editTopic() {
        // Existing topic
        Integer topicId = topicManager.addTopic(new Topic("EditTopicTest"));
        topicManager.editTopic(topicId, "EditTopicTest NewTitle");
        Topic topic = topicManager.getTopic(topicId);
        assertEquals("EditTopicTest NewTitle", topic.getTitle());
        topicManager.deleteTopic(topicId);

        // Not existing topic should return IdExceptionErrorString from TopicManager class to console
        topicId = topicManager.addTopic(new Topic("EditTopicTest"));
        topicManager.deleteTopic(topicId);
        topicManager.editTopic(topicId, "EditTopicTest NewTitle");
    }

    @Test
    public void editPost() {
        // Existing post
        Integer topicId = topicManager.addTopic(new Topic("EditPostTest"));
        Integer postId = topicManager.addTopicPost(topicId, new Post("Tester", "TestContent"));
        topicManager.editPost(postId, new Post("NewTester", "Test"));
        Post post = topicManager.getPost(postId);

        assertEquals(postId.intValue(), post.getId());
        assertEquals("NewTester", post.getPostAuthor());
        assertEquals("Test", post.getPostContent());

        //Not existing post should return IdExceptionErrorString from TopicManager class to console
        topicManager.deletePost(postId);
        topicManager.editPost(postId, new Post("NotExistingAuthor", "NotExistingPost"));

        topicManager.deleteTopic(topicId);
    }

    @Test
    public void getTopic() {
        // Existing topic
        Integer topicId = topicManager.addTopic(new Topic("TestTopic"));
        Topic topic = topicManager.getTopic(topicId);
        topicManager.deleteTopic(topicId);
        assertNotNull(topic);

        //Not existing topic
        topic = topicManager.getTopic(topicId);
        assertNull(topic);
    }

    @Test
    public void getPost() {
        Integer topicId = topicManager.addTopic(new Topic("TestTopic"));
        Integer postId = topicManager.addTopicPost(topicId, new Post("Test", "TestContent"));
        Post post = topicManager.getPost(postId);
        assertNotNull(post);

        //Cascade removing
        topicManager.deleteTopic(topicId);
        post = topicManager.getPost(postId);
        assertNull(post);
    }

    @Test
    public void deleteTopic() {
        // Existing topic
        Integer topicId = topicManager.addTopic(new Topic("TestTopic"));
        Topic topic = topicManager.getTopic(topicId);
        topicManager.deleteTopic(topicId);
        assertNotNull(topic);
        topic = topicManager.getTopic(topicId);
        assertNull(topic);

        //Not existing topic
        topicManager.deleteTopic(topicId);
    }

    @Test
    public void deletePost() {
        Integer topicId = topicManager.addTopic(new Topic("TestTopic"));
        Integer postId = topicManager.addTopicPost(topicId, new Post("Tester", "Test"));
        Post post = topicManager.getPost(postId);
        assertNotNull(post);
        topicManager.deleteTopic(topicId);
        post = topicManager.getPost(postId);
        assertNull(post);

        //Not existing topic
        topicManager.deletePost(postId);
    }
}