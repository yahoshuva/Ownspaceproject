//package com.fwitter.models;
//
//import jakarta.persistence.*;
//
//@Entity
//public class Category {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(unique = true)
//    private String name;
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public Category() {}
//
//    public Category(String name) {
//        this.name = name;
//    }
//
//}


package com.fwitter.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "category") // Ensure the table name is correct
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-increment for category_id
    @Column(name = "category_id")  // Explicit column name
    private Long id;

    @Column(unique = true)
    private String name;

    // No need to manually create `category_id`, Hibernate will manage it for you
    // No need to change this unless you want to add more custom behavior

    @ManyToMany(mappedBy = "categories")
    @JsonBackReference  // This prevents serialization of the posts in Category
    private Set<Post> posts = new HashSet<>();

    // Getters, setters, and constructors...

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    public Category() {}

    public Category(String name) {
        this.name = name;
    }
}
