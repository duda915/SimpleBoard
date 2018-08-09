package com.mdud.simpleboard.users;

import com.mdud.simpleboard.datamodel.TopicManager;
import junit.framework.TestCase;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserManagerTest {

    private SessionFactory sessionFactory;
    private UserManager userManager;

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

        userManager = new UserManager(sessionFactory);

        assertNotNull(sessionFactory);
    }

    @Test
    public void addUser() {
        Integer userId = userManager.addUser(new User("test", "test"));
        assertNotNull(userId);
        User user = userManager.getUser(new User("test", "test"));
        assertNotNull(user);
        userManager.deleteUser(new User("test", "test"));

        User firstUser = new User("test", "test");
        User secondUser = new User("test", "xxx");
        Integer firstUserId = userManager.addUser(firstUser);
        Integer secondUserId = userManager.addUser(secondUser);
        userManager.deleteUser(firstUser);
        assertNotNull(firstUserId);
        assertNull(secondUserId);
    }

    @Test
    public void deleteUser() {
        User testUser = new User("test", "test");
        userManager.addUser(testUser);
        userManager.deleteUser(testUser);
        User deletedUser = userManager.getUser(testUser);
        assertNull(deletedUser);

        userManager.deleteUser(testUser);
    }

    @Test
    public void getUser() {
        User testUser = new User("test", "test");
        userManager.addUser(testUser);
        User getUser = userManager.getUser(testUser);
        assertNotNull(getUser);
        userManager.deleteUser(getUser);
        getUser = userManager.getUser(getUser);
        assertNull(getUser);
    }

    @Test
    public void setUserPassword() {
        User testUser = new User("test", "test");
        userManager.addUser(testUser);
        userManager.setUserPassword(testUser, "newtest");
        User getUser = userManager.getUser(new User("test", "newtest"));
        assertNotNull(getUser);
        userManager.deleteUser(getUser);

        userManager.setUserPassword(getUser, "test");


    }
}