package com.example.sportal.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "users", indexes = @Index(name = "usernameAndEmailIndex", columnList = "username, email"))
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private boolean isActive = true;
    @Column(nullable = false)
    private boolean isAdmin;
    @OneToMany(mappedBy = "author", cascade = CascadeType.PERSIST)
    private List<Article> articles;
    @OneToMany(mappedBy = "author", cascade = CascadeType.PERSIST)
    private List<Comment> comments;
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "users_like_comments",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<Comment> likes;
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "users_dislike_comments",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<Comment> dislikes;
}
