package com.example.sportal.model.entity;

import lombok.*;

import javax.persistence.*;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "articles",
        indexes = {@Index(name = "titleIndex", columnList = "title"),@Index(name = "textIndex", columnList = "text")})
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
    @Column(columnDefinition = "text", nullable = false)
    private String text;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private User author;
    @Column
    private Date postDate;
    @OneToMany(mappedBy = "article", cascade = CascadeType.PERSIST)
    private List<Comment> comments;
    @OneToMany(mappedBy = "article", cascade = CascadeType.PERSIST)
    private List<Image> images;
    @Column(nullable = false)
    private int views = 0;
    @Column(nullable = false)
    private Integer dailyViews = 0;
}