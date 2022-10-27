package com.example.sportal.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String text;
    @Column(nullable = false)
    private Date postDate;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(referencedColumnName = "id")
    private Article article;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.PERSIST)
    private List<Comment> answers = new ArrayList<>();

    @ManyToMany(mappedBy = "likes", cascade = CascadeType.PERSIST)
    private List<User> likedBy;
    @ManyToMany(mappedBy = "dislikes", cascade = CascadeType.PERSIST)
    private List<User> dislikedBy;

}
