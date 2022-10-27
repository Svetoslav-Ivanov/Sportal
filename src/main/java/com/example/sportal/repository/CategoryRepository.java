package com.example.sportal.repository;

import com.example.sportal.model.entity.Category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAll();

    boolean existsByName(String categoryName);

    Category getByName(String categoryName);

    Optional<Category> findByName(String name);

    Category getById(long id);
}

