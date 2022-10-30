package com.example.sportal.model.entity;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Getter
@Setter
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String URI;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "article")
    private Article article;

    public Image(String URI, Article article) {
        this.URI = URI;
        this.article = article;
    }
}
