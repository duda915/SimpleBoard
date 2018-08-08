package com.mdud.simpleboard.datamodel;


import javax.persistence.*;

@Entity(name = "post")
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "topic_id")
    private int topicId;

    @Column(name = "user_name")
    private String postAuthor;

    @Column(name = "content")
    private String postContent;

    public Post() {}

    public Post(int topicId, String postAuthor, String postContent) {
        this.topicId = topicId;
        this.postAuthor = postAuthor;
        this.postContent = postContent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public String getPostAuthor() {
        return postAuthor;
    }

    public void setPostAuthor(String postAuthor) {
        this.postAuthor = postAuthor;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }
}
