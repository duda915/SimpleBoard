package com.mdud.simpleboard.utility;

import com.mdud.simpleboard.datamodel.Post;
import com.mdud.simpleboard.datamodel.Topic;

import java.util.List;

public class GuiWrapper {
    public static void drawTopic(Topic topic) {
        System.out.println(topic.getId() + " || " + topic.getTitle());
        System.out.println("*****************************************");
        for(Post p : topic.getPosts()) {
            System.out.println(p.getId() + " || " + p.getPostAuthor());
            System.out.println("------------");
            System.out.println(p.getPostContent());
            System.out.println("___________________________________________");
        }
    }

    public static void drawTopicList(List<Topic> topicList) {
        for (Topic t : topicList) {
            System.out.println(t.getId() + " || " + t.getTitle());
            System.out.println("************************************");
        }
    }
}
