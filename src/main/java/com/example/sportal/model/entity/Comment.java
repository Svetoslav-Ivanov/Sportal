package com.example.sportal.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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
    private Long id;
    @Column(nullable = false)
    private String text;
    @Column(nullable = false)
    private Date postDate;
    @ManyToOne
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private Article article;
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;
    @OneToMany//TODO
    private List<Comment> answers;
    @ManyToMany(mappedBy = "likes")
    private List<User> likedBy;
    @ManyToMany(mappedBy = "dislikes")
    private List<User> dislikedBy;
}
