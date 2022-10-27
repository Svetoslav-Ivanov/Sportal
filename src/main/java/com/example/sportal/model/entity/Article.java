package com.example.sportal.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private Category category;
    @Column(nullable = false)
    private String title;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private User author;
    @Column
    private Date postDate;
    @OneToMany(mappedBy = "article",cascade = CascadeType.PERSIST)
    private List<Comment> comments;
    @Column(nullable = false)
    private int views = 0;
    @Column(nullable = false)
    private Integer dailyViews = 0;
}