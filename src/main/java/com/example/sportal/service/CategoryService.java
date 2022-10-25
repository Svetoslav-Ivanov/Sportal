package com.example.sportal.service;

import com.example.sportal.dto.category.CategoryDTO;
import com.example.sportal.model.entity.Category;
import com.example.sportal.model.exception.*;
import com.example.sportal.repository.CategoryRepository;

import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService extends AbstractService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(c -> modelMapper.map(c, CategoryDTO.class))
                .collect(Collectors.toList());
    }

    public CategoryDTO createCategory(Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new DataAlreadyExistException("Category with this name already exists!");
        }
        if (category.getName() == null || category.getName().isBlank()) {
            throw new InvalidDataException("Invalid category name!");
        }
        categoryRepository.save(category);
        return modelMapper.map(category, CategoryDTO.class);
    }

    public boolean deleteCategory(long id) {
        Category category = getCategoryById(id);
        categoryRepository.delete(category);
        return true;
    }
}

