package com.example.sportal.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;
    @Column(unique = true, nullable = false)
    private String name;
    @OneToMany(mappedBy = "category", cascade = CascadeType.PERSIST)
    private List<Article> articles;
}
