package com.mdud.simpleboard.main;

import com.mdud.simpleboard.datamodel.Post;
import com.mdud.simpleboard.datamodel.Topic;
import com.mdud.simpleboard.datamodel.TopicManager;
import com.mdud.simpleboard.users.PasswordEncrypter;
import com.mdud.simpleboard.users.User;
import com.mdud.simpleboard.users.UserManager;
import com.mdud.simpleboard.utility.GuiWrapper;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.io.Console;
import java.util.Scanner;
import java.util.logging.Level;

/**
 * Hello world!
 *
 */
public class App 
{
    private static SessionFactory sessionFactory;
    public static void main( String[] args )
    {
//        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure("hibernate.cfg.xml")
            .build();

        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);
        }

        User user = null;
        Integer activeTopic = null;

        TopicManager topicManager = new TopicManager(sessionFactory);
        UserManager userManager = new UserManager(sessionFactory);
        Scanner scanner = new Scanner(System.in);
        String command = "";

        System.out.println("SimpleBoard - Hibernate local notes board");
        System.out.println("Available commands: ");
        System.out.println("q, help, ls, ls topicId, mktop, mkpost, rmtop, rmpost, edtop, edpost, setuser, settopic");
        System.out.println("Start with setuser");

        while(!command.equals("q")) {
            command = scanner.nextLine();
            String[] parameters = command.toLowerCase().split(" ");

            switch(parameters[0]) {
                case "help":
                    System.out.println("Available commands: ");
                    System.out.println("q, ls, ls topicId, mktop, mkpost, rmtop, rmpost, edtop, edpost, setuser, settopic");

                    break;
                case "ls":
                    if(parameters.length == 1) {
                        System.out.println();
                        GuiWrapper.drawTopicList(topicManager.getTopicList());
                        break;
                    }
                    else if(parameters.length == 2) {
                        try {
                            int topicId = Integer.parseInt(parameters[1]);
                            Topic topic = topicManager.getTopic(topicId);
                            if(topic == null) {
                                System.out.println("Wrong Id");
                                break;
                            }
                            System.out.println();
                            GuiWrapper.drawTopic(topic, topicManager.getTopicPosts(topicId));
                            break;
                        } catch (NumberFormatException e) {

                        }
                    }
                    System.out.println("usage: ls [topicId]");
                    break;

                case "mktop":
                    parameters = command.split(" ", 2);
                    if(parameters.length == 2) {
                        topicManager.addTopic(new Topic(parameters[1]));
                        break;
                    }
                    System.out.println("usage: mktop topic name");
                    break;

                case "mkpost":
                    if(user == null || activeTopic == null) {
                        System.out.println("To write post you need to login and set active topic first.");
                        System.out.println("See setuser and settopic commands.");
                        break;
                    }
                    parameters = command.split(" ", 2);
                    if(parameters.length == 2) {
                        try {
                            topicManager.addTopicPost(activeTopic, new Post(user.getUserName().toLowerCase(), parameters[1]));
                            break;
                        } catch (NumberFormatException e) {

                        }
                    }
                    System.out.println("usage: mkpost post content");
                    break;
                case "rmtop":
                    if(!user.getUserName().equals("admin")) {
                        System.out.println("admin-only");
                        break;
                    }
                    parameters = command.split(" ", 2);
                    if(parameters.length == 2) {
                        try {
                            int topicId = Integer.parseInt(parameters[1]);
                            topicManager.deleteTopic(topicId);
                            break;
                        } catch (NumberFormatException e) {

                        }
                    }
                    System.out.println("usage: rmtop topicId");
                    break;
                case "rmpost":
                    parameters = command.split(" ", 2);
                    if(parameters.length == 2) {
                        try {
                            int postId = Integer.parseInt(parameters[1]);
                            Post post = topicManager.getPost(postId);
                            if(post == null)
                                break;
                            if(!user.getUserName().equals("admin") && !user.getUserName().toLowerCase().equals(post.getPostAuthor())) {
                                System.out.println("Only author can remove post!");
                                break;
                            }
                                topicManager.deletePost(postId);
                            break;
                        } catch (NumberFormatException e) {

                        }
                    }
                    System.out.println("usage: rmpost postId");
                    break;
                case "edtop":
                    if(!user.getUserName().equals("admin")) {
                        System.out.println("admin-only");
                        break;
                    }
                    parameters = command.split(" ", 3);
                    if(parameters.length == 3) {
                        try {
                            int topicId = Integer.parseInt(parameters[1]);
                            topicManager.editTopic(topicId, parameters[2]);
                            break;
                        }catch (NumberFormatException e) {

                        }
                    }
                    System.out.println("usage: edtop topicId new topic name");
                    break;
                case "edpost":
                    parameters = command.split(" ", 4);
                    if(parameters.length == 4) {
                        try {
                            int postId = Integer.parseInt(parameters[1]);

                            Post post = topicManager.getPost(postId);
                            if(post == null)
                                break;
                            if(!user.getUserName().equals("admin") && !user.getUserName().equals(post.getPostAuthor())) {
                                System.out.println("Only author can edit post!");
                                break;
                            }
                            topicManager.editPost(postId, new Post(parameters[2], parameters[3]));
                            break;
                        } catch (NumberFormatException e) {

                        }
                    }

                    System.out.println("Usage: edpost postId newUsername new post content");
                    break;

                case "setuser":
                    parameters = command.split(" ", 2);
                    if(parameters.length == 2) {
                        System.out.println("Input password:");
                        String password = scanner.nextLine();
                        String pass = password;
                        pass = PasswordEncrypter.encrypt(pass);
                        User passedUser = new User(parameters[1], pass);
                        User getUser = userManager.getUser(new User(parameters[1], pass));
                        if(getUser == null) {
                            if(userManager.addUser(passedUser) == null)
                                System.out.println("Wrong credentials");
                        }
                        user = userManager.getUser(passedUser);
                        break;
                    }

                    System.out.println("Usage: setuser username - creates user if user not exist or log in");
                    break;
                case "settopic":
                    if(parameters.length == 2) {
                        try {
                            int topicId = Integer.parseInt(parameters[1]);
                            activeTopic = topicId;
                            break;
                        } catch (NumberFormatException e) {

                        }
                    }
                    System.out.println("Usage: settopic topicId");
            }
        }

        sessionFactory.close();

    }
}
