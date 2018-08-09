package com.mdud.simpleboard.datamodel;


import javax.persistence.*;

@Entity
@Table (name = "post")
public class Post {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Topic topic;

    @Column(name = "user_name")
    private String postAuthor;

    @Column(name = "content")
    private String postContent;



    public Post() {}

    public Post(String postAuthor, String postContent) {
        this.postAuthor = postAuthor;
        this.postContent = postContent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
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
