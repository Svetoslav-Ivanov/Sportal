package com.example.sportal.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
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
    @OneToOne
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private Category category;
    @Column(nullable = false)
    private String title;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;
    @ManyToOne
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private User author;
    @Column
    private Date postDate;
    @OneToMany(mappedBy = "article")
    private List<Comment> comments;
    @Column(nullable = false)
    private int views = 0;
    @Column
    private int dailyViews;

    // TODO: Images

}