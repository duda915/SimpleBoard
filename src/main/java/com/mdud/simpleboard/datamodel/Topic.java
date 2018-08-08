package com.mdud.simpleboard.datamodel;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity (name = "topic")
public class Topic {

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "title")
    private String title;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> list = new ArrayList<>();

    public Topic() {}

    public Topic(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Post> getList() {
        return list;
    }

    public void setList(List<Post> list) {
        this.list = list;
    }
}
