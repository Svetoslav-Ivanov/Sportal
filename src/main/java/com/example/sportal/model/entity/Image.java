package com.example.sportal.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "URL", nullable = false)
    private String URL;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(nullable = false)
    private Article article;

}
