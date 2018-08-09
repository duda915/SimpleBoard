package com.mdud.simpleboard.main;

import com.mdud.simpleboard.datamodel.Post;
import com.mdud.simpleboard.datamodel.Topic;
import com.mdud.simpleboard.datamodel.TopicManager;
import com.mdud.simpleboard.utility.GuiWrapper;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.Scanner;

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
        Scanner scanner = new Scanner(System.in);
        String command = "";

        while(!command.equals("q")) {
            command = scanner.nextLine();
            String[] parameters = command.toLowerCase().split(" ");

            switch(parameters[0]) {
                case "help":
                    System.out.println("Available commands: ");
                    System.out.println("q, ls, mktop, mkpost,");
                    break;
                case "ls":
                    if(parameters.length == 1) {
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
                    parameters = command.split(" ", 4);
                    if(parameters.length >= 4) {
                        try {
                            int topicId = Integer.parseInt(parameters[1]);
                            topicManager.addTopicPost(topicId, new Post(parameters[2], parameters[3]));
                            break;
                        } catch (NumberFormatException e) {

                        }
                    }
                    System.out.println("usage: mkpost topicId username post content");
                    break;
                case "rmtop":
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
                            topicManager.deletePost(postId);
                            break;
                        } catch (NumberFormatException e) {

                        }
                    }
                    System.out.println("usage: rmpost postId");
                    break;
                case "edtop":
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
                            topicManager.editPost(postId, new Post(parameters[2], parameters[3]));
                            break;
                        } catch (NumberFormatException e) {

                        }
                    }

                    System.out.println("Usage: edpost postId newUsername new post content");
                    break;
            }
        }

        sessionFactory.close();

    }
}
